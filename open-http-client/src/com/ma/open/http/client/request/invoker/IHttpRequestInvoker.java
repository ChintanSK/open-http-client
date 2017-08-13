package com.ma.open.http.client.request.invoker;

import java.util.concurrent.Future;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.FutureHttpResponseHandler;
import com.ma.open.http.client.request.response.HttpResponse;

public interface IHttpRequestInvoker {

	static IHttpRequestInvoker newInvoker() {
		return new HttpRequestInvoker();
	}

	static IHttpRequestInvoker newRetriableInvoker(IRetryPolicy retryPolicy) {
		return new RetriableHttpRequestInvoker(retryPolicy);
	}

	HttpResponse invoke(AbstractHttpRequest httpRequest, FutureHttpResponseHandler callback);

	Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest, FutureHttpResponseHandler callback);

	default boolean isResponseHandled() {
		return false;
	}

}
