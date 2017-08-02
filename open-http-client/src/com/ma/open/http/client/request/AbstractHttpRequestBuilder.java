package com.ma.open.http.client.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ma.open.http.client.request.sender.IHttpRequestSender;

public abstract class AbstractHttpRequestBuilder {

	protected String url;
	protected Map<String, List<Object>> headers = new HashMap<>();
	protected Map<String, List<String>> params;
	protected Map<String, Object> requestConfig;
	protected IHttpRequestSender requestSender;

	public abstract AbstractHttpRequest build();

	public AbstractHttpRequestBuilder header(String name, String... values) {
		if (headers == null) {
			headers = new HashMap<>();
		}
		List<Object> existingValues = headers.get(name);
		if (existingValues == null) {
			existingValues = new ArrayList<>();
			headers.put(name, existingValues);
		}
		existingValues.addAll(Arrays.asList(values));
		return this;
	}

	public AbstractHttpRequestBuilder headers(Map<String, List<Object>> headers) {
		if (headers == null) {
			this.headers = null;
			return this;
		}
		if (headers.size() > 0) {
			headers.keySet().parallelStream().forEach(name -> {
				if (this.headers.containsKey(name)) {
					this.headers.get(name).addAll(headers.get(name));
				} else {
					this.headers.put(name, headers.get(name));
				}
			});
		}
		return this;
	}

	public AbstractHttpRequestBuilder finalHeaders(Map<String, List<Object>> finalHeaders) {
		this.headers = finalHeaders;
		return this;
	}

	public AbstractHttpRequestBuilder param(String name, String... values) {
		if (params == null) {
			params = new HashMap<>();
		}
		List<String> existingValues = params.get(name);
		if (existingValues == null) {
			existingValues = new ArrayList<>();
		}
		existingValues.addAll(Arrays.asList(values));
		return this;
	}

	public AbstractHttpRequestBuilder contentType(String contentType) {
		return header("Content-Type", contentType);
	}

	public AbstractHttpRequestBuilder accept(String... acceptTypes) {
		return header("Accept", acceptTypes);
	}

	public AbstractHttpRequestBuilder requestConfig(String key, String value) {
		if (requestConfig == null) {
			requestConfig = new HashMap<>();
		}
		requestConfig.put(key, value);
		return this;
	}

	public AbstractHttpRequestBuilder requestConfigs(Map<String, Object> requestConfigs) {
		this.requestConfig = requestConfigs;
		return this;
	}

	public AbstractHttpRequestBuilder secure(SSLConfig sslConfig) {
		return new SecureRequest.Builder(this, sslConfig);
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

	public static GetRequest.Builder aGetRequest(String url, IHttpRequestSender requestSender) {
		return new GetRequest.Builder(url, requestSender);
	}

	public static PostRequest.Builder aPostRequest(String url, IHttpRequestSender requestSender, Object content) {
		return new PostRequest.Builder(url, requestSender).content(content);
	}

}
