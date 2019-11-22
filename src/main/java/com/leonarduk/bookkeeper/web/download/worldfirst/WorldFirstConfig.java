/**
 * worldfirstConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.worldfirst;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class WorldFirstConfig extends AbstractWebConfig {
	static final String BOOKKEEPER_WEB_WORLDFIRST_ACCOUNTSUMMARYURL = "bookkeeper.web.worldfirst.accountsummaryurl";

	public WorldFirstConfig(final Config config) throws IOException {
		super(config);
	}

	public String getAccountSummaryUrl() {
		return this.getConfig().getProperty(WorldFirstConfig.BOOKKEEPER_WEB_WORLDFIRST_ACCOUNTSUMMARYURL);
	}

	public String getBaseUrl() {
		return this.getConfig().getProperty("bookkeeper.web.worldfirst.baseurl");
	}

	public String getPassword() {
		return this.getConfig().getProperty("bookkeeper.web.worldfirst.password");
	}

	public String getUserName() {
		return this.getConfig().getProperty("bookkeeper.web.worldfirst.username");
	}

	public String getWorldFirstDownloadFile() {
		return this.getConfig().getProperty("bookkeeper.web.worldfirst.file");
	}

	public String getWorldFirstDownloadFolder() {
		return this.getConfig().getProperty("bookkeeper.web.worldfirst.folder");
	}

	public String getAnswer(String question) {
		return this.getConfig()
				.getProperty("bookkeeper.web.worldfirst.question." + question.trim().replaceAll(" ", "_"));
	}

	public String getAccountId() {
		return this.getConfig().getProperty("bookkeeper.web.worldfirst.account");
	}
}
