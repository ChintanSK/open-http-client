package com.ma.open.http.client.external.service;

import static com.ma.open.http.client.request.AbstractHttpRequestBuilder.aGetRequest;
import static com.ma.open.http.client.request.AbstractHttpRequestBuilder.aPostRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.SSLConfig;
import com.ma.open.http.client.request.invoker.AsyncRequestInvoker;
import com.ma.open.http.client.request.invoker.SyncRequestInvoker;
import com.ma.open.http.client.request.sender.impl.ApacheHttpRequestSender;
import com.ma.open.http.client.request.sender.impl.JerseyHttpRequestSender;

public class RemoteServiceAdapter implements ILocalInterfaceToRemoteService {

	private final String baseUri = "https://someHost:port/context/";

	private Map<String, List<Object>> headers = new HashMap<String, List<Object>>() {
		private static final long serialVersionUID = 7612901587388155853L;

		{
			put("auth-token", Arrays.asList(new Object[] { "ae8fhjjgh45hjh4579bcjgnq" }));
		}
	};

	private SyncRequestInvoker syncInvoker;
	private AsyncRequestInvoker asyncInvoker;

	public RemoteServiceAdapter(SyncRequestInvoker syncInvoker, AsyncRequestInvoker asyncInvoker) {
		this.syncInvoker = syncInvoker;
		this.asyncInvoker = asyncInvoker;
	}

	@Override
	public Object get(String id) {
		AbstractHttpRequest httpRequest = aGetRequest(baseUri + "objects/" + id, new JerseyHttpRequestSender())
				.accept("application/json").headers(headers).secure(new SSLConfig()).build();

		return syncInvoker.invoke(httpRequest);
	}

	@Override
	public void getAll() {
		AbstractHttpRequest httpRequest = aGetRequest(baseUri + "objects", new ApacheHttpRequestSender())
				.accept("application/json").headers(headers).param("list", "true").secure(new SSLConfig()).build();

		asyncInvoker.invoke(httpRequest, (httpResponse) -> {
			// Fire an event with the Http Response
		});
	}

	@Override
	public boolean create(Object newObject) {
		AbstractHttpRequest httpRequest = aPostRequest(baseUri + "newId", new JerseyHttpRequestSender(),
				"Hello there, I am from open-http-client").headers(headers).contentType("text/plain").build();

		return syncInvoker.invoke(httpRequest).getStatus() == 204;
	}

	@Override
	public boolean remove(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
