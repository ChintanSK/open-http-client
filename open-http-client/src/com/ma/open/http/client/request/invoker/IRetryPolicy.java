package com.ma.open.http.client.request.invoker;

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.ma.open.http.client.request.HttpResponse;

public interface IRetryPolicy {

	int maxCount();

	Supplier<Long> nextInterval();

	boolean failOnException();

	Predicate<HttpResponse> shouldContinue();

}
