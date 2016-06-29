/**
 * AmexConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.amex;

import com.leonarduk.webscraper.core.config.Config;

public class AmexConfig {
	private final Config config;

	public AmexConfig(final Config config) {
		this.config = config;
	}

	public String getPassword() {
		return this.config.getProperty("bookkeeper.web.amex.password");
	}

	public String getUserName() {
		return this.config.getProperty("bookkeeper.web.amex.username");
	}
}
