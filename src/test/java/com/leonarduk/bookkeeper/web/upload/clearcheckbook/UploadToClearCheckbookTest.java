/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class UploadToClearCheckbookTest {

	/**
	 * Test convert money string.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testConvertMoneyString() throws IOException {
		final ClearCheckbookTransactionUploader clearCheckbook = new ClearCheckbookTransactionUploader(
		        Mockito.mock(ClearCheckbookConfig.class));
		final double convertMoneyString = clearCheckbook.convertMoneyString("£750,055");
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
			// UploadToClearCheckbook.updateEstimate(account, currentValue,
			// userName,
			// password, driver, valueXpath, memo);
		}
		catch (final Exception e) {
			Assert.fail("Exception caught");
		}
	}

}
