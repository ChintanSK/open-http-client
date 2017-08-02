package com.ma.open.http.client.request.invoker;

import java.util.function.Consumer;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;

public class AsyncRequestInvoker {
	public void invoke(AbstractHttpRequest httpRequest, Consumer<HttpResponse> callback) {
		new Thread(() -> {
			callback.accept(httpRequest.send());
		}).start();
	}
}
