/**
 * DateUtilsTest
 *
 * @author ${author}
 * @since 14-Jul-2016
 */
package com.leonarduk.bookkeeper.file;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DateUtilsTest {

	private Date expected;

	@Before
	public void setUp() throws Exception {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(2016, Calendar.APRIL, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.expected = calendar.getTime();

	}

	@Test
	public final void testNullString() {
		Assert.assertEquals(null, DateUtils.stringToDate(""));
		Assert.assertEquals(null, DateUtils.stringToDate(null));
	}

	@Test
	public final void testStringToDateddMMyyyySlashes() {
		final Date actual = DateUtils.stringToDate("01/04/2016");
		Assert.assertEquals(this.expected.getTime(), actual.getTime());
	}

	@Ignore
	@Test
	public final void testStringToDateFull() {
		Assert.assertEquals(this.expected,
		        DateUtils.stringToDate("Fri, 1 Apr 2016 00:00:00 +0000 (UTC)"));
	}

	@Test
	public final void testStringToDateyyyyMMdd() {
		Assert.assertEquals(this.expected.getTime(), DateUtils.stringToDate("20160401").getTime());
	}

	@Test
	public final void testStringToDateyyyyMMddSlashes() {
		final Date actual = DateUtils.stringToDate("2016/04/01");
		Assert.assertEquals(this.expected.getTime(), actual.getTime());
	}

	@Test
	public final void testStringToddMMyyyy() {
		Assert.assertEquals(this.expected.getTime(), DateUtils.stringToDate("01042016").getTime());
	}

}
