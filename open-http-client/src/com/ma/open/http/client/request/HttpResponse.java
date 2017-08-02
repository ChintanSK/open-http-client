package com.ma.open.http.client.request;

public class HttpResponse {

	private int status;
	private Object body;

	public HttpResponse(int status) {
		this.status = status;
	}

	public HttpResponse withBody(Object body) {
		this.body = body;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Object getBody() {
		return body;
	}

}
