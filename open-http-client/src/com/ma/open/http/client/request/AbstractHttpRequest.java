package com.ma.open.http.client.request;

import java.util.List;
import java.util.Map;

import com.ma.open.http.client.request.sender.IHttpRequestSender;
import com.ma.open.http.client.request.ssl.SSLConfig;

public abstract class AbstractHttpRequest {
	protected String url;
	protected Map<String, List<Object>> headers;
	protected Map<String, List<String>> params;
	protected Map<String, Object> requestConfig;
	protected IHttpRequestSender requestSender;
	protected Object requestBody;

	protected AbstractHttpRequest() {
	}

	protected AbstractHttpRequest(AbstractHttpRequestBuilder abstractBuilder) {
		this.url = abstractBuilder.getUrl();
		this.headers = abstractBuilder.getHeaders();
		this.params = abstractBuilder.getParams();
		this.requestConfig = abstractBuilder.getRequestConfig();
		this.requestSender = abstractBuilder.getRequestSender();
		this.requestBody = abstractBuilder.getRequestBody();
	}

	public abstract HttpResponse send();

	public String getUrl() {
		return url;
	}

	public Map<String, List<Object>> getHeaders() {
		return headers;
	}

	public Map<String, List<String>> getParams() {
		return params;
	}

	public Map<String, Object> getRequestConfig() {
		return requestConfig;
	}

	public IHttpRequestSender getRequestSender() {
		return requestSender;
	}

	protected SSLConfig getSslConfig() {
		return null;
	}

	protected Object getRequestBody() {
		return null;
	}

}
