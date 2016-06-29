/**
 * SantanderDownloadTransactionsIT
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web.santander;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.FileUtils;

public class SantanderDownloadTransactionsIT {
	static final Logger LOGGER = Logger.getLogger(SantanderDownloadTransactionsIT.class);

	@Test
	public void testDownloadTransactions() throws Exception {
		final File tempDir = FileUtils.createTempDir();
		final SantanderLogin santanderLogin = SantanderLoginIT.getSantanderLogin(tempDir);
		final SantanderDownloadTransactions santanderTransactions = new SantanderDownloadTransactions(
		        santanderLogin);
		santanderTransactions.get();
		final String fileName = santanderTransactions.downloadTransactions(tempDir);
		final File file = new File(fileName);
		Assert.assertTrue(file.exists());
		final QifFileParser parser = new QifFileParser();
		final List<TransactionRecord> records = parser.parse(fileName);
		final String content = FileUtils.getFileContents(fileName);
		SantanderDownloadTransactionsIT.LOGGER.info(content);
	}
}
