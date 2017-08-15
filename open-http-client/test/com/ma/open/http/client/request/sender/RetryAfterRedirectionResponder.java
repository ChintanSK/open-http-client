package com.ma.open.http.client.request.sender;

import java.util.HashMap;
import java.util.Map;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.ssl.SSLConfig;

public class RetryAfterRedirectionResponder implements IHttpRequestSender {

	@Override
	public void configureSsl(SSLConfig sslConfig) {
	}

	@Override
	public HttpResponse get(AbstractHttpRequest getRequest) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Retry-After", "120");
		return new HttpResponse(301).withHeaders(headers);
	}

	@Override
	public HttpResponse post(AbstractHttpRequest postRequest) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Retry-After", "120");
		return new HttpResponse(301).withHeaders(headers);
	}

}
