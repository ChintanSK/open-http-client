package com.ma.open.http.client.request.invoker;

import java.time.Duration;
import java.util.function.Predicate;

import com.ma.open.http.client.request.HttpResponse;

public abstract class AbstractRetryPolicy implements IRetryPolicy {

	static final int MAX_ATTEMPTS = 5;
	static final long MAX_TURN_AROUND_TIME = 30000L;

	@Override
	public int maxAttempts() {
		return MAX_ATTEMPTS;
	}

	@Override
	public Duration maxTurnAroundTime() {
		return Duration.ofMillis(MAX_TURN_AROUND_TIME);
	}

	@Override
	public boolean failOnException(Exception e) {
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
