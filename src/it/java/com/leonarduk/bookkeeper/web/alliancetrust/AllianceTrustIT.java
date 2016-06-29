/**
 * AllianceTrustIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.alliancetrust;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.webscraper.core.config.Config;

public class AllianceTrustIT {

	@Test
	public final void testGetValue() throws IOException {
		final WebDriver webDriver = new FirefoxDriver();
		final String steveaccount = "155266";
		final String lucyAccount = "155385";

		final AllianceTrust trust = new AllianceTrust(webDriver,
		        new AllianceTrustConfig(new Config("bookkeeper-sit.properties")));
		trust.get();

		final String pensionValue = trust.getValue(1, steveaccount);
		System.out.println("Pension:" + pensionValue);

		final String steveLisaValue = trust.getValue(2, steveaccount);
		System.out.println("Steve ISA:" + steveLisaValue);

		final String lucyIsaValue = trust.getValue(1, lucyAccount);
		System.out.println("Lucy ISA:" + lucyIsaValue);
	}

}
