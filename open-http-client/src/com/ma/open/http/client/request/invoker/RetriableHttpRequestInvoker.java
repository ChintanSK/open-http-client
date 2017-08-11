package com.ma.open.http.client.request.invoker;

import static com.ma.open.http.client.request.invoker.AbstractRetryPolicy.MAX_ATTEMPTS;
import static com.ma.open.http.client.request.invoker.HttpRequestInvoker.POOL;
import static java.lang.Thread.sleep;

import java.time.Duration;
import java.time.Instant;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;
import com.ma.open.http.client.utils.HttpDateUtils;
import com.ma.open.http.client.utils.NumberUtils;

class RetriableHttpRequestInvoker implements IHttpRequestInvoker {

	private final IHttpRequestInvoker invoker;
	private final IRetryPolicy retryPolicy;
	private final boolean retryAfterEnabled;

	RetriableHttpRequestInvoker(IHttpRequestInvoker invoker, IRetryPolicy retryPolicy) {
		this.invoker = invoker;
		this.retryPolicy = retryPolicy;
		this.retryAfterEnabled = retryPolicy.retryAfterEnabled();
	}

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest) {
		HttpResponse httpResponse = null;
		boolean retryAfterTriggered = false;
		Duration retryAfterDuration = null;
		OfInt intervals = retryPolicy.intervals().iterator();
		Instant invocationStartInstant = Instant.now();
		int attempts = -1;
		do {
			try {
				attempts++;
				checkElapsedTime(invocationStartInstant);
				if (attempts > 0) {
					System.out.println("DEBUG: " + "attempt " + attempts);
					if (retryAfterTriggered) {
						if (retryAfterDuration.compareTo(retryPolicy.maxTurnAroundTime()
								.minus(Duration.between(invocationStartInstant, Instant.now()))) > 0) {
							System.out.println(
									"The Retry-After value is beyond the fixed max turn around time for this request");
							break;
						}
						sleepFor(retryAfterDuration);
						retryAfterTriggered = false;
						retryAfterDuration = null;
					} else {
						sleepFor(Duration.ofMillis(intervals.next()));
					}
				}
				httpResponse = invoker.invoke(httpRequest);
				if (retryAfterTriggered = retryAfterTriggered(httpResponse)) {
					retryAfterDuration = calculateRetryAfterDuration(httpResponse);
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

	private Duration calculateRetryAfterDuration(HttpResponse httpResponse) {
		String retryAfterValue = httpResponse.getHeaders().get("Retry-After");
		if (NumberUtils.isWholeNumber(retryAfterValue)) {
			return Duration.ofSeconds(NumberUtils.longValue(retryAfterValue));
		}
		final Instant now = Instant.now();
		Duration durationFromRetryAfterDateTime = Duration.between(now, HttpDateUtils.instant(retryAfterValue));
		if (durationFromRetryAfterDateTime.isNegative() || durationFromRetryAfterDateTime.isZero()) {
			return Duration.ofSeconds(0);
		}
		return durationFromRetryAfterDateTime;
	}

	private boolean retryAfterTriggered(HttpResponse httpResponse) {
		return retryAfterEnabled && httpResponse.getHeaders() != null
				&& httpResponse.getHeaders().containsKey("Retry-After");
	}
	
	private void checkElapsedTime(Instant invocationStartInstant) throws TimeoutException {
		if (Duration.between(invocationStartInstant, Instant.now())
				.compareTo(retryPolicy.maxTurnAroundTime()) > 0) {
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
