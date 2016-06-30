/**
 * NationwideConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.nationwide;

import com.leonarduk.webscraper.core.config.Config;

public class NationwideConfig {
	private final Config config;

	public NationwideConfig(final Config config) {
		this.config = config;
	}

	public String getCustomerNumber() {
		return this.config.getProperty("bookkeeper.web.nationwide.customerNumber");
	}

	public String getMemorableWord() {
		return this.config.getProperty("bookkeeper.web.nationwide.memorableword");
	}

	public String getPassword() {
		return this.config.getProperty("bookkeeper.web.nationwide.password");
	}
}
