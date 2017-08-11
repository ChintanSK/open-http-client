package com.ma.open.http.client.utils;

public final class StringUtils {
	private StringUtils() {
	}

	public static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

}
