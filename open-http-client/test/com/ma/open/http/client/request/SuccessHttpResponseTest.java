package com.ma.open.http.client.request;

import static com.ma.open.http.client.matchers.SuccessfulHttpResponseMatcher.isSuccessWithBody;
import static com.ma.open.http.client.matchers.SuccessfulHttpResponseMatcher.isSuccessWithNoBody;
import static com.ma.open.http.client.request.OpenHttpClient.newGetRequest;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ma.open.http.client.request.response.DelayedHttpResponseHandler;
import com.ma.open.http.client.request.response.HttpResponse;
import com.ma.open.http.client.request.response.HttpResponseHolder;
import com.ma.open.http.client.request.sender.SuccessResponder;

public class SuccessHttpResponseTest {

	@Test
	public void testGet() {
		HttpResponse httpResponse = newGetRequest("any/uri", successResponder()).accept("*/*")
				.send(delayedHttpResponseHandler());

		assertThat("Unexpected HttpResponse!", httpResponse,
				isSuccessWithBody("Hello Open Http Client. I know you are there with my client"));

	}

	@Test
	public void testPost() {
		HttpResponse httpResponse = OpenHttpClient.newPostRequest("any/uri", successResponder(), "PostRequestBody")
				.contentType("text/plain").send(delayedHttpResponseHandler());

		assertThat("Unexpected HttpResponse!", httpResponse, isSuccessWithNoBody());
	}

	private static SuccessResponder successResponder() {
		return new SuccessResponder();
	}

	private static DelayedHttpResponseHandler delayedHttpResponseHandler() {
		return new DelayedHttpResponseHandler(new HttpResponseHolder());
	}

}
