/**
 * ZooplaConfig
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.zoopla;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class ZooplaConfig extends AbstractWebConfig {

	public ZooplaConfig(final Config config) throws IOException {
		super(config);
	}

	public String getUrl() {
		return this.getConfig().getProperty("bookkeeper.web.zoopla.url");
	}
}
