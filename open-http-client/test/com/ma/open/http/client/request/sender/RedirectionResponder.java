package com.ma.open.http.client.request.sender;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.ssl.SSLConfig;

public class RedirectionResponder implements IHttpRequestSender {

	@Override
	public void configureSsl(SSLConfig sslConfig) {
	}

	@Override
	public HttpResponse get(AbstractHttpRequest getRequest) {
		return new HttpResponse(301);
	}

	@Override
	public HttpResponse post(AbstractHttpRequest postRequest) {
		return new HttpResponse(301);
	}

}
