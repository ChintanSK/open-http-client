package com.ma.open.http.client.utils;

import java.lang.ref.SoftReference;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class HttpDateUtils {

	private static final String RFC_1123_HTTP_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
	private static final String RFC_1036_HTTP_DATE_PATTERN = "EEEE, dd-MMM-yy HH:mm:ss zzz";
	private static final String ASC_TIME_HTTP_DATE_PATTERN = "EEE MMM d HH:mm:ss yyyy";

	private static final String[] HTTP_DATE_PATTERNS = new String[] { RFC_1123_HTTP_DATE_PATTERN,
			RFC_1036_HTTP_DATE_PATTERN, ASC_TIME_HTTP_DATE_PATTERN };

	private static SoftReference<Map<String, DateTimeFormatter>> httpDateFormatSoftReferences = new SoftReference<Map<String, DateTimeFormatter>>(
			new HashMap<>());

	static {
		final Map<String, DateTimeFormatter> mapOfFormats = httpDateFormatSoftReferences.get();
		final ZoneId gmtZoneId = ZoneId.of("GMT");

		for (String httpDatePattern : HTTP_DATE_PATTERNS) {
			mapOfFormats.put(httpDatePattern,
					DateTimeFormatter.ofPattern(httpDatePattern, Locale.US).withZone(gmtZoneId));
		}
	}

	private HttpDateUtils() {
	}

	public static Instant instant(String httpDate) {
		if (StringUtils.isBlank(httpDate))
			return null;

		if (httpDate.length() > 1 && httpDate.startsWith("'") && httpDate.endsWith("'")) {
			httpDate = httpDate.substring(1, httpDate.length() - 1);
		}

		for (String pattern : HTTP_DATE_PATTERNS) {
			DateTimeFormatter dateTimeFormatter = forPattern(pattern);
			try {
				return ZonedDateTime.parse(httpDate, dateTimeFormatter).toInstant();
			} catch (DateTimeParseException e) {
				// do nothing to try another http date pattern
			}
		}
		throw new DateTimeException("Parsing " + httpDate + " failed");
	}

	private static DateTimeFormatter forPattern(String pattern) {

		Map<String, DateTimeFormatter> map = httpDateFormatSoftReferences.get();
		if (map == null) {
			map = new HashMap<>();
			httpDateFormatSoftReferences = new SoftReference<Map<String, DateTimeFormatter>>(map);
		}
		DateTimeFormatter dateTimeFormatter = map.get(pattern);
		if (dateTimeFormatter == null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.US).withZone(ZoneId.of("GMT"));
			map.put(pattern, dateTimeFormatter);
		}
		return dateTimeFormatter;
	}

	public static void main(String[] args) {
		System.out.println(LocalDateTime.ofInstant(ZonedDateTime
				.parse("Fri, 11 Aug 2017 12:30:59 -0700", DateTimeFormatter.RFC_1123_DATE_TIME).toInstant(),
				ZoneId.systemDefault()));

		System.out
				.println(LocalDateTime.parse("Fri, 11 Aug 2017 12:30:59 -0700", DateTimeFormatter.RFC_1123_DATE_TIME));
	}
}
