package com.ma.open.http.client.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import com.ma.open.http.client.request.response.HttpResponse;

public class SuccessfulHttpResponseMatcher extends BaseMatcher<HttpResponse> {

	private HttpResponse httpResponse;
	private int status;
	private Object expectedBody;
	private boolean bodyPresent;

	public SuccessfulHttpResponseMatcher(int status, boolean bodyPresent) {
		this.status = status;
		this.bodyPresent = bodyPresent;
	}

	public SuccessfulHttpResponseMatcher(int status, boolean bodyPresent, Object expectedBody) {
		this(status, bodyPresent);
		this.expectedBody = expectedBody;
	}

	@Override
	public boolean matches(Object subject) {
		httpResponse = (HttpResponse) subject;
		return testStatus(httpResponse.getStatus()) && testBody(httpResponse.getBody());
	}

	private boolean testStatus(int actualStatus) {
		return status == actualStatus;
	}

	private boolean testBody(Object actualBody) {
		if (bodyPresent) {
			if (expectedBody != null) {
				return expectedBody.equals(actualBody);
			} else {
				return actualBody != null;
			}
		} else {
			return actualBody == null;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("HttpResponse matcher with status " + status + ((!bodyPresent) ? " and no body"
				: ((expectedBody == null) ? " and any body" : " and body" + expectedBody)));

	}

	@Factory
	public static Matcher<HttpResponse> isSuccessWithNoBody() {
		return new SuccessfulHttpResponseMatcher(204, false);
	}

	@Factory
	public static Matcher<HttpResponse> isSuccessWithAnyBody() {
		return new SuccessfulHttpResponseMatcher(200, true);
	}

	@Factory
	public static Matcher<HttpResponse> isSuccessWithBody(Object body) {
		return new SuccessfulHttpResponseMatcher(200, true, body);
	}

}
