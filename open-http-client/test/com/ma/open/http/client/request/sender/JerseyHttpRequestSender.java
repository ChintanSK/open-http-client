package com.ma.open.http.client.request.sender;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;
import com.ma.open.http.client.request.SSLConfig;
import com.ma.open.http.client.request.sender.IHttpRequestSender;

public class JerseyHttpRequestSender implements IHttpRequestSender {

	@Override
	public void configureSsl(SSLConfig sslConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public HttpResponse get(AbstractHttpRequest getRequest) {
		// TODO Auto-generated method stub
		return new HttpResponse(200).withBody("Hello Open Http Client. I know you are there with my client");
	}

	@Override
	public HttpResponse post(AbstractHttpRequest postRequest) {
		// TODO Auto-generated method stub
		return new HttpResponse(204);
	}

}
