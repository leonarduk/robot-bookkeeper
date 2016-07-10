/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class UploadToClearCheckbookTest {

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
			final String name = "clearcheckbook/";
			final URL url = ClearCheckbookTransactionUploader.class.getClass().getResource(name);
			// UploadToClearCheckbook.updateEstimate(account, currentValue,
			// userName,
			// password, driver, valueXpath, memo);
		}
		catch (final Exception e) {
			Assert.fail("Exception caught");
		}
	}

}
