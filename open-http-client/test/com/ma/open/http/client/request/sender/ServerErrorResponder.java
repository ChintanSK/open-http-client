package com.ma.open.http.client.request.sender;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.ssl.SSLConfig;

public class ServerErrorResponder implements IHttpRequestSender {

	private final int maxErrorCount;
	private int getCount = 0;
	private int postCount = 0;

	public ServerErrorResponder(int maxErrorCount) {
		this.maxErrorCount = maxErrorCount;
	}

	@Override
	public void configureSsl(SSLConfig sslConfig) {
	}

	@Override
	public HttpResponse get(AbstractHttpRequest getRequest) {
		getCount++;
		if (getCount <= maxErrorCount) {
			return new HttpResponse(503);
		} else {
			return new HttpResponse(200).withBody("You made it here after " + maxErrorCount + " server errors");
		}
	}

	@Override
	public HttpResponse post(AbstractHttpRequest postRequest) {
		postCount++;
		if (postCount <= maxErrorCount) {
			return new HttpResponse(503);
		} else {
			return new HttpResponse(204);
		}
	}

}
