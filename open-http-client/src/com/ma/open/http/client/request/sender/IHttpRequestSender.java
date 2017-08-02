package com.ma.open.http.client.request.sender;

import com.ma.open.http.client.request.GetRequest;
import com.ma.open.http.client.request.HttpResponse;
import com.ma.open.http.client.request.PostRequest;
import com.ma.open.http.client.request.SSLConfig;

public interface IHttpRequestSender {

	void configureSsl(SSLConfig sslConfig);

	HttpResponse get(GetRequest getRequest);

	HttpResponse post(PostRequest postRequest);

}
