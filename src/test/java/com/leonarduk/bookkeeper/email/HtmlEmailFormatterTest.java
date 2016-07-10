/**
 * HtmlEmailFormatterTest
 *
 * @author ${author}
 * @since 06-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.bookkeeper.file.TransactionRecord;

public class HtmlEmailFormatterTest {

	private HtmlEmailFormatter formatter;

	@Before
	public void setUp() throws Exception {
		this.formatter = new HtmlEmailFormatter();
	}

	@Test
	public final void testAddNode() {
		Assert.assertEquals("<td>test</td>", this.formatter.addNode("test", "td"));
	}

	@Test
	public final void testEndMessageBody() {
		Assert.assertEquals("</body></html>", this.formatter.endMessageBody());
	}

	@Test
	public final void testFormat() {
		final List<TransactionRecord> records = new ArrayList<>();
		final Calendar cal = Calendar.getInstance();
		cal.set(2016, 01, 1, 12, 34, 56);
		final Date date2 = cal.getTime();
		records.add(new TransactionRecord(12.12, "Payment to me", date2, "12", "somebody"));
		Assert.assertEquals(
		        "<table><tr><th>Date</th><th>Amount</th><th>Description</th><th>Payee</th><th>CheckNumber</th></tr>"
		                + "<tr><td>2016/02/01</td><td>12.12</td><td>Payment to me</td><td>somebody</td><td>12</td></tr></table>",
		        this.formatter.format(records));
	}

	@Test
	public final void testStartMessageBody() {
		Assert.assertEquals("<html><body>", this.formatter.startMessageBody());
	}

}
