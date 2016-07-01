/**
 * AmexConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.amex;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class AmexConfig extends AbstractWebConfig {
	public AmexConfig(final Config config) throws IOException {
		super(config);
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
