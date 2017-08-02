package com.ma.open.http.client.request;

import com.ma.open.http.client.request.sender.IHttpRequestSender;

public class GetRequest extends AbstractHttpRequest {

	private GetRequest(Builder builder) {
		super(builder);
		this.requestSender = builder.requestSender;
	}

	@Override
	public HttpResponse send() {
		System.out.println("GetRequest.send fetching content from " + requestSender.getClass().getSimpleName());
		return requestSender.get(this);
	}

	public static class Builder extends AbstractHttpRequestBuilder {

		public Builder(String url, IHttpRequestSender requestSender) {
			this.url = url;
			this.requestSender = requestSender;
		}

		@Override
		public AbstractHttpRequest build() {
			return new GetRequest(this);
		}

	}

}
