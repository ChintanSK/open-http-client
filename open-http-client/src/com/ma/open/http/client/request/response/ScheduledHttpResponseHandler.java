package com.ma.open.http.client.request.response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledHttpResponseHandler implements IScheduledHttpResponseHandler {
	private static final int DELAYED_RETRIALS_MAX_ATTEMPTS = 5;

	private ScheduledExecutorService POOL = Executors.newScheduledThreadPool(1);

	private int scheduledAttempts = 0;

	private IDelayedHttpResponseHandler delayedHttpResponseHandler;

	public ScheduledHttpResponseHandler(IDelayedHttpResponseHandler delayedHttpResponseHandler) {
		this.delayedHttpResponseHandler = delayedHttpResponseHandler;
	}

	@Override
	public void handleScheduledHttpResponse(ScheduledFuture<HttpResponse> scheduledHttpResponse) {
		scheduledAttempts++;
		if (scheduledAttempts > DELAYED_RETRIALS_MAX_ATTEMPTS) {
			scheduledHttpResponse.cancel(true);
			return;
		}
		System.out.println("DEBUG: scheduled attempt " + scheduledAttempts);

		final long delay = scheduledHttpResponse.getDelay(TimeUnit.MILLISECONDS);
		if (delay > 0) {
			POOL.schedule(() -> {
				try {
					HttpResponse httpResponse = scheduledHttpResponse.get();
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

	@Override
	public IDelayedHttpResponseHandler getDelayedHttpResponseHandler() {
		return delayedHttpResponseHandler;
	}
}
