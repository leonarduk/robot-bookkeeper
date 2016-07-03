/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.file.DateUtils;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class ClearCheckbookIT {

	private static boolean						internetAvailable;
	private ClearCheckbookTransactionUploader	clearCheckbook;

	@BeforeClass
	public static void setupStatic() {
		ClearCheckbookIT.internetAvailable = SeleniumUtils.isInternetAvailable();
	}

	@Before
	public void setup() throws IOException {
		this.clearCheckbook = new ClearCheckbookTransactionUploader(
		        new ClearCheckbookConfig(new Config("bookkeeper-sit.properties")));
	}

	/**
	 * Test convert money string.
	 */
	@Test
	public final void testConvertMoneyString() {
		final double convertMoneyString = this.clearCheckbook.convertMoneyString("£750,055");
		final int expected = 750055;
		Assert.assertEquals(expected, convertMoneyString, 0);
	}

	/**
	 * Test update estimate.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@Ignore
	public void testUpdateEstimate() throws Exception {
		try {
			final File tempDir = FileUtils.createTempDir();

			final WebDriver driver = SeleniumUtils.getDownloadCapableBrowser(tempDir);

			final String name = "clearcheckbook/";
			final URL url = ClearCheckbookTransactionUploader.class.getClass().getResource(name);
			driver.get(url.getPath());
		}
		catch (final Exception e) {
			Assert.fail("Exception caught");
		}
		// UploadToClearCheckbook.updateEstimate(account, currentValue,
		// userName,
		// password, driver, valueXpath, memo);
	}

	@Test
	public void testUploadToClearCheckbookCash() throws Exception {
		this.clearCheckbook.setAccount("Cash");
		this.uploadToClearcheckBook();

	}

	/**
	 * Test upload to clear checkbook.
	 *
	 * @throws Exception
	 */
	@Test
	public void testUploadToClearCheckbookChecking() throws Exception {
		this.clearCheckbook.setAccount("Checking");
		this.uploadToClearcheckBook();
	}

	public void uploadToClearcheckBook() throws IOException, Exception {
		final FileFormatter formatter = new QifFileFormatter(QifFileFormatter.CCB_FORMAT);
		final List<TransactionRecord> transactionRecords = new ArrayList<>();
		transactionRecords.add(new TransactionRecord(-12.23, "Payment",
		        DateUtils.stringToDate("2016/06/23"), "1", "Payee"));
		transactionRecords.add(new TransactionRecord(2.23, "Receipt",
		        DateUtils.stringToDate("2016/06/26"), "2", "Payee2"));
		final File tempDir = FileUtils.createTempDir();
		final String outputFileName = tempDir.getAbsolutePath() + "/output.qif";
		formatter.format(transactionRecords, outputFileName);

		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);

		final String results = this.clearCheckbook.uploadToClearCheckbook(outputFileName);
		System.out.println(results);
		webDriver.close();
	}

}
