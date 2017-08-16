package com.ma.open.http.client.request.invoker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.IDelayedHttpResponseHandler;

class HttpRequestInvoker implements IHttpRequestInvoker {
	static final ExecutorService POOL = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

	private IRetryAfterHandler retryAfterHandler;
	private IRedirectionHandler redirectionHandler;
	private IDelayedHttpResponseHandler delayedHttpResponseHandler;

	private boolean responseHandled;
	private boolean retryAfterEnabled = true;

	HttpRequestInvoker() {
	}

	@Override
	public void disableRetryAfter() {
		retryAfterEnabled = false;
	}

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest) {
		return doInvoke(httpRequest);
	}

	@Override
	public Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest) {
		return POOL.submit(() -> {
			return invoke(httpRequest);
		});
	}

	public boolean isResponseHandled() {
		return responseHandled;
	}

	private HttpResponse doInvoke(AbstractHttpRequest httpRequest) {
		final HttpResponse httpResponse = httpRequest.send().withHttpRequest(httpRequest);
		if (retryAfterEnabled && retryAfter(httpResponse)) {
			responseHandled = true;
			getRetryAfterHandler().handleRetryAfterResponse(this, httpResponse);
		}
		return httpResponse;
	}

	private boolean retryAfter(HttpResponse httpResponse) {
		return httpResponse != null && (httpResponse.getStatus() == 503 || httpResponse.getStatus() == 301)
				&& httpResponse.getHeaders() != null && httpResponse.getHeaders().containsKey("Retry-After");
	}

	@Override
	public IRetryAfterHandler getRetryAfterHandler() {
		return retryAfterHandler;
	}

	public void setRetryAfterHandler(IRetryAfterHandler retryAfterHandler) {
		this.retryAfterHandler = retryAfterHandler;
	}

	@Override
	public IRedirectionHandler getRedirectionHandler() {
		return redirectionHandler;
	}

	public void setRedirectionHandler(IRedirectionHandler redirectionHandler) {
		this.redirectionHandler = redirectionHandler;
	}

	@Override
	public IDelayedHttpResponseHandler getDelayedHttpResponseHandler() {
		return delayedHttpResponseHandler;
	}

	public void setDelayedHttpResponseHandler(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
		this.delayedHttpResponseHandler = delayedHttpResponseHandler;
	}

}
