package com.ma.open.http.client.request.invoker;

import com.ma.open.http.client.request.response.IDelayedHttpResponseHandler;

public class HttpRequestInvokerBuilder {

	private IDelayedHttpResponseHandler delayedHttpResponseHandler;

	private HttpRequestInvokerBuilder(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
		this.delayedHttpResponseHandler = delayedHttpResponseHandler;
		this.retryAfterHandler = new DefaultRetryAfterHandler(delayedHttpResponseHandler);
	}

	public static HttpRequestInvokerBuilder newBuilder(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
		return new HttpRequestInvokerBuilder(delayedHttpResponseHandler);
	}

	private IRetryPolicy retryPolicy;
	private boolean retryAfterDisabled = false;
	private IRetryAfterHandler retryAfterHandler;

	public HttpRequestInvokerBuilder withRetryPolicy(IRetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
		return this;
	}

	public HttpRequestInvokerBuilder disableRetryAfter() {
		this.retryAfterDisabled = true;
		return this;
	}

	public HttpRequestInvokerBuilder withRetryAfterHandler(IRetryAfterHandler retryAfterHandler) {
		this.retryAfterHandler = retryAfterHandler;
		return this;
	}

	public IHttpRequestInvoker build() {
		IHttpRequestInvoker invoker;
		if (retryPolicy != null) {
			invoker = new RetriableHttpRequestInvoker(retryPolicy);
		} else {
			invoker = new HttpRequestInvoker();
		}

		if (retryAfterDisabled)
			invoker.disableRetryAfter();

		((HttpRequestInvoker) invoker).setRetryAfterHandler(retryAfterHandler);
		((HttpRequestInvoker) invoker).setDelayedHttpResponseHandler(delayedHttpResponseHandler);
		return invoker;
	}

}
