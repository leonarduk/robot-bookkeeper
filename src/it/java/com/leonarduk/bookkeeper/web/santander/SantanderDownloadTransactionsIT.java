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
import org.junit.Test;

import com.leonarduk.bookkeeper.file.CSVFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
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
		final List<TransactionRecord> records = santanderTransactions.downloadTransactions(tempDir);
		final FileFormatter formatter = new CSVFormatter();
		final String outputFileName = "test.qif";
		formatter.format(records, outputFileName);
		System.out.println(FileUtils.getFileContents(outputFileName));
	}
}
