package com.ma.open.http.client.request.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.invoker.IHttpRequestInvoker;
import com.ma.open.http.client.request.response.HttpResponse;

public enum DefaultHttpRequestScheduler implements IHttpRequestScheduler {
	DEFAULT_SCHEDULER;

	// TODO: The core pool size should come from some external configuration
	private ScheduledExecutorService POOL = Executors.newScheduledThreadPool(10);

	@Override
	public ScheduledFuture<HttpResponse> scheduleRequestInvocation(IHttpRequestInvoker invoker,
			AbstractHttpRequest httpRequest, long delay) {
		return POOL.schedule(new Callable<HttpResponse>() {

			@Override
			public HttpResponse call() throws Exception {
				return invoker.invoke(httpRequest);
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
}
