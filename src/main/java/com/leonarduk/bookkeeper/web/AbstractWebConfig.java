/**
 * AbstractWebConfig
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;

import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class AbstractWebConfig {
	private final Config	config;
	private final File		tempDir;
	private final WebDriver	downloadCapableBrowser;

	public AbstractWebConfig(final Config config2) throws IOException {
		this.config = config2;
		this.tempDir = FileUtils.createTempDir();
		this.downloadCapableBrowser = SeleniumUtils.getDownloadCapableBrowser(this.tempDir);

	}

	protected Config getConfig() {
		return this.config;
	}

	public File getDownloadDir() {
		return this.tempDir;
	}

	public WebDriver getWebDriver() {
		return this.downloadCapableBrowser;
	}

}
