/**
 * FreeAgentLoginIT
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.web.freeagent;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class FreeAgentLoginIT {

	@Test
	public final void testLoad() throws IOException {
		final File tempDir = FileUtils.createTempDir();
		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
		final Config config = new Config("bookkeeper-sit.properties");
		final FreeAgentConfig freeagentconfig = new FreeAgentConfig(config);
		final FreeAgentLogin freeAgentLogin = new FreeAgentLogin(webDriver, freeagentconfig);
		freeAgentLogin.get();
	}

}
