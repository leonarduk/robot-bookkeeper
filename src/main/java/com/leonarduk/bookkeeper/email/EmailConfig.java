/**
 * EmailConfig
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import com.leonarduk.webscraper.core.config.Config;

public class EmailConfig {
	private final Config config;

	public EmailConfig(final Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return this.config;
	}

	public String getEmailPassword() {
		return this.config.getProperty("bookkeeper.email.password");
	}

	public String getEmailPort() {
		return this.config.getProperty("bookkeeper.email.port");
	}

	public String getEmailServer() {
		return this.config.getProperty("bookkeeper.email.server");
	}

	public String[] getEmailTo() {
		return this.config.getArrayProperty("bookkeeper.email.to");
	}

	public String getEmailUser() {
		return this.config.getProperty("bookkeeper.email.user");
	}

	public String getFromEmail() {
		return this.config.getProperty("bookkeeper.email.from.email");
	}

	public String getFromEmailName() {
		return this.config.getProperty("bookkeeper.email.from.name");
	}

	public boolean getUseHtml() {
		return this.config.getBooleanProperty("bookkeeper.email.usehtml");
	}

	public boolean getUseSsl() {
		return this.config.getBooleanProperty("bookkeeper.email.usessl");
	}

}
