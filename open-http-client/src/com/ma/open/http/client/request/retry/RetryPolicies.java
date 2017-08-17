package com.ma.open.http.client.request.retry;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.ma.open.http.client.request.response.HttpResponse;

public final class RetryPolicies {

	private RetryPolicies() {
	}

	private static final int MAX_ATTEMPTS = 5;
	private static final long MAX_TIMEOUT = 90000L;

	private static Predicate<HttpResponse> DISCONTINUE_ON_SUCCESS_PREDICATE = new Predicate<HttpResponse>() {

		@Override
		public boolean test(HttpResponse r) {
			return !(r != null && (r.getStatus() == 200 && r.getBody() != null) || r.getStatus() == 204);
		}
	};

	private static Supplier<IntStream> FIXED_DELAY(final int size, final int delay) {
		return new Supplier<IntStream>() {

			@Override
			public IntStream get() {
				return IntStream.generate(() -> {
					return delay;
				}).limit(size);
			}
		};
	}

	private static Supplier<IntStream> INCREMENTAL_DELAY(final int size) {
		return new Supplier<IntStream>() {

			@Override
			public IntStream get() {
				return IntStream.iterate(1000, (i) -> {
					return i + 1000;
				}).limit(size);
			}
		};
	}

	private static Supplier<IntStream> EXPONENTIAL_BACKOFF_DELAY(final int size) {
		return new Supplier<IntStream>() {

			@Override
			public IntStream get() {
				final Stream<int[]> fibonacciStream = Stream.iterate(new int[] { 1000, 2000 }, (previousTuple) -> {
					int[] nextTuple = new int[2];
					nextTuple[0] = previousTuple[0] + previousTuple[1];
					nextTuple[1] = previousTuple[1] + nextTuple[0];
					return nextTuple;
				}).limit(size);
				return fibonacciStream.flatMapToInt(Arrays::stream).limit(size);
			}
		};
	}

	public static IRetryPolicy fixedDelayPolicy(int delay) {
		return new RetryPolicyBuilder().retryDelaySupplier(FIXED_DELAY(MAX_ATTEMPTS, delay))
				.continuePredicate(DISCONTINUE_ON_SUCCESS_PREDICATE).fatalException(IOException.class).build();
	}

	public static IRetryPolicy incrementalDelayPolicy() {
		return new RetryPolicyBuilder().retryDelaySupplier(INCREMENTAL_DELAY(MAX_ATTEMPTS))
				.continuePredicate(DISCONTINUE_ON_SUCCESS_PREDICATE).fatalException(IOException.class).build();
	}

	public static IRetryPolicy exponentialBackOffDelayPolicy() {
		return new RetryPolicyBuilder().retryDelaySupplier(EXPONENTIAL_BACKOFF_DELAY(MAX_ATTEMPTS))
				.continuePredicate(DISCONTINUE_ON_SUCCESS_PREDICATE).fatalException(IOException.class).build();
	}

	public static RetryPolicyBuilder newRetryPolicy() {
		return new RetryPolicyBuilder();
	}

	public static class RetryPolicyBuilder {
		private int maxAttempts = MAX_ATTEMPTS;
		private long timeOut = MAX_TIMEOUT;
		private Supplier<IntStream> retryDelaySupplier;
		private List<Class<? extends Exception>> fatalExceptions = new ArrayList<>();
		private Predicate<HttpResponse> continueRetryingPredicate;

		private RetryPolicyBuilder() {
		}

		public RetryPolicyBuilder withMaxAttempts(int maxAttempts) {
			this.maxAttempts = maxAttempts;
			return this;
		}

		public RetryPolicyBuilder withMaxTimeout(long maxTimeOut) {
			this.timeOut = maxTimeOut;
			return this;
		}

		public RetryPolicyBuilder retryDelaySupplier(Supplier<IntStream> retryDelaySupplier) {
			this.retryDelaySupplier = retryDelaySupplier;
			return this;
		}

		public RetryPolicyBuilder continuePredicate(Predicate<HttpResponse> continuePredicate) {
			this.continueRetryingPredicate = continuePredicate;
			return this;
		}

		public RetryPolicyBuilder fatalExceptions(List<Class<? extends Exception>> exceptions) {
			fatalExceptions = exceptions;
			return this;
		}

		public RetryPolicyBuilder fatalException(Class<? extends Exception> exception) {
			if (fatalExceptions == null) {
				fatalExceptions = new ArrayList<>();
			}
			fatalExceptions.add(exception);
			return this;
		}

		public IRetryPolicy build() {
			return new IRetryPolicy() {

				@Override
				public int maxAttempts() {
					return maxAttempts;
				}

				@Override
				public Duration maxTurnAroundTime() {
					return Duration.ofMillis(timeOut);
				}

				@Override
				public IntStream intervals() {
					return retryDelaySupplier.get();
				}

				@Override
				public boolean failOnException(Exception exception) {
					return null != fatalExceptions.parallelStream().filter((e) -> {
						return e.isAssignableFrom(exception.getClass());
					}).findFirst();
				}

				@Override
				public Predicate<HttpResponse> shouldContinueRetrying() {
					return continueRetryingPredicate;
				}

			};
		}

	}
}
