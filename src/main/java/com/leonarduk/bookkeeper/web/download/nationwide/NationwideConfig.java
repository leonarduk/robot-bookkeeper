/**
 * NationwideConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.nationwide;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class NationwideConfig extends AbstractWebConfig {

	public NationwideConfig(final Config config) throws IOException {
		super(config);
	}

	public String getAccountListUrl() {
		return this.getConfig().getProperty("bookkeeper.web.nationwide.accountlist.url");
	}

	public String getCustomerNumber() {
		return this.getConfig().getProperty("bookkeeper.web.nationwide.customerNumber");
	}

	public String getFullStatementUrl(final int aAccountId) {
		return this.getConfig().getProperty("bookkeeper.web.nationwide.statement.url") + aAccountId;
	}

	public String getLoginUrl() {
		return this.getConfig().getProperty("bookkeeper.web.nationwide.login.url");
	}

	public String getMemorableWord() {
		return this.getConfig().getProperty("bookkeeper.web.nationwide.memorableword");
	}

	public String getPassword() {
		return this.getConfig().getProperty("bookkeeper.web.nationwide.password");
	}
}
