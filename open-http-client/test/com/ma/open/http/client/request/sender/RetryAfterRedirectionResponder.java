package com.ma.open.http.client.request.sender;

import java.util.HashMap;
import java.util.Map;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.ssl.SSLConfig;

public class RetryAfterRedirectionResponder implements IHttpRequestSender {

	private final int maxRetryAfterResponseCount;
	private int getCount = 0;
	private int postCount = 0;

	public RetryAfterRedirectionResponder(int maxRetryAfterResponseCount) {
		this.maxRetryAfterResponseCount = maxRetryAfterResponseCount;
	}

	@Override
	public void configureSsl(SSLConfig sslConfig) {
	}

	@Override
	public HttpResponse get(AbstractHttpRequest getRequest) {
		getCount++;
		if (getCount <= maxRetryAfterResponseCount) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Retry-After", "120");
			return new HttpResponse(301).withHeaders(headers);
		} else {
			return new HttpResponse(200)
					.withBody("You made it after " + maxRetryAfterResponseCount + " redirected GET retries");
		}
	}

	@Override
	public HttpResponse post(AbstractHttpRequest postRequest) {
		postCount++;
		if (postCount <= maxRetryAfterResponseCount) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Retry-After", "120");
			return new HttpResponse(301).withHeaders(headers);
		} else {
			return new HttpResponse(204);
		}
	}

}
