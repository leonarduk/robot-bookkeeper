/**
 * All rights reserved. @Leonard UK Ltd.
 */
package uk.co.sleonard.accounts.web;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

import uk.co.sleonard.accounts.web.UploadToClearCheckbook.Setting;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class UploadToClearCheckbookSIT {

	private static boolean internetAvailable;

	@BeforeClass
	public static void setupStatic() {
		UploadToClearCheckbookSIT.internetAvailable = SeleniumUtils.isInternetAvailable();
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test convert money string.
	 */
	@Test
	public final void testConvertMoneyString() {
		final double convertMoneyString = UploadToClearCheckbook.convertMoneyString("£750,055");
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
		final File tempDir = FileUtils.createTempDir();

		final WebDriver driver = SeleniumUtils.getDownloadCapableBrowser(tempDir);

		final String name = "clearcheckbook/";
		final URL url = UploadToClearCheckbook.class.getClass().getResource(name);
		driver.get(url.getPath());
		// UploadToClearCheckbook.updateEstimate(account, currentValue,
		// userName,
		// password, driver, valueXpath, memo);
	}

	/**
	 * Test upload to clear checkbook.
	 *
	 * @throws Exception
	 */
	@Test

	public void testUploadToClearCheckbookNationwide() throws Exception {
		if (UploadToClearCheckbookSIT.internetAvailable) {
			final String userName = "virtualagent";
			final String password = "eggsandbacon123";
			final String account = "ZZZ  - Test Nationwide";
			final String fileToUpload = "test";
			final File tempDir = FileUtils.createTempDir();
			final WebDriver driver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
			final Setting setting = UploadToClearCheckbook.Setting.NATIONWIDE;
			UploadToClearCheckbook.uploadToClearCheckbook(userName, password, account, fileToUpload,
			        driver, setting);
		}
	}
}
