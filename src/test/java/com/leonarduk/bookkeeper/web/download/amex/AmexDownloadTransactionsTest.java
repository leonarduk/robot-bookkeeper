/**
 * AmexDownloadTransactionsTest
 *
 * @author ${author}
 * @since 14-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.amex;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.leonarduk.webscraper.core.config.Config;

public class AmexDownloadTransactionsTest {

	private AmexDownloadTransactions	amex;
	private AmexConfig					config;

	@Before
	public void setUp() throws Exception {
		final Config config2 = new Config("bookkeeper.properties");
		this.config = new AmexConfig(config2);
		final URL htmlUrl = this.getClass().getClassLoader()
		        .getResource(this.config.getAccountSummaryUrl());
		config2.setProperty(AmexConfig.BOOKKEEPER_WEB_AMEX_ACCOUNTSUMMARYURL,
		        "file://" + htmlUrl.getFile());
		this.amex = new AmexDownloadTransactions(this.config);
	}

	@After
	public void tearDown() throws Exception {
		this.config.getWebDriver().close();
	}

	@Ignore   // Travis can't handle this
	@Test
	public final void testGetCurrentValue() throws IOException {
		final double actual = this.amex.getCurrentValue();
		final double expected = 949.14;
		Assert.assertEquals(expected, actual, 0);
	}

	@Ignore   // Travis can't handle this
	@Test
	public final void testGetDescription() {
		Assert.assertEquals("Account adjustment to reconcile with Amex site",
		        this.amex.getDescription());
	}

}
