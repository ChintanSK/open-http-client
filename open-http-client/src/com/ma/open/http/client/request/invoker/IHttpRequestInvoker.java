package com.ma.open.http.client.request.invoker;

import java.util.concurrent.Future;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.IDelayedHttpResponseHandler;

public interface IHttpRequestInvoker {

	static IHttpRequestInvoker newInvoker() {
		return new HttpRequestInvoker();
	}

	static IHttpRequestInvoker newRetriableInvoker(IRetryPolicy retryPolicy) {
		return new RetriableHttpRequestInvoker(retryPolicy);
	}

	HttpResponse invoke(AbstractHttpRequest httpRequest);

	Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest);

	void disableRetryAfter();

	IRetryAfterHandler getRetryAfterHandler();

	IRedirectionHandler getRedirectionHandler();

	IDelayedHttpResponseHandler getDelayedHttpResponseHandler();
}
