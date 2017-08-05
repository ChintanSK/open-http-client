package com.ma.open.http.client.request;

import com.ma.open.http.client.request.sender.IHttpRequestSender;

class PostRequest extends AbstractHttpRequest {

	private PostRequest(Builder builder) {
		super(builder);
	}

	@Override
	public HttpResponse send() {
		return requestSender.post(this);
	}

	static class Builder extends AbstractHttpRequestBuilder {

		public Builder(String url, IHttpRequestSender requestSender) {
			this.url = url;
			this.requestSender = requestSender;
		}

		@Override
		public AbstractHttpRequest build() {
			return new PostRequest(this);
		}

		@Override
		public Object getRequestBody() {
			return requestBody;
		}

	}

}
