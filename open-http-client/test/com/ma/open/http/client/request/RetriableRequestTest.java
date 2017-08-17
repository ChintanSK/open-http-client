package com.ma.open.http.client.request;

import static com.ma.open.http.client.matchers.SuccessfulHttpResponseMatcher.isSuccessWithBody;
import static com.ma.open.http.client.matchers.SuccessfulHttpResponseMatcher.isSuccessWithNoBody;
import static com.ma.open.http.client.request.OpenHttpClient.newGetRequest;
import static com.ma.open.http.client.request.OpenHttpClient.newPostRequest;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ma.open.http.client.request.response.DelayedHttpResponseHandler;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.HttpResponseHolder;
import com.ma.open.http.client.request.retry.IRetryPolicy;
import com.ma.open.http.client.request.retry.RetryPolicies;
import com.ma.open.http.client.request.sender.ServerErrorResponder;

public class RetriableRequestTest {

	private IRetryPolicy incrementalPolicy = RetryPolicies.incrementalDelayPolicy();

	@Test
	public void testGetWithRetryAfterResponse() {
		HttpResponse httpResponse = newGetRequest("any/url", new ServerErrorResponder(2)).accept("*/*")
				.retry(incrementalPolicy).send(new DelayedHttpResponseHandler(new HttpResponseHolder()));

		assertThat("Unexpected HttpResponse!", httpResponse,
				isSuccessWithBody("You made it here after 2 server errors"));
	}

	@Test
	public void testPostWithRetryAfterResponse() {
		HttpResponse httpResponse = newPostRequest("any/url", new ServerErrorResponder(2), "attempting post")
				.contentType("application/json").retry(incrementalPolicy)
				.send(new DelayedHttpResponseHandler(new HttpResponseHolder()));

		assertThat("Unexpected HttpResponse!", httpResponse, isSuccessWithNoBody());
	}

}
