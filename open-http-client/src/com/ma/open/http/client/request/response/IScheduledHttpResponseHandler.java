package com.ma.open.http.client.request.response;

import java.util.concurrent.ScheduledFuture;

public interface IScheduledHttpResponseHandler {

	void handleScheduledHttpResponse(ScheduledFuture<HttpResponse> scheduledHttpResponse);

	IDelayedHttpResponseHandler getDelayedHttpResponseHandler();

}
