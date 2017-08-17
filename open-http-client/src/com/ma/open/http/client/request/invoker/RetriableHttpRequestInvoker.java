package com.ma.open.http.client.request.invoker;

import static java.lang.Thread.sleep;

import java.time.Duration;
import java.time.Instant;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.retry.IRetryPolicy;

class RetriableHttpRequestInvoker extends HttpRequestInvoker {

	// TODO: this should come from a property file
	private static final int MAX_ALLOWED_ATTEMPTS = 10;

	private final IRetryPolicy retryPolicy;

	RetriableHttpRequestInvoker(IRetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest) {
		HttpResponse httpResponse = null;
		OfInt intervals = retryPolicy.intervals().iterator();
		Instant invocationStartInstant = Instant.now();
		int attempts = -1;
		do {
			try {
				attempts++;
				if (attempts > 0) {
					checkElapsedTime(invocationStartInstant);
					System.out.println("DEBUG: " + "attempt " + attempts);
					sleepFor(Duration.ofMillis(intervals.next()));
				}
				httpResponse = super.invoke(httpRequest);
				if (httpResponse.isScheduled() && super.isResponseHandled()) {
					break;
				}
				checkAttempts(attempts);
			} catch (Exception e) {
				if (retryPolicy.failOnException(e)) {
					throw new RuntimeException(e);
				}
			}
		} while (retryPolicy.shouldContinueRetrying().test(httpResponse));
		return httpResponse;
	}

	@Override
	public Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest) {
		return POOL.submit(new Callable<HttpResponse>() {

			@Override
			public HttpResponse call() throws Exception {
				return invoke(httpRequest);
			}

		});
	}

	private void checkElapsedTime(Instant invocationStartInstant) throws TimeoutException {
		if (Duration.between(invocationStartInstant, Instant.now()).compareTo(retryPolicy.maxTurnAroundTime()) > 0) {
			throw new TimeoutException("Request timed out");
		}
	}

	private void checkAttempts(int attempts) throws TimeoutException {
		if (attempts >= retryPolicy.maxAttempts() || attempts >= MAX_ALLOWED_ATTEMPTS) {
			throw new TimeoutException("Maximum retrial attempts exceeded");
		}
	}

	private void sleepFor(Duration sleepDuration) throws InterruptedException {
		sleep(sleepDuration.toMillis());
	}

}
