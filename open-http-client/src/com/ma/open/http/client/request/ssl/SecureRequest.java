package com.ma.open.http.client.request.ssl;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.AbstractHttpRequestBuilder;
import com.ma.open.http.client.request.HttpResponse;

public class SecureRequest extends AbstractHttpRequest {
	private AbstractHttpRequest wrappedRequest;
	private SSLConfig sslConfig;

	private SecureRequest(Builder builder) {
		this.wrappedRequest = builder.wrappedBuilder.build();
		this.requestSender = this.wrappedRequest.getRequestSender();
		this.sslConfig = builder.sslConfig;
	}

	@Override
	public HttpResponse send() {
		requestSender.configureSsl(sslConfig);
		return wrappedRequest.send();
	}

	@Override
	public SSLConfig getSslConfig() {
		return sslConfig;
	}

	public static class Builder extends AbstractHttpRequestBuilder {

		private AbstractHttpRequestBuilder wrappedBuilder;
		private SSLConfig sslConfig;

		public Builder(AbstractHttpRequestBuilder wrappedBuilder, SSLConfig sslConfig) {
			this.wrappedBuilder = wrappedBuilder;
			this.sslConfig = sslConfig;
		}

		@Override
		public AbstractHttpRequest build() {
			return new SecureRequest(this);
		}

	}

}
