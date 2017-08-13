package com.ma.open.http.client.request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.ma.open.http.client.request.invoker.IHttpRequestInvoker;
import com.ma.open.http.client.request.invoker.IRetryPolicy;
import com.ma.open.http.client.request.response.FutureHttpResponseHandler;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.IDelayedHttpResponseHandler;
import com.ma.open.http.client.request.sender.IHttpRequestSender;
import com.ma.open.http.client.request.ssl.SSLConfig;

public final class OpenHttpClient {

	public static InvocationBuilder newGetRequest(String url, IHttpRequestSender requestSender) {
		return new InvocationBuilder(AbstractHttpRequestBuilder.aGetRequest(url, requestSender));
	}

	public static InvocationBuilder newPostRequest(String url, IHttpRequestSender requestSender, Object requestBody) {
		return new InvocationBuilder(AbstractHttpRequestBuilder.aPostRequest(url, requestSender, requestBody));
	}

	public final static class InvocationBuilder {
		private AbstractHttpRequestBuilder wrappedBuilder;
		private IHttpRequestInvoker invoker = IHttpRequestInvoker.newInvoker();
		private IRetryPolicy retryPolicy;

		public InvocationBuilder(AbstractHttpRequestBuilder builder) {
			this.wrappedBuilder = builder;
		}

		public HttpResponse send(IDelayedHttpResponseHandler httpResponseHandler) {
			if (retryPolicy != null) {
				invoker = IHttpRequestInvoker.newRetriableInvoker(retryPolicy);
			}
			try {
				return invoker.invoke(wrappedBuilder.build(), new FutureHttpResponseHandler(httpResponseHandler));
			} finally {
				wrappedBuilder = null;
			}
		}

		public Future<HttpResponse> sendAsync(IDelayedHttpResponseHandler httpResponseHandler) {
			if (retryPolicy != null) {
				invoker = IHttpRequestInvoker.newRetriableInvoker(retryPolicy);
			}
			try {
				return invoker.invokeAsync(wrappedBuilder.build(), new FutureHttpResponseHandler(httpResponseHandler));
			} finally {
				wrappedBuilder = null;
			}
		}

		public InvocationBuilder retry(IRetryPolicy retryPolicy) {
			this.retryPolicy = retryPolicy;
			return this;
		}

		public InvocationBuilder header(String name, String... values) {
			wrappedBuilder = wrappedBuilder.header(name, values);
			return this;
		}

		public InvocationBuilder headers(Map<String, List<Object>> headers) {
			wrappedBuilder = wrappedBuilder.headers(headers);
			return this;
		}

		public InvocationBuilder finalHeaders(Map<String, List<Object>> finalHeaders) {
			wrappedBuilder = wrappedBuilder.finalHeaders(finalHeaders);
			return this;
		}

		public InvocationBuilder param(String name, String... values) {
			wrappedBuilder = wrappedBuilder.param(name, values);
			return this;
		}

		public InvocationBuilder contentType(String contentType) {
			wrappedBuilder = wrappedBuilder.contentType(contentType);
			return this;
		}

		public InvocationBuilder accept(String... acceptTypes) {
			wrappedBuilder = wrappedBuilder.accept(acceptTypes);
			return this;
		}

		public InvocationBuilder requestConfig(String key, String value) {
			wrappedBuilder = wrappedBuilder.requestConfig(key, value);
			return this;
		}

		public InvocationBuilder requestConfigs(Map<String, Object> requestConfigs) {
			wrappedBuilder = wrappedBuilder.requestConfigs(requestConfigs);
			return this;
		}

		public InvocationBuilder content(Object requestBody) {
			wrappedBuilder = wrappedBuilder.content(requestBody);
			return this;
		}

		public InvocationBuilder secure(SSLConfig sslConfig) {
			wrappedBuilder = wrappedBuilder.secure(sslConfig);
			return this;
		}

	}

}
