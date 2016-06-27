/**
 * QifFileParserTest
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QifFileParserTest {

	@Test
	public final void testParse() throws IOException {
		final QifFileParser parser = new QifFileParser();
		final String fileName = QifFileParserTest.class.getClassLoader().getResource("example.qif")
		        .getFile();
		final List<TransactionRecord> record = parser.parse(fileName);
		Assert.assertEquals(-55.69, record.get(0).getAmount(), 0);
		Assert.assertEquals(-53.03, record.get(1).getAmount(), 0);

		Assert.assertEquals("DIRECT DEBIT PAYMENT TO MOBILE, 55.69",
		        record.get(0).getDescription());
		Assert.assertEquals(
		        "DIRECT DEBIT PAYMENT TO PAYPAL PAYMENT REF 4M522222XVZ5N, MANDATE NO 0002                 , 53.03",
		        record.get(1).getDescription());

		final Calendar cal1 = Calendar.getInstance();
		cal1.set(2016, Calendar.JUNE, 28, 0, 0, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		final Calendar cal2 = Calendar.getInstance();
		cal2.set(2016, Calendar.JUNE, 22, 0, 0, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(cal1.getTime(), record.get(0).getDate());
		Assert.assertEquals(cal2.getTime(), record.get(1).getDate());

	}

}
