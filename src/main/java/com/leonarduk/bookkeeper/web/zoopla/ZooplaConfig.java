/**
 * ZooplaConfig
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.zoopla;

import com.leonarduk.webscraper.core.config.Config;

public class ZooplaConfig {
	private final Config config;

	public ZooplaConfig(final Config config) {
		this.config = config;
	}

	public String getUrl() {
		return this.config.getProperty("bookkeeper.web.zoopla.url");
	}
}
