/**
 * ClearCheckbookConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.clearcheckbook;

import com.leonarduk.webscraper.core.config.Config;

public class ClearCheckbookConfig {
	private final Config config;

	public ClearCheckbookConfig(final Config config) {
		this.config = config;
	}

	public CharSequence getPassword() {
		return this.config.getProperty("bookkeeper.web.clearcheckbook.password");
	}

	public String getUserName() {
		return this.config.getProperty("bookkeeper.web.clearcheckbook.username");
	}
}
