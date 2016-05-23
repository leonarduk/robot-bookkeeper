/**
 * All rights reserved. @Leonard UK Ltd.
 */
package uk.co.sleonard.accounts.web;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.core.FileUtils;
import com.leonarduk.web.SeleniumUtils;

import uk.co.sleonard.accounts.web.UploadToClearCheckbook.Setting;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class UploadToClearCheckbookSIT {

	private static boolean internetAvailable;

	@BeforeClass
	public static void setupStatic() {
		internetAvailable = SeleniumUtils.isInternetAvailable();
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
	 * Test upload to clear checkbook.
	 * 
	 * @throws Exception
	 */
	@Test

	public void testUploadToClearCheckbookNationwide() throws Exception {
		if (internetAvailable) {
			String userName = "virtualagent";
			String password = "eggsandbacon123";
			String account = "ZZZ  - Test Nationwide";
			String fileToUpload = "test";
			File tempDir = FileUtils.createTempDir();
			WebDriver driver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
			Setting setting = UploadToClearCheckbook.Setting.NATIONWIDE;
			UploadToClearCheckbook.uploadToClearCheckbook(userName, password, account, fileToUpload, driver, setting);
		}
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

		WebDriver driver = SeleniumUtils.getDownloadCapableBrowser(tempDir);

		String name = "clearcheckbook/";
		URL url = UploadToClearCheckbook.class.getClass().getResource(name);
		driver.get(url.getPath());
		// UploadToClearCheckbook.updateEstimate(account, currentValue,
		// userName,
		// password, driver, valueXpath, memo);
	}

	/**
	 * Test convert money string.
	 */
	@Test
	public final void testConvertMoneyString() {
		double convertMoneyString = UploadToClearCheckbook.convertMoneyString("£750,055");
		final int expected = 750055;
		assertEquals(expected, convertMoneyString, 0);
	}
}
