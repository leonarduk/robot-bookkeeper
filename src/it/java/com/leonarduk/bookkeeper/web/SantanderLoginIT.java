/**
 * SantanderDownloadTransactionsIT
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.web.santander.SantanderConfig;
import com.leonarduk.bookkeeper.web.santander.SantanderLogin;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

public class SantanderLoginIT {

	@Test(expected = NoSuchElementException.class)
	public final void testSantander() throws IOException {
		final File tempDir = FileUtils.createTempDir();

		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);
		final Properties properties = new Properties();
		final SantanderConfig config = new SantanderConfig(properties);

		final String santanderUrl = "https://business.santander.co.uk/LGSBBI_NS_ENS/BtoChannelDriver.ssobto?dse_operationName=LGSBBI_LOGON";
		config.setSantanderStartUrl(santanderUrl);
		final SantanderLogin santanderLogin = new SantanderLogin(webDriver, config);
		santanderLogin.get();
	}

}
