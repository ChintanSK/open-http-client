package com.ma.open.http.client.request;

import java.util.Map;

public class HttpResponse {

	private int status;
	private Object body;
	private Map<String, String> headers;

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

	public int getStatus() {
		return status;
	}

	public Object getBody() {
		return body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

}
