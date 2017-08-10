package com.ma.open.http.client.request.invoker;

import java.util.function.Predicate;

import com.ma.open.http.client.request.HttpResponse;

public abstract class AbstractRetryPolicy implements IRetryPolicy {

	static final int MAX_ATTEMPTS = 5;

	@Override
	public int maxAttempts() {
		return MAX_ATTEMPTS;
	}

	@Override
	public boolean failOnException() {
		return true;
	}

	@Override
	public Predicate<HttpResponse> shouldContinueRetrying() {
		return (r) -> {
			if (r == null)
				return true;
			if (r.getHeaders() != null && r.getHeaders().containsKey("Retry-After"))
				return false;
			return !((r.getStatus() == 200 && r.getBody() != null) || r.getStatus() == 204);
		};
	}

}
