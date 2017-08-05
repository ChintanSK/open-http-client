package com.ma.open.http.client.request.invoker;

import java.util.function.Predicate;

import com.ma.open.http.client.request.HttpResponse;

public abstract class AbstractRetryPolicy implements IRetryPolicy {

	@Override
	public int maxCount() {
		return 3;
	}

	@Override
	public boolean failOnException() {
		return true;
	}

	@Override
	public Predicate<HttpResponse> shouldContinue() {
		return (r) -> {
			return (r.getStatus() == 200 && r.getBody() != null) || r.getStatus() == 204;
		};
	}

}
