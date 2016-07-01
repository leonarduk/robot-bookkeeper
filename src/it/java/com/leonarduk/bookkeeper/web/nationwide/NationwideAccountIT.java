/**
 * NationwideAccountIT
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.nationwide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.nationwide.NationwideAccount.FileType;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class NationwideAccountIT {

	private NationwideAccount	nationwideAccount;
	private File				tempDir;
	private WebDriver			downloadCapableBrowser;

	@Before
	public void setUp() throws IOException {
		this.tempDir = FileUtils.createTempDir();
		this.downloadCapableBrowser = SeleniumUtils.getDownloadCapableBrowser(this.tempDir);
		final FileType type = FileType.OFX;
		this.nationwideAccount = (NationwideAccount) new NationwideAccount(
		        new NationwideLogin(this.downloadCapableBrowser,
		                new NationwideConfig(new Config("bookkeeper-sit.properties"))),
		        1, type).get();

	}

	@After
	public void tearDown() {
		this.downloadCapableBrowser.close();
	}

	@Test
	public final void testAccountName() {
		this.nationwideAccount.accountName();
	}

	@Test
	public final void testDownload() throws IOException {
		this.nationwideAccount.downloadTransactionsFile();
		final File[] files = this.tempDir.listFiles();
		if (files.length > 0) {
			final QifFileParser parser = new QifFileParser();
			final List<TransactionRecord> records = parser.parse(files[0].getAbsolutePath());
			final FileFormatter formatter = new CsvFormatter();
			final String outputFileName = "test.csv";
			formatter.format(records, outputFileName);
			System.out.println(FileUtils.getFileContents(outputFileName));
		}
	}

}
