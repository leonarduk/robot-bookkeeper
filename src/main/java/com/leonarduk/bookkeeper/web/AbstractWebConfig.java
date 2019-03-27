/**
 * AbstractWebConfig
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class AbstractWebConfig {
	private static final Logger _logger = Logger.getLogger(AbstractWebConfig.class);
	private final Config config;
	private final File tempDir;

	private WebDriver downloadCapableBrowser;

	public AbstractWebConfig(final Config config2) throws IOException {
		this.config = config2;
		this.tempDir = FileUtils.createTempDir();
	}

	protected Config getConfig() {
		return this.config;
	}

	public File getDownloadDir() {
		return this.tempDir;
	}

	public String getMandatoryField(final String fieldName) {
		final String property = this.getConfig().getProperty(fieldName);
		if (property == null) {
			AbstractWebConfig._logger.error("Cannot find " + fieldName);
		}
		AbstractWebConfig._logger.info(fieldName + "=" + property);
		return property;
	}

	public WebDriver getWebDriver() throws IOException {
//		MutableCapabilities capability = new MutableCapabilities();
//		capability.setCapability("browser.download.folderList", 2);
//		capability.setCapability("browser.download.manager.showWhenStarting", false);
//		capability.setCapability("browser.download.dir", tempDir.getCanonicalPath());
//		capability.setCapability("browser.helperApps.alwaysAsk.force", false);
//		capability.setCapability("browser.helperApps.neverAsk.saveToDisk",
//				"application/x-csv,text/html,text/ofx,text/qif,application/ofx,qif File"
//						+ "application/x-msmoney,application/x-ofx,application/x-qif,application/qif,text/qif,text/csv,text/x-csv,"
//						+ "application/x-download,application/vnd.ms-excel," + "application/pdf,text/plain");
//		capability.setCapability("browser.helperApps.neverAsk.openFile",
//				"application/x-msmoney,application/x-csv,text/html,text/ofx,application/ofx,"
//						+ "application/octet-stream,application/x-ofx,"
//						+ "application/vnd.ms-excel,text/csv,text/x-csv,application/qif,text/qif,"
//						+ "application/x-download,application/vnd.ms-excel," + "application/pdf,text/plain");
//
//		return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);

		if (null == this.downloadCapableBrowser) {
			this.downloadCapableBrowser = SeleniumUtils.getDownloadCapableBrowser(this.tempDir);
		}
		return this.downloadCapableBrowser;
	}

}
