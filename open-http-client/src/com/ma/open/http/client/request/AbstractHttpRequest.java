package com.ma.open.http.client.request;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.sender.IHttpRequestSender;
import com.ma.open.http.client.request.ssl.SSLConfig;

public abstract class AbstractHttpRequest {
	protected String url;
	protected Map<String, List<Object>> headers;
	protected Map<String, List<String>> params;
	protected Map<String, Object> requestConfig;
	protected IHttpRequestSender requestSender;
	protected Object requestBody;
	private Stack<HttpResponse> responses;

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

	public boolean addHttpResponse(HttpResponse httpResponse) {
		if (responses == null) {
			responses = new Stack<>();
		}
		return responses.push(httpResponse) != null;
	}

	public HttpResponse previousResponse() {
		if (responses.isEmpty())
			return null;

		return responses.pop();
	}

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
