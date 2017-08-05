package com.ma.open.http.client.request.invoker;

import java.util.concurrent.Future;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;

public interface IHttpRequestInvoker {

	public static final IHttpRequestInvoker requestInvoker = new HttpRequestInvoker();

	HttpResponse invoke(AbstractHttpRequest httpRequest);

	Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest);

	public static IHttpRequestInvoker retriableInvoker(IRetryPolicy retryPolicy) {
		return new RetriableHttpRequestInvoker(requestInvoker, retryPolicy);
	}

}
