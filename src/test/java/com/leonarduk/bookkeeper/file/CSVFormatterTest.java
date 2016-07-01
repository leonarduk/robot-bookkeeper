/**
 * FreeAgentCSVFormatterTest
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.leonarduk.webscraper.core.FileUtils;

/**
 * The Class FreeAgentCSVFormatterTest.
 *
 * http://www.freeagent.com/support/kb/banking/file-format-for-bank-upload-csv/
 */
public class CSVFormatterTest {

	/**
	 * Test format.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public final void testFormat() throws IOException {
		final CsvFormatter formatter = new CsvFormatter();
		final List<TransactionRecord> transactionRecords = new ArrayList<>();
		transactionRecords.add(new TransactionRecord(-12.23, "Payment",
		        DateUtils.stringToDate("2016/06/23"), "1", "Payee"));
		transactionRecords.add(new TransactionRecord(2.23, "Receipt",
		        DateUtils.stringToDate("2016/06/26"), "2", "Payee2"));
		final String outputFileName = "output.csv";
		formatter.format(transactionRecords, outputFileName);

		final String actual = FileUtils.getFileContents(outputFileName);
		final String expected = "2016/06/23,-12.23,Payment\n" + "2016/06/26,2.23,Receipt";
		Assert.assertEquals(expected, actual);
	}

}
