/**
 * SantanderDownloadTransactionsIT
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.web.santander.SantanderConfig;
import com.leonarduk.bookkeeper.web.santander.SantanderLogin;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

public class SantanderLoginIT {
	static final Logger LOGGER = Logger.getLogger(SantanderLoginIT.class);

	public static SantanderLogin getSantanderLogin(final File tempDir) throws IOException {
		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
		final String file = "bookkeeper-sit.properties";
		final SantanderConfig config = new SantanderConfig(file);

		final String santanderUrl = "https://business.santander.co.uk/LGSBBI_NS_ENS/BtoChannelDriver.ssobto?dse_operationName=LGSBBI_LOGON";
		config.setSantanderStartUrl(santanderUrl);
		final SantanderLogin santanderLogin = new SantanderLogin(webDriver, config);
		return santanderLogin;
	}

	@Test
	public final void testSantanderLogin() throws IOException {
		final SantanderLogin santanderLogin = SantanderLoginIT
		        .getSantanderLogin(FileUtils.createTempDir());
		santanderLogin.get();
		SantanderLoginIT.LOGGER.info("Complete");
	}

}
