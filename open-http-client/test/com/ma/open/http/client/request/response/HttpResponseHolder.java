package com.ma.open.http.client.request.response;

import javax.xml.ws.Holder;

public class HttpResponseHolder {
	private Holder<HttpResponse> httpResponseHolder;

	public void setHttpResponse(HttpResponse httpResponse) {
		if (httpResponseHolder == null || httpResponseHolder.value != null) {
			throw new IllegalStateException();
		}
		httpResponseHolder = new Holder<>(httpResponse);
	}

	public HttpResponse getHttpResponse() {
		if (httpResponseHolder == null || httpResponseHolder.value == null) {
			throw new IllegalStateException();
		}
		return httpResponseHolder.value;
	}

}
