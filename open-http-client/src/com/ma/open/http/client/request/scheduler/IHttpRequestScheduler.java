package com.ma.open.http.client.request.scheduler;

import java.util.concurrent.ScheduledFuture;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.invoker.IHttpRequestInvoker;
import com.ma.open.http.client.request.response.HttpResponse;

public interface IHttpRequestScheduler {

	ScheduledFuture<HttpResponse> scheduleRequestInvocation(IHttpRequestInvoker invoker,
			AbstractHttpRequest httpRequest, long delay);

}
