/**
 * StringUtils
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.file;

import org.apache.commons.lang3.StringUtils;

public class StringConversionUtils {
	/**
	 * Convert money string.
	 *
	 * @param amount
	 *            the amount
	 * @return the double
	 */
	public static double convertMoneyString(final String amount) {
		if (StringUtils.isEmpty(amount)) {
			return 0.0;
		}
		return Double.valueOf(amount.replaceAll("£", "").replaceAll(",", "").replaceAll("�", ""))
		        .doubleValue();
	}
}
