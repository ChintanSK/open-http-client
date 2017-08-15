package com.ma.open.http.client.request.invoker;

import static com.ma.open.http.client.request.invoker.AbstractRetryPolicy.MAX_ATTEMPTS;
import static java.lang.Thread.sleep;

import java.time.Duration;
import java.time.Instant;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.ScheduledHttpResponseHandler;
import com.ma.open.http.client.request.response.HttpResponse;

class RetriableHttpRequestInvoker extends HttpRequestInvoker {

	private final IRetryPolicy retryPolicy;

	RetriableHttpRequestInvoker(IRetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest, ScheduledHttpResponseHandler responseHandler) {
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
				httpResponse = super.invoke(httpRequest, responseHandler);
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
	public Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest,
			ScheduledHttpResponseHandler responseHandler) {
		return POOL.submit(new Callable<HttpResponse>() {

			@Override
			public HttpResponse call() throws Exception {
				return invoke(httpRequest, responseHandler);
			}

		});
	}

	private void checkElapsedTime(Instant invocationStartInstant) throws TimeoutException {
		if (Duration.between(invocationStartInstant, Instant.now()).compareTo(retryPolicy.maxTurnAroundTime()) > 0) {
			throw new TimeoutException("Request timed out");
		}
	}

	private void checkAttempts(int attempts) throws TimeoutException {
		if (attempts >= retryPolicy.maxAttempts() || attempts >= MAX_ATTEMPTS) {
			throw new TimeoutException("Maximum retrial attempts exceeded");
		}
	}

	private void sleepFor(Duration sleepDuration) throws InterruptedException {
		sleep(sleepDuration.toMillis());
	}

}
