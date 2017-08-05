package com.ma.open.http.client.request.invoker;

import java.util.function.Supplier;

public final class RetryPolicies {

	public static final IRetryPolicy FIXED_DELAY = new AbstractRetryPolicy() {

		@Override
		public Supplier<Long> nextInterval() {
			return () -> {
				return 2000L;
			};
		}

	};

	private RetryPolicies() {
	}
}
