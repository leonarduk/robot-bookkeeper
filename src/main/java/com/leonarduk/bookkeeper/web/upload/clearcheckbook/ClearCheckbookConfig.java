/**
 * ClearCheckbookConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class ClearCheckbookConfig extends AbstractWebConfig {
	public ClearCheckbookConfig(final Config config) throws IOException {
		super(config);
	}

	public CharSequence getPassword() {
		return this.getConfig().getProperty("bookkeeper.web.clearcheckbook.password");
	}

	public String getUserName() {
		return this.getConfig().getProperty("bookkeeper.web.clearcheckbook.username");
	}

	public boolean isRemoveDuplicatesEnabled() {
		return this.getConfig()
		        .getBooleanProperty("bookkeeper.web.clearcheckbook.removeduplicates");
	}
}
