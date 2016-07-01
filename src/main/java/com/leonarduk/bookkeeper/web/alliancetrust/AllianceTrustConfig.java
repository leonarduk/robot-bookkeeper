/**
 * AllianceTrustConfig
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.alliancetrust;

import java.io.IOException;

import com.leonarduk.bookkeeper.web.AbstractWebConfig;
import com.leonarduk.webscraper.core.config.Config;

public class AllianceTrustConfig extends AbstractWebConfig {

	public AllianceTrustConfig(final Config config) throws IOException {
		super(config);
	}

	public String getAccountNumber() {
		return this.getConfig().getProperty("bookkeeper.web.alliancetrust.username");
	}

	public String getCustomerListViewUrl() {
		return this.getConfig().getProperty("bookkeeper.web.alliancetrust.customerListView.url");
	}

	public String getPassword() {
		return this.getConfig().getProperty("bookkeeper.web.alliancetrust.password");
	}
}
