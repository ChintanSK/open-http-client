package com.ma.open.http.client.request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.ma.open.http.client.request.invoker.DefaultRetryAfterHandler;
import com.ma.open.http.client.request.invoker.HttpRequestInvokerBuilder;
import com.ma.open.http.client.request.invoker.IHttpRequestInvoker;
import com.ma.open.http.client.request.invoker.IRetryAfterHandler;
import com.ma.open.http.client.request.invoker.IRetryPolicy;
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
		private boolean retryAfterDisabled;
		private IRetryAfterHandler retryAfterHandler;

		public InvocationBuilder(AbstractHttpRequestBuilder builder) {
			this.wrappedBuilder = builder;
		}

		public HttpResponse send(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
			buildInvoker(delayedHttpResponseHandler);
			try {
				return invoker.invoke(wrappedBuilder.build());
			} finally {
				wrappedBuilder = null;
			}
		}

		public Future<HttpResponse> sendAsync(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
			buildInvoker(delayedHttpResponseHandler);
			try {
				return invoker.invokeAsync(wrappedBuilder.build());
			} finally {
				wrappedBuilder = null;
			}
		}

		private void buildInvoker(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
			HttpRequestInvokerBuilder invokerBuilder = HttpRequestInvokerBuilder.newBuilder(delayedHttpResponseHandler);
			if (retryPolicy != null) {
				invokerBuilder.withRetryPolicy(retryPolicy);
			}
			if (retryAfterDisabled) {
				invokerBuilder.disableRetryAfter();
			} else if (retryAfterHandler == null) {
				invokerBuilder.withRetryAfterHandler(retryAfterHandler);
			} else {
				invokerBuilder.withRetryAfterHandler(new DefaultRetryAfterHandler(delayedHttpResponseHandler));
			}
			invoker = invokerBuilder.build();
		}

		public InvocationBuilder disableRetryAfter() {
			retryAfterDisabled = true;
			return this;
		}

		public InvocationBuilder retryAfterHandler(IRetryAfterHandler retryAfterHandler) {
			this.retryAfterHandler = retryAfterHandler;
			return this;
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
