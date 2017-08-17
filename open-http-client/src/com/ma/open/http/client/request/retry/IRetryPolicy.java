package com.ma.open.http.client.request.retry;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import com.ma.open.http.client.request.response.HttpResponse;

public interface IRetryPolicy {

	int maxAttempts();

	Duration maxTurnAroundTime();

	IntStream intervals();

	boolean failOnException(Exception e);

	Predicate<HttpResponse> shouldContinueRetrying();

}
