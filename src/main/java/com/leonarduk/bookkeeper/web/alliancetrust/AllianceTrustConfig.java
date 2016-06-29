/**
 * AllianceTrustConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.alliancetrust;

import com.leonarduk.webscraper.core.config.Config;

public class AllianceTrustConfig {
	private final Config config;

	public AllianceTrustConfig(final Config config) {
		this.config = config;
	}

	public String getAccountNumber() {
		return this.config.getProperty("bookkeeper.web.alliancetrust.username");
	}

	public String getPassword() {
		return this.config.getProperty("bookkeeper.web.alliancetrust.password");
	}
}
