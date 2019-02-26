/**
 * AmexDownloadTransactionsIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.amex;

import java.util.List;

import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.FileUtils;

public class AmexDownloadTransactionsIT {

	@Test
	public final void testDownloadTransactions() throws Exception {
		final AmexConfig config = new AmexConfig(SitConfig.getSitConfig());
		final AmexDownloadTransactions transactions = new AmexDownloadTransactions(config);

		final List<TransactionRecord> records = transactions.saveTransactions();
		final FileFormatter formatter = new CsvFormatter();
		final String outputFileName = "test.qif";
		formatter.format(records, outputFileName);
		System.out.println(FileUtils.getFileContents(outputFileName));
		config.getWebDriver().close();
	}

}
