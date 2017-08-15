package com.ma.open.http.client.request.response;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.ma.open.http.client.request.AbstractHttpRequest;

public class HttpResponse {
	private AbstractHttpRequest httpRequest;

	private int status;
	private Object body;
	private Map<String, String> headers;

	private ScheduledFuture<HttpResponse> scheduledHttpResponse;
	private boolean scheduled;

	public HttpResponse(int status, ScheduledFuture<HttpResponse> futureHttpResponse) {
		this.status = status;
		this.scheduledHttpResponse = futureHttpResponse;
		scheduled = true;
	}

	public HttpResponse(int status) {
		this.status = status;
	}

	public HttpResponse withBody(Object body) {
		this.body = body;
		return this;
	}

	public HttpResponse withHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public HttpResponse withHttpRequest(AbstractHttpRequest httpRequest) {
		this.httpRequest = httpRequest;
		return this;
	}

	public HttpResponse withScheduledHttpResponse(ScheduledFuture<HttpResponse> scheduledHttpResponse) {
		this.scheduledHttpResponse = scheduledHttpResponse;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Object getBody() {
		return body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public AbstractHttpRequest getHttpRequest() {
		return httpRequest;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public ScheduledFuture<HttpResponse> getFutureHttpResponse() {
		return scheduledHttpResponse;
	}

	@Override
	public String toString() {
		StringBuffer string = new StringBuffer();
		if (scheduled) {
			string.append("Scheduled:");
		}
		string.append(status);
		if (body != null) {
			string.append(":" + body);
		}
		return string.toString();
	}

}
