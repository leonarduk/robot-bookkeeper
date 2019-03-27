/**
 * NationwideAccountIT
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.nationwide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.webscraper.core.FileUtils;

public class NationwideAccountIT {

	private NationwideAccount nationwideAccount;
	private NationwideConfig nationwideConfig;
	private NationwideLogin aLogin;

	@SuppressWarnings("resource")
	@Before
	public void setUp() throws IOException {
		this.nationwideConfig = new NationwideConfig(SitConfig.getSitConfig());
		this.aLogin = new NationwideLogin(this.nationwideConfig);
		this.nationwideAccount = (NationwideAccount) new NationwideAccount(this.aLogin, 1).get();

	}

	@After
	public void tearDown() throws Exception {
		this.nationwideAccount.close();
	}

	@Test
	public final void testAccountName() {
		this.nationwideAccount.accountName();
	}

	@Test
	public final void testDownloadTransactions() throws IOException {
		TransactionRecordFilter filter = (record) -> true;
		final List<TransactionRecord> records = this.nationwideAccount.saveTransactions(filter);
		Assert.assertFalse(records.isEmpty());
		final FileFormatter formatter = new CsvFormatter();
		final String outputFileName = "test.csv";
		formatter.format(records, outputFileName, filter);
		System.out.println(FileUtils.getFileContents(outputFileName));
	}

	@Test
	public final void testDownloadTransactionsFile() throws IOException {
		this.nationwideAccount.downloadTransactionsFile();
		final File[] files = this.nationwideConfig.getDownloadDir().listFiles();
		Assert.assertNotNull(files);
		Assert.assertTrue(files.length > 0);

		TransactionRecordFilter filter = (record) -> true;
		if (files.length > 0) {
			final QifFileParser parser = new QifFileParser();
			final List<TransactionRecord> records = parser.parse(files[0].getAbsolutePath(), filter);
			final FileFormatter formatter = new CsvFormatter();
			final String outputFileName = "test.csv";
			formatter.format(records, outputFileName, filter);
			System.out.println(FileUtils.getFileContents(outputFileName));
		}
	}

	@Test
	public void testLogin() throws Exception {
		this.aLogin.get();
	}

}
