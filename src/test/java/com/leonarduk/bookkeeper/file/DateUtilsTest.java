/**
 * DateUtilsTest
 *
 * @author ${author}
 * @since 14-Jul-2016
 */
package com.leonarduk.bookkeeper.file;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DateUtilsTest {

	private LocalDate expected;

	@Before
	public void setUp() throws Exception {
		this.expected = LocalDate.of(2016, 4, 1);
	}

	@Test
	public final void testNullString() {
		Assert.assertEquals(null, DateUtils.parse(""));
		Assert.assertEquals(null, DateUtils.parse(null));
	}

	@Test
	public final void testStringToDateddMMyyyySlashes() {
		final LocalDate actual = DateUtils.parse("01/04/2016");
		Assert.assertEquals(this.expected, actual);
	}

	@Ignore
	@Test
	public final void testStringToDateFull() {
		Assert.assertEquals(this.expected,
		        DateUtils.parse("Fri, 1 Apr 2016 00:00:00 +0000 (UTC)"));
	}

	@Test
	public final void testStringToDateyyyyMMdd() {
		Assert.assertEquals(this.expected, DateUtils.parse("20160401"));
	}

	@Test
	public final void testStringToDateyyyyMMddSlashes() {
		final LocalDate actual = DateUtils.parse("2016/04/01");
		Assert.assertEquals(this.expected, actual);
	}

	@Test
	public final void testStringToddMMyyyy() {
		Assert.assertEquals(this.expected, DateUtils.parse("01042016"));
	}

}
