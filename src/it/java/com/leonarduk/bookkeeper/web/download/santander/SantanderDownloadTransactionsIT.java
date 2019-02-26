/**
 * SantanderDownloadTransactionsIT
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.santander;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.FileUtils;

public class SantanderDownloadTransactionsIT {
	static final Logger LOGGER = Logger.getLogger(SantanderDownloadTransactionsIT.class);

	@Test
	public void testDownloadLatestStatement() throws Exception {
		final SantanderConfig config = new SantanderConfig(SitConfig.getSitConfig());

		final SantanderLogin santanderLogin = new SantanderLogin(config);
		final SantanderDownloadTransactions santanderTransactions = new SantanderDownloadTransactions(
		        santanderLogin);
		santanderTransactions.get();
		santanderTransactions.downloadLatestStatement();
		final File[] listFiles = config.getDownloadDir().listFiles();
		final File file = listFiles[0];
		Assert.assertTrue(file.exists());
		santanderLogin.getWebDriver().close();
	}

	@Test
	public void testDownloadTransactions() throws Exception {
		final SantanderConfig config = new SantanderConfig(SitConfig.getSitConfig());

		final SantanderLogin santanderLogin = new SantanderLogin(config);
		final SantanderDownloadTransactions santanderTransactions = new SantanderDownloadTransactions(
		        santanderLogin);
		santanderTransactions.get();
		final List<TransactionRecord> records = santanderTransactions.saveTransactions();
		final FileFormatter formatter = new CsvFormatter();
		final String outputFileName = "test.qif";
		formatter.format(records, outputFileName);
		System.out.println(FileUtils.getFileContents(outputFileName));
		santanderLogin.getWebDriver().close();
	}
}
