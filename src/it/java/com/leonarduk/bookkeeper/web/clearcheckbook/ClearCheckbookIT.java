/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.clearcheckbook;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.web.clearcheckbook.ClearCheckbook.Setting;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class ClearCheckbookIT {

	private static boolean internetAvailable;

	@BeforeClass
	public static void setupStatic() {
		ClearCheckbookIT.internetAvailable = SeleniumUtils.isInternetAvailable();
	}

	/**
	 * Test convert money string.
	 */
	@Test
	public final void testConvertMoneyString() {
		final double convertMoneyString = ClearCheckbook.convertMoneyString("£750,055");
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
			final URL url = ClearCheckbook.class.getClass().getResource(name);
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
	public void testUploadToClearCheckbookAmex() throws Exception {
		final String userName = "stevel56";
		final String password = "N0bigm0mas!";
		final String account = "CC - AMEX";
		final String fileToUpload = "/home/stephen/Downloads/ofx(10).qif";

		final WebDriver webDriver = SeleniumUtils
		        .getDownloadCapableBrowser("/home/stephen/Downloads");
		final String results = ClearCheckbook.uploadToClearCheckbook(userName, password, account,
		        fileToUpload, webDriver, Setting.AMEX);
		System.out.println(results);

	}

	/**
	 * Test upload to clear checkbook.
	 *
	 * @throws Exception
	 */
	@Test
	public void testUploadToClearCheckbookNationwide() throws Exception {
		try {
			if (ClearCheckbookIT.internetAvailable) {
				final String userName = "virtualagent";
				final String password = "eggsandbacon123";
				final String account = "ZZZ  - Test Nationwide";
				final String fileToUpload = "test";
				final File tempDir = FileUtils.createTempDir();
				final WebDriver driver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
				final Setting setting = ClearCheckbook.Setting.NATIONWIDE;
				ClearCheckbook.uploadToClearCheckbook(userName, password, account, fileToUpload,
				        driver, setting);
			}
		}
		catch (final Exception e) {
			Assert.fail("Exception caught");
		}
	}

}
