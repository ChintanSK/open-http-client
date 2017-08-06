package com.ma.open.http.client.request.invoker;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class RetryPolicies {

	public static final IRetryPolicy TWO_SECOND_DELAY = new AbstractRetryPolicy() {

		@Override
		public IntStream intervals() {
			return IntStream.generate(() -> {
				return 2000;
			}).limit(MAX_ATTEMPTS);
		}

	};

	public static final IRetryPolicy FIBONACCI_DELAY = new AbstractRetryPolicy() {

		@Override
		public IntStream intervals() {
			final Stream<int[]> iterate = Stream.iterate(new int[] { 1000, 2000 }, (previousTuple) -> {
				int[] nextTuple = new int[2];
				nextTuple[0] = previousTuple[0] + previousTuple[1];
				nextTuple[1] = previousTuple[1] + nextTuple[0];
				return nextTuple;
			}).limit(MAX_ATTEMPTS);
			return iterate.flatMapToInt(Arrays::stream).limit(MAX_ATTEMPTS);
		}
	};

	public static final IRetryPolicy INCREMENTAL_DELAY = new AbstractRetryPolicy() {

		@Override
		public IntStream intervals() {
			return IntStream.iterate(1000, (i) -> {
				return i + 1000;
			}).limit(MAX_ATTEMPTS);
		}
	};

	private RetryPolicies() {
	}
}
