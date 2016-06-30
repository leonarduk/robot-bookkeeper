/**
 * FreeAgentLoginIT
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.web.freeagent;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class FreeAgentLoginIT {

	private FreeAgentLogin login;

	public static FreeAgentLogin getFreeAgentLogin() throws IOException {
		final File tempDir = FileUtils.createTempDir();
		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
		final Config config = new Config("bookkeeper-sit.properties");
		final FreeAgentConfig freeagentconfig = new FreeAgentConfig(config);
		final FreeAgentLogin freeAgentLogin = new FreeAgentLogin(webDriver, freeagentconfig);
		return freeAgentLogin;
	}

	@Before
	public void setUp() throws Exception {
		this.login = FreeAgentLoginIT.getFreeAgentLogin();
	}

	@After
	public void tearDown() throws Exception {
		this.login.getWebDriver().close();
	}

	@Test
	public final void testLoad() {
		this.login.get();

		final String profits = this.login.getProfits();
		System.out.println(profits);
		this.login.getWebDriver().close();
	}

}
