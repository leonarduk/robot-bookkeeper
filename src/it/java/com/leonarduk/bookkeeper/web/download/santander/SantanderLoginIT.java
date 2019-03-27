/**
 * SantanderDownloadTransactionsIT
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.santander;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;

public class SantanderLoginIT {
	static final Logger LOGGER = Logger.getLogger(SantanderLoginIT.class);

	@Test
	public final void testSantanderLogin() throws IOException {
		final SantanderConfig config = new SantanderConfig(SitConfig.getSitConfig());

		final SantanderLogin santanderLogin = new SantanderLogin(config);
		santanderLogin.get();
		SantanderLoginIT.LOGGER.info("Complete");
		santanderLogin.getBrowserController().close();
	}

}
