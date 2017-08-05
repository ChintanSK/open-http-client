package com.ma.open.http.client.request.invoker;

import java.util.stream.IntStream;

public final class RetryPolicies {

	public static final IRetryPolicy FIXED_DELAY = new AbstractRetryPolicy() {

		@Override
		public IntStream intervals() {
			return IntStream.generate(() -> {
				return 2000;
			});
		}

	};

	private RetryPolicies() {
	}
}
