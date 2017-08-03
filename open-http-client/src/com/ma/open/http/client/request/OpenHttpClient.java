package com.ma.open.http.client.request;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.ma.open.http.client.request.invoker.AsyncRequestInvoker;
import com.ma.open.http.client.request.invoker.SyncRequestInvoker;
import com.ma.open.http.client.request.sender.IHttpRequestSender;

public final class OpenHttpClient {

	private static final SyncRequestInvoker syncInvoker = new SyncRequestInvoker();
	private static final AsyncRequestInvoker asyncInvoker = new AsyncRequestInvoker();

	public static InvocationBuilder newGetRequest(String url, IHttpRequestSender requestSender) {
		return new InvocationBuilder(AbstractHttpRequestBuilder.aGetRequest(url, requestSender));
	}

	public static InvocationBuilder newPostRequest(String url, IHttpRequestSender requestSender, Object requestBody) {
		return new InvocationBuilder(AbstractHttpRequestBuilder.aPostRequest(url, requestSender, requestBody));
	}

	public static HttpResponse sendSync(AbstractHttpRequest httpRequest) {
		return syncInvoker.invoke(httpRequest);
	}

	public static void sendAsync(AbstractHttpRequest httpRequest, Consumer<HttpResponse> httpResponseConsumer) {
		asyncInvoker.invoke(httpRequest, httpResponseConsumer);
	}

	public final static class InvocationBuilder {
		private AbstractHttpRequestBuilder wrappedBuilder;
		private boolean isSynchronous = true;
		private Consumer<HttpResponse> httpResponseConsumer;

		public InvocationBuilder(AbstractHttpRequestBuilder builder) {
			this.wrappedBuilder = builder;
		}

		public InvocationBuilder async(Consumer<HttpResponse> httpResponseConsumer) {
			this.httpResponseConsumer = httpResponseConsumer;
			isSynchronous = false;
			return this;
		}

		public Optional<HttpResponse> getHttpResponse() {
			if (isSynchronous) {
				return Optional.of(OpenHttpClient.sendSync(wrappedBuilder.build()));
			} else {
				OpenHttpClient.sendAsync(wrappedBuilder.build(), httpResponseConsumer);
				return Optional.empty();
			}
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
