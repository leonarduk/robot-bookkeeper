/**
 * EmailParserIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.email;

import java.io.IOException;

import org.junit.Test;

public class EmailParserIT {

	@Test
	public final void testProcessEmails() throws IOException {
		EmailParser.processEmails(SitConfig.getSitConfig());
	}

}
