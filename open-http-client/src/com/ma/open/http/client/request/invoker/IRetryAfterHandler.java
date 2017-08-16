package com.ma.open.http.client.request.invoker;

import com.ma.open.http.client.request.response.HttpResponse;

public interface IRetryAfterHandler {

	void handleRetryAfterResponse(IHttpRequestInvoker invoker, HttpResponse httpResponse);
}
