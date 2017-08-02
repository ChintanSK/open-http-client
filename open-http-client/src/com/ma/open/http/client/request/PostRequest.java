package com.ma.open.http.client.request;

import com.ma.open.http.client.request.sender.IHttpRequestSender;

public class PostRequest extends AbstractHttpRequest {

	private Object content;

	private PostRequest(Builder builder) {
		super(builder);
		this.content = builder.content;
	}

	@Override
	public HttpResponse send() {
		System.out.println(
				"PostRequest.send sending " + content.toString() + "to " + requestSender.getClass().getSimpleName());
		return requestSender.post(this);
	}

	public static class Builder extends AbstractHttpRequestBuilder {

		private Object content;

		public Builder(String url, IHttpRequestSender requestSender) {
			this.url = url;
			this.requestSender = requestSender;
		}

		@Override
		public AbstractHttpRequest build() {
			return new PostRequest(this);
		}

		public Builder content(Object content) {
			this.content = content;
			return this;
		}

	}

}
