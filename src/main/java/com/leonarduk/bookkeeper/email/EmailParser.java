/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.email;

import org.apache.log4j.Logger;

import com.leonarduk.webscraper.core.config.Config;
import com.leonarduk.webscraper.core.email.EmailReader;
import com.leonarduk.webscraper.core.email.ServerType;
import com.leonarduk.webscraper.core.email.impl.EmailProcessorChain;
import com.leonarduk.webscraper.core.email.impl.EmailReaderImpl;
import com.leonarduk.webscraper.core.email.impl.SimplePrintEmailProcessor;

/**
 * The Class EmailParser.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 3 Feb 2015
 */
public final class EmailParser {
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(EmailParser.class);

	/**
	 * Process emails.
	 *
	 * @param config
	 *            the config
	 */
	public static void processEmails(final Config config) {
		EmailParser.LOGGER.debug("processEmails");
		final String userName = config.getProperty("bookkeeper.email.user");
		final String server = config.getProperty("bookkeeper.email.server");
		final String serverTypeName = config.getProperty("bookkeeper.email.server.type");
		final String password = config.getProperty("bookkeeper.email.password");

		final ServerType serverType = ServerType.valueOf(serverTypeName);
		final EmailReader reader = new EmailReaderImpl();
		final String attachmentsFolder = config.getProperty("bookkeeper.email.attachments.savedir");
		final EmailProcessorChain processor = new EmailProcessorChain();
		processor.addProcessor(new SimplePrintEmailProcessor());
		processor.addProcessor(new BFCAInvoiceProcessor());

		reader.processMail(server, userName, password, serverType, attachmentsFolder, processor);
	}

	/**
	 * Instantiates a new email parser.
	 */
	private EmailParser() {
	}
}
