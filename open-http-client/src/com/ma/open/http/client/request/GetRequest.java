package com.ma.open.http.client.request;

import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.sender.IHttpRequestSender;

class GetRequest extends AbstractHttpRequest {

	private GetRequest(Builder builder) {
		super(builder);
	}

	@Override
	public HttpResponse send() {
		return requestSender.get(this);
	}

	static class Builder extends AbstractHttpRequestBuilder {

		public Builder(String url, IHttpRequestSender requestSender) {
			this.url = url;
			this.requestSender = requestSender;
		}

		@Override
		public AbstractHttpRequest build() {
			return new GetRequest(this);
		}

		@Override
		public AbstractHttpRequestBuilder content(Object requestBody) {
			return this;
		}

	}

}
