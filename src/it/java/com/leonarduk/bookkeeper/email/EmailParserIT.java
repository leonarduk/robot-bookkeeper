/**
 * EmailParserIT
 * 
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.email;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.EmailParser;
import com.leonarduk.webscraper.core.config.Config;

public class EmailParserIT {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testProcessEmails() throws IOException {
		final Config config = new Config("bookkeeper-sit.properties");
		EmailParser.processEmails(config);
	}

}
