package com.ma.open.http.client.request.response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FutureHttpResponseHandler {
	private static final int DELAYED_RETRIALS_MAX_ATTEMPTS = 5;

	private ScheduledExecutorService POOL = Executors.newScheduledThreadPool(1);

	private int scheduledAttempts = 0;

	private IDelayedHttpResponseHandler delayedHttpResponseHandler;

	public FutureHttpResponseHandler(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
		this.delayedHttpResponseHandler = delayedHttpResponseHandler;
	}

	public final void handleFutureHttpResponse(ScheduledFuture<HttpResponse> delayedHttpResponse) {
		scheduledAttempts++;
		if (scheduledAttempts > DELAYED_RETRIALS_MAX_ATTEMPTS) {
			delayedHttpResponse.cancel(true);
			return;
		}
		System.out.println("DEBUG: scheduled attempt " + scheduledAttempts);

		final long delay = delayedHttpResponse.getDelay(TimeUnit.MILLISECONDS);
		if (delay > 0) {
			POOL.schedule(() -> {
				try {
					HttpResponse httpResponse = delayedHttpResponse.get();
					if (!httpResponse.isScheduled()) {
						delayedHttpResponseHandler.handleDelayedHttpResponse(httpResponse);
					}
				} catch (InterruptedException | ExecutionException e) {
					if (Thread.interrupted()) {
						throw new RuntimeException(new InterruptedException());
					}
					throw new RuntimeException(e.getCause());
				}
			}, delay, TimeUnit.MILLISECONDS);
		}
	}
}
