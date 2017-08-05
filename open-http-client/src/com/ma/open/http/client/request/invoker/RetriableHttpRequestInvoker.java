package com.ma.open.http.client.request.invoker;

import static java.lang.Thread.sleep;

import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;

class RetriableHttpRequestInvoker implements IHttpRequestInvoker {

	private final IHttpRequestInvoker invoker;
	private final IRetryPolicy retryPolicy;

	RetriableHttpRequestInvoker(IHttpRequestInvoker invoker, IRetryPolicy retryPolicy) {
		this.invoker = invoker;
		this.retryPolicy = retryPolicy;
	}

	@Override
	public HttpResponse invoke(AbstractHttpRequest httpRequest) {
		HttpResponse httpResponse = null;
		OfInt intervals = retryPolicy.intervals().iterator();
		int attempts = -1;
		do {
			try {
				attempts++;
				if (attempts > 0) {
					sleep(intervals.nextInt());
				}
				httpResponse = invoker.invoke(httpRequest);
			} catch (Exception e) {
				if (retryPolicy.failOnException()) {
					throw new RuntimeException(e);
				}
			}
		} while (attempts < retryPolicy.maxCount() && retryPolicy.shouldContinue().test(httpResponse));
		return httpResponse;
	}

	@Override
	public Future<HttpResponse> invokeAsync(AbstractHttpRequest httpRequest) {
		return HttpRequestInvoker.POOL.submit(new Callable<HttpResponse>() {

			@Override
			public HttpResponse call() throws Exception {
				return invoke(httpRequest);
			}

		});
	}

}
