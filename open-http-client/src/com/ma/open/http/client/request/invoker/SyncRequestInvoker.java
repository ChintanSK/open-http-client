package com.ma.open.http.client.request.invoker;

import com.ma.open.http.client.request.AbstractHttpRequest;
import com.ma.open.http.client.request.HttpResponse;

public class SyncRequestInvoker {
	public HttpResponse invoke(AbstractHttpRequest httpRequest) {
		return httpRequest.send();
	}
}
