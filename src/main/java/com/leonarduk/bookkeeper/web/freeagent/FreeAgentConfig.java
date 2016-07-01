/**
 * FreeAgentConfig
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.web.freeagent;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class FreeAgentConfig extends AbstractWebConfig {
	private static final String	URL			= "bookkeeper.web.freeagent.url";
	private static final String	USER		= "bookkeeper.web.freeagent.username";
	private static final String	PASSWORD	= "bookkeeper.web.freeagent.password";

	public FreeAgentConfig(final Config config) throws IOException {
		super(config);
	}

	public String getFreeagentUrl() {
		return this.getConfig().getProperty(FreeAgentConfig.URL);
	}

	public String getPassword() {
		return this.getConfig().getProperty(FreeAgentConfig.PASSWORD);
	}

	public String getUsername() {
		return this.getConfig().getProperty(FreeAgentConfig.USER);
	}
}
