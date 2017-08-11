package com.ma.open.http.client.utils;

public final class NumberUtils {

	private static final String NUMBER_PATTERN = "\\d+";

	private NumberUtils() {
	}

	public static boolean isWholeNumber(String number) {
		return StringUtils.isNotBlank(number) && number.matches(NUMBER_PATTERN);
	}

	public static long longValue(String retryAfterValue) {
		if (!isWholeNumber(retryAfterValue))
			throw new NumberFormatException(retryAfterValue + " is not a whole number");

		return Long.parseLong(retryAfterValue);
	}

}
