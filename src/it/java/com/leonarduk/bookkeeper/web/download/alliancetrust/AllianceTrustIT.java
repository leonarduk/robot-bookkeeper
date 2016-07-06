/**
 * AllianceTrustIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.alliancetrust;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.webscraper.core.config.Config;

public class AllianceTrustIT {

	@Test
	public final void testGetValue() throws IOException {
		final WebDriver webDriver = new FirefoxDriver();

		final AllianceTrustConfig config = new AllianceTrustConfig(
		        new Config("bookkeeper-sit.properties"));
		final AllianceTrust trust = new AllianceTrust(config);
		trust.get();

		final Object steveaccount = "getfromconfig";
		final String pensionValue = trust.getValue(1);
		Assert.assertNotNull(pensionValue);
		System.out.println("Pension:" + pensionValue);

		final String steveLisaValue = trust.getValue(2);
		System.out.println("Steve ISA:" + steveLisaValue);

		final Object lucyAccount;
		final String lucyIsaValue = trust.getValue(1);
		System.out.println("Lucy ISA:" + lucyIsaValue);
		webDriver.close();
	}

}
