/**
 * StringUtils
 * 
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.file;

public class StringUtils {
	/**
	 * Convert money string.
	 *
	 * @param amount
	 *            the amount
	 * @return the double
	 */
	public static double convertMoneyString(final String amount) {
		return Double.valueOf(amount.replaceAll("£", "").replaceAll(",", "")).doubleValue();
	}
}
