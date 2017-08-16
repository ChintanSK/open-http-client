package com.ma.open.http.client.request.invoker;

import static com.ma.open.http.client.request.scheduler.DefaultHttpRequestScheduler.DEFAULT_SCHEDULER;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.IDelayedHttpResponseHandler;
import com.ma.open.http.client.request.response.IScheduledHttpResponseHandler;
import com.ma.open.http.client.request.response.ScheduledHttpResponseHandler;
import com.ma.open.http.client.request.scheduler.IHttpRequestScheduler;
import com.ma.open.http.client.utils.HttpDateUtils;
import com.ma.open.http.client.utils.NumberUtils;

public class DefaultRetryAfterHandler implements IRetryAfterHandler {

	private IHttpRequestScheduler httpRequestScheduler = DEFAULT_SCHEDULER;
	private IScheduledHttpResponseHandler scheduledHttpResponseHandler;

	public DefaultRetryAfterHandler(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
		scheduledHttpResponseHandler = new ScheduledHttpResponseHandler(delayedHttpResponseHandler);
	}

	@Override
	public void handleRetryAfterResponse(IHttpRequestInvoker invoker, HttpResponse httpResponse) {
		httpResponse.getHttpRequest().addHttpResponse(httpResponse);
		final ScheduledFuture<HttpResponse> delayedHttpResponse = httpRequestScheduler
				.scheduleRequestInvocation(invoker, httpResponse.getHttpRequest(), retryAfterMillis(httpResponse));
		httpResponse.withScheduledHttpResponse(delayedHttpResponse);
		scheduledHttpResponseHandler.handleScheduledHttpResponse(delayedHttpResponse);
	}

	private long retryAfterMillis(HttpResponse httpResponse) {
		Duration untilNextRetry;
		String retryAfterValue = httpResponse.getHeaders().get("Retry-After");
		if (NumberUtils.isWholeNumber(retryAfterValue)) {
			untilNextRetry = Duration.ofSeconds(NumberUtils.longValue(retryAfterValue));
		} else {
			final Instant now = Instant.now();
			untilNextRetry = Duration.between(now, HttpDateUtils.instant(retryAfterValue));
			if (untilNextRetry.isNegative() || untilNextRetry.isZero()) {
				untilNextRetry = Duration.ofSeconds(0);
			}
		}
		return untilNextRetry.toMillis();
	}

}
