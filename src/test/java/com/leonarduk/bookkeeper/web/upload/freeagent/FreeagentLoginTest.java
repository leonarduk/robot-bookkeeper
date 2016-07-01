/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.freeagent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * The Class FreeagentLoginTest.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 4 Mar 2015
 */
public class FreeagentLoginTest {

	/** The login. */
	private FreeAgentUploadTransactions login;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public final void setUp() throws Exception {
		final FreeAgentConfig config = Mockito.mock(FreeAgentConfig.class);
		this.login = new FreeAgentUploadTransactions(new FreeAgentLogin(config));
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
		// this.login.getWebDriver().close();
	}

	/**
	 * Test is loaded.
	 */
	@Test
	@Ignore
	// get Jenkins working for now
	public final void testIsLoaded() {
		try {
			this.login.get();
			this.login.uploadTransactions("/tmp/heaDd2/Statements09015613132580.qif");
		}
		catch (final Exception e) {
			Assert.fail("Caught exception: " + e.getLocalizedMessage());
		}
	}

	// @Test
	/**
	 * Test santander login.
	 */
	public final void testSantanderLogin() {
		Assert.assertNotNull(this.login);
	}

}
