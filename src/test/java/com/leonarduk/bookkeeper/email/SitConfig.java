/**
 * SitConfig
 * 
 * @author ${author}
 * @since 11-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.io.IOException;

import com.leonarduk.webscraper.core.config.Config;

public class SitConfig {
	public static Config getSitConfig() throws IOException {
		return new Config("bookkeeper-sit.properties");
	}
}
