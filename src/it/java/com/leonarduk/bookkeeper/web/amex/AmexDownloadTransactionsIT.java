/**
 * AmexDownloadTransactionsIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.amex;

import java.util.List;

import org.junit.Test;

import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class AmexDownloadTransactionsIT {

	@Test
	public final void testDownloadTransactions() throws Exception {
		final AmexConfig config = new AmexConfig(new Config("bookkeeper-sit.properties"));
		final AmexDownloadTransactions transactions = new AmexDownloadTransactions(config);

		final List<TransactionRecord> records = transactions.downloadTransactions();
		final FileFormatter formatter = new CsvFormatter();
		final String outputFileName = "test.qif";
		formatter.format(records, outputFileName);
		System.out.println(FileUtils.getFileContents(outputFileName));
		config.getWebDriver().close();
	}

}
