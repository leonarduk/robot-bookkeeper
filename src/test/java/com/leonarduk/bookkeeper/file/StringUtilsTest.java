/**
 * StringUtilsTest
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	/**
	 * Test convert money string.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testConvertMoneyString() throws IOException {
		final double convertMoneyString = StringUtils.convertMoneyString("£750,055");
		final int expected = 750055;
		Assert.assertEquals(expected, convertMoneyString, 0);
	}

}
