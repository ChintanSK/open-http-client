package com.ma.open.http.client.request.invoker;

import static com.ma.open.http.client.request.scheduler.HttpRequestScheduler.SCHEDULER;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.ScheduledHttpResponseHandler;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.utils.HttpDateUtils;
import com.ma.open.http.client.utils.NumberUtils;

class HttpRequestInvoker implements IHttpRequestInvoker {
	static final ExecutorService POOL = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

	private boolean responseHandled;
	private boolean retryAfterEnabled = true;

	HttpRequestInvoker() {
	}

	@Override
	public void disableRetryAfter() {
		retryAfterEnabled = false;
	}

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest, ScheduledHttpResponseHandler responseHandler) {
		return doInvoke(httpRequest, responseHandler);
	}

	@Override
	public Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest,
			ScheduledHttpResponseHandler responseHandler) {

		return POOL.submit(() -> {
			return doInvoke(httpRequest, responseHandler);
		});
	}

	@Override
	public boolean isResponseHandled() {
		return responseHandled;
	}

	private HttpResponse doInvoke(AbstractHttpRequest httpRequest, ScheduledHttpResponseHandler responseHandler) {
		final HttpResponse httpResponse = httpRequest.send().withHttpRequest(httpRequest);
		if (retryAfterEnabled && retryAfter(httpResponse)) {
			responseHandled = true;
			final ScheduledFuture<HttpResponse> delayedHttpResponse = scheduleHttpRequest(this, httpRequest,
					httpResponse, responseHandler);
			httpResponse.withScheduledHttpResponse(delayedHttpResponse);
			responseHandler.handleScheduledHttpResponse(delayedHttpResponse);
		}
		return httpResponse;
	}

	private boolean retryAfter(HttpResponse httpResponse) {
		return httpResponse != null && (httpResponse.getStatus() == 503 || httpResponse.getStatus() == 301)
				&& httpResponse.getHeaders() != null && httpResponse.getHeaders().containsKey("Retry-After");
	}

	private ScheduledFuture<HttpResponse> scheduleHttpRequest(HttpRequestInvoker httpRequestInvoker,
			AbstractHttpRequest httpRequest, HttpResponse httpResponse, ScheduledHttpResponseHandler responseHandler) {
		httpRequest.addHttpResponse(httpResponse);
		return SCHEDULER.scheduleRequestInvocation(httpRequestInvoker, httpRequest, retryAfterMillis(httpResponse),
				responseHandler);
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
