package com.ma.open.http.client.consumer.service;

import static com.ma.open.http.client.request.OpenHttpClient.newGetRequest;
import static com.ma.open.http.client.request.OpenHttpClient.newPostRequest;
import static com.ma.open.http.client.request.ssl.SSLConfig.newSSLConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.ma.open.http.client.request.invoker.RetryPolicies;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.IDelayedHttpResponseHandler;
import com.ma.open.http.client.request.sender.IHttpRequestSender;

public class RemoteServiceAdapter implements ILocalInterfaceToRemoteService {

	private final String baseUri = "https://someHost:port/context/";

	private Map<String, List<Object>> headers = new HashMap<String, List<Object>>() {
		private static final long serialVersionUID = 7612901587388155853L;

		{
			put("auth-token", Arrays.asList(new Object[] { "ae8fhjjgh45hjh4579bcjgnq" }));
		}
	};

	private IDelayedHttpResponseHandler delayedHttpResponseHandler = new IDelayedHttpResponseHandler() {

		@Override
		public void handleDelayedHttpResponse(HttpResponse delayedHttpResponse) {
			System.out.println("Debug: Http Response, status:" + delayedHttpResponse.getStatus() + " body:"
					+ delayedHttpResponse.getBody());
		}
	};

	private IHttpRequestSender httpRequestSender;

	public RemoteServiceAdapter(IHttpRequestSender httpRequestSender) {
		this.httpRequestSender = httpRequestSender;
	}

	@Override
	public Object get(String id) {
		return newGetRequest(baseUri + "objects/" + id, httpRequestSender).accept("application/json").headers(headers)
				.secure(newSSLConfig().getSSLConfig()).send(delayedHttpResponseHandler).getBody();
	}

	@Override
	public Supplier<Object> getAll() {
		Future<HttpResponse> future = newGetRequest(baseUri + "objects", httpRequestSender).accept("application/json")
				.headers(headers).param("list", "true").secure(newSSLConfig().getSSLConfig())
				.sendAsync(delayedHttpResponseHandler);

		return () -> {
			try {
				while (!future.isDone()) {
					System.out.println("Still waiting...");
				}
				return future.get().getBody();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		};
	}

	@Override
	public boolean create(Object newObject) {
		// totally understand that decoding the below large statement may be difficult
		return newPostRequest(baseUri + "objects/" + "newId", httpRequestSender, newObject).headers(headers)
				.contentType("text/plain").send(delayedHttpResponseHandler).getStatus() == 204;
	}

	@Override
	public boolean createWithRetryAttempts(Object newObject) {
		// totally understand that decoding the below large statement may be difficult
		return newPostRequest(baseUri + "objects/" + "newId", httpRequestSender, newObject).headers(headers)
				.contentType("text/plain").retry(RetryPolicies.FIBONACCI_DELAY).send(delayedHttpResponseHandler)
				.getStatus() == 204;
	}

	@Override
	public boolean remove(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
