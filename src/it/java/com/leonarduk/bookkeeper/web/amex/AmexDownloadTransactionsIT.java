/**
 * AmexDownloadTransactionsIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.amex;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class AmexDownloadTransactionsIT {

	@Test
	public final void testDownloadTransactions() throws Exception {
		final File downloadDir = FileUtils.createTempDir();
		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(downloadDir);

		final AmexConfig config = new AmexConfig(new Config("bookkeeper-sit.properties"));
		final AmexDownloadTransactions transactions = new AmexDownloadTransactions(webDriver,
		        config, downloadDir);

		final List<TransactionRecord> records = transactions.downloadTransactions();
		final FileFormatter formatter = new CsvFormatter();
		final String outputFileName = "test.qif";
		formatter.format(records, outputFileName);
		System.out.println(FileUtils.getFileContents(outputFileName));
		webDriver.close();
	}

}
