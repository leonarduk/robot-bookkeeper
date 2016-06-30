/**
 * ZooplaEstimateIT
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.zoopla;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.webscraper.core.config.Config;

public class ZooplaEstimateIT {

	@Test
	public final void testGetEstimate() throws IOException {
		final FirefoxDriver webDriver = new FirefoxDriver();
		final ZooplaEstimate zooplaEstimate = new ZooplaEstimate(webDriver,
		        new ZooplaConfig(new Config("bookkeeper-sit.properties")));
		zooplaEstimate.get();
		final String estimate = zooplaEstimate.getEstimate();
		Assert.assertTrue(estimate.startsWith("£"));
		System.out.println(estimate);
		webDriver.close();
	}

}
