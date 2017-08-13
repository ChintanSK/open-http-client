package com.ma.open.http.client.request.sender;

import java.util.HashMap;
import java.util.Map;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.ssl.SSLConfig;

public class ApacheHttpRequestSender implements IHttpRequestSender {

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
		Map<String, String> responseHeaders = new HashMap<>();
//		responseHeaders.put("Retry-After", "Tue, 15 Aug 2017 17:00:00 GMT");
		responseHeaders.put("Retry-After", "3");
		return new HttpResponse(301).withHeaders(responseHeaders);
	}

}
