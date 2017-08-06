package com.ma.open.http.client.request.invoker;

import java.util.function.Predicate;
import java.util.stream.IntStream;

import com.ma.open.http.client.request.HttpResponse;

public interface IRetryPolicy {

	int maxAttempts();

	IntStream intervals();

	boolean failOnException();

	Predicate<HttpResponse> shouldContinueRetrying();

}
