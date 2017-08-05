package com.ma.open.http.client.request.invoker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;

class HttpRequestInvoker implements IHttpRequestInvoker {

	HttpRequestInvoker() {
		// TODO Auto-generated constructor stub
	}

	static final ExecutorService POOL = Executors.newCachedThreadPool();

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest) {
		return httpRequest.send();
	}

	@Override
	public Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest) {
		return POOL.submit(new Callable<HttpResponse>() {

			@Override
			public HttpResponse call() throws Exception {
				return httpRequest.send();
			}

		});
	}

}
