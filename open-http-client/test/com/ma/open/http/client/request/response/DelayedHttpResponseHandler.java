package com.ma.open.http.client.request.response;

public class DelayedHttpResponseHandler implements IDelayedHttpResponseHandler {

	private HttpResponseHolder httpResponseHolder;

	public DelayedHttpResponseHandler(HttpResponseHolder httpResponseHolder) {
		this.httpResponseHolder = httpResponseHolder;
	}

	@Override
	public void handleDelayedHttpResponse(HttpResponse delayedHttpResponse) {
		System.out.println(delayedHttpResponse);
		httpResponseHolder.setHttpResponse(delayedHttpResponse);
	}

}
