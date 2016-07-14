/**
 * DateUtils
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.apache.http.ParseException;

/**
 * The Class DateUtils.
 */
public class DateUtils {
	static final String[] DATE_SEPARATORS = { "/", "-", ".", "," };

	/**
	 * Convert the String with date to Date Object.
	 *
	 * @author Elton - elton_12_nunes@hotmail.com
	 * @param text
	 *            the text
	 * @return Return the date.
	 * @since 11/06/2016
	 */
	@SuppressWarnings("unused")
	public static Date stringToDate(final String text) {
		StringBuilder pattern = null;

		if ((null == text) || text.equals("")) {
			return null;
		}

		final String yyyy = "yyyy";
		final String dd = "dd";
		final String mm = "MM";

		if (text.length() == 8) {
			try {
				// 20101229
				pattern = new StringBuilder().append(yyyy).append(mm).append(dd);
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
				final LocalDate localDate = LocalDate.parse(text, formatter);
				return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			catch (final DateTimeParseException e) {
				// Try other.
			}
			try {
				// 13122010
				pattern = new StringBuilder().append(dd).append(mm).append(yyyy);
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
				final LocalDate localDate = LocalDate.parse(text, formatter);
				return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			catch (final DateTimeParseException e) {
				throw new ParseException("Failed to parse date:" + text + e.getLocalizedMessage());
			}
		}
		else if (text.length() == 10) {
			for (final String separator : DateUtils.DATE_SEPARATORS) {
				if (text.contains(separator)) {
					try {
						// Ex.12/05/1994
						pattern = new StringBuilder().append(dd).append(separator).append(mm)
						        .append(separator).append(yyyy);
						final DateTimeFormatter formatter = DateTimeFormatter
						        .ofPattern(pattern.toString());
						final LocalDate localDate = LocalDate.parse(text, formatter);
						return Date
						        .from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					}
					catch (final DateTimeParseException e) {
						// ignore
					}
					try {
						// Ex.1994/12/05
						pattern = new StringBuilder().append(yyyy).append(separator).append(mm)
						        .append(separator).append(dd);
						final DateTimeFormatter formatter = DateTimeFormatter
						        .ofPattern(pattern.toString());
						final LocalDate localDate = LocalDate.parse(text, formatter);
						return Date
						        .from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					}
					catch (final DateTimeParseException e) {
						// ignore
					}
				}
			}
			throw new ParseException("Failed to parse date:" + text);
		}
		else {
			String dateText = text.trim();
			if (dateText.substring(0, 1).matches("[a-zA-Z]") && dateText.contains("(")) {
				// Sun, 18 Jan 2015 23:40:56 +0000 (UTC)
				// TRIM TO
				// 18 Jan 2015 23:40:56 +0000
				dateText = dateText.substring(4, dateText.indexOf("(")).trim();

				final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
				final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
				return Date.from(zonedDateTime.toInstant());
			}
			else if (dateText.substring(0, 1).matches("[a-zA-Z]")) {
				dateText = dateText.substring(4, dateText.length()).trim();
				// Sun, 18 Jan 2015 23:40:56 +0000
				// TRIM TO
				// 18 Jan 2015 23:40:56 +0000
				final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
				final ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateText, formatter);
				return Date.from(zonedDateTime.toInstant());
			}
			else {
				// 30 Jan 2015 23:37:13 GMT
				final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
				final LocalDate localDate = LocalDate.parse(dateText, formatter);
				return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
		}
	}

}
