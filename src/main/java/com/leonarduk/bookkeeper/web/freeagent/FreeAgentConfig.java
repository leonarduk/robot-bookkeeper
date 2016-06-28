/**
 * FreeAgentConfig
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.web.freeagent;

import com.leonarduk.webscraper.core.config.Config;

public class FreeAgentConfig {
	private static final String	URL			= "bookkeeper.web.freeagent.url";
	private static final String	USER		= "bookkeeper.web.freeagent.username";
	private static final String	PASSWORD	= "bookkeeper.web.freeagent.password";
	private final Config		config;

	public FreeAgentConfig(final Config config) {
		this.config = config;
	}

	public String getFreeagentUrl() {
		return this.config.getProperty(FreeAgentConfig.URL);
	}

	public String getPassword() {
		return this.config.getProperty(FreeAgentConfig.PASSWORD);
	}

	public String getUsername() {
		return this.config.getProperty(FreeAgentConfig.USER);
	}
}
