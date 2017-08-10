package com.ma.open.http.client.request.sender;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;
import com.ma.open.http.client.request.ssl.SSLConfig;

public interface IHttpRequestSender {

	void configureSsl(SSLConfig sslConfig);

	HttpResponse get(AbstractHttpRequest getRequest);

	HttpResponse post(AbstractHttpRequest postRequest);

}
