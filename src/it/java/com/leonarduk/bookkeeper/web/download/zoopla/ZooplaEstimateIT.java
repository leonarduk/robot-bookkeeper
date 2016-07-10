/**
 * ZooplaEstimateIT
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.zoopla;

import org.junit.Assert;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;

public class ZooplaEstimateIT {

	@Test
	public final void testGetEstimate() throws Exception {
		try (final ZooplaEstimate zooplaEstimate = new ZooplaEstimate(
		        new ZooplaConfig(SitConfig.getSitConfig()));) {
			final String estimate = zooplaEstimate.getEstimate();
			Assert.assertTrue(estimate.startsWith("£"));
			System.out.println(estimate);
		}
	}

}
