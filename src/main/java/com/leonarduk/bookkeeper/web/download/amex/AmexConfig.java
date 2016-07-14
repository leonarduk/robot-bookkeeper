/**
 * AmexConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.amex;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class AmexConfig extends AbstractWebConfig {
	static final String BOOKKEEPER_WEB_AMEX_ACCOUNTSUMMARYURL = "bookkeeper.web.amex.accountsummaryurl";

	public AmexConfig(final Config config) throws IOException {
		super(config);
	}

	public String getAccountSummaryUrl() {
		return this.getConfig().getProperty(AmexConfig.BOOKKEEPER_WEB_AMEX_ACCOUNTSUMMARYURL);
	}

	public String getBaseUrl() {
		return this.getConfig().getProperty("bookkeeper.web.amex.baseurl");
	}

	public String getPassword() {
		return this.getConfig().getProperty("bookkeeper.web.amex.password");
	}

	public String getUserName() {
		return this.getConfig().getProperty("bookkeeper.web.amex.username");
	}
}
