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
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.nationwide.NationwideAccount.FileType;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class NationwideAccountIT {

	private NationwideAccount	nationwideAccount;
	private NationwideConfig	nationwideConfig;
	private NationwideLogin		aLogin;

	@Before
	public void setUp() throws IOException {
		final FileType type = FileType.OFX;
		this.nationwideConfig = new NationwideConfig(new Config("bookkeeper-sit.properties"));
		this.aLogin = new NationwideLogin(this.nationwideConfig);
		this.nationwideAccount = (NationwideAccount) new NationwideAccount(this.aLogin, 1, type)
		        .get();

	}

	@After
	public void tearDown() {
		this.nationwideConfig.getWebDriver().close();
	}

	@Test
	public final void testAccountName() {
		this.nationwideAccount.accountName();
	}

	@Test
	public final void testDownloadTransactions() throws IOException {
		final List<TransactionRecord> records = this.nationwideAccount.downloadTransactions();
		final FileFormatter formatter = new CsvFormatter();
		final String outputFileName = "test.csv";
		formatter.format(records, outputFileName);
		System.out.println(FileUtils.getFileContents(outputFileName));
	}

	@Test
	public final void testDownloadTransactionsFile() throws IOException {
		this.nationwideAccount.downloadTransactionsFile();
		final File[] files = this.nationwideConfig.getDownloadDir().listFiles();
		if (files.length > 0) {
			final QifFileParser parser = new QifFileParser();
			final List<TransactionRecord> records = parser.parse(files[0].getAbsolutePath());
			final FileFormatter formatter = new CsvFormatter();
			final String outputFileName = "test.csv";
			formatter.format(records, outputFileName);
			System.out.println(FileUtils.getFileContents(outputFileName));
		}
	}

	@Test
	public void testLogin() throws Exception {
		this.aLogin.get();
	}

}
