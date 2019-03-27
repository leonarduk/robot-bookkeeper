/**
 * QifFileParserTest
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QifFileParserTest {

	@Test
	public final void testParse() throws IOException {
		final QifFileParser parser = new QifFileParser();
		TransactionRecordFilter filter = (record) -> true;
		final String fileName = QifFileParserTest.class.getClassLoader().getResource("example.qif")
		        .getFile();
		final List<TransactionRecord> record = parser.parse(fileName, filter);
		Assert.assertEquals(-55.69, record.get(0).getAmount(), 0);
		Assert.assertEquals(-53.03, record.get(1).getAmount(), 0);

		Assert.assertEquals("DIRECT DEBIT PAYMENT TO MOBILE, 55.69", record.get(0).getPayee());
		Assert.assertEquals(
		        "DIRECT DEBIT PAYMENT TO PAYPAL PAYMENT REF 4M522222XVZ5N, MANDATE NO 0002                 , 53.03",
		        record.get(1).getPayee());

		final LocalDate cal1 = LocalDate.of(2016, 6, 28);
		final LocalDate cal2 = LocalDate.of(2016, 6, 22);
		Assert.assertEquals(cal1, record.get(0).getDate());
		Assert.assertEquals(cal2, record.get(1).getDate());

	}

}
