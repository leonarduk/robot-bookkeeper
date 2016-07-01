/**
 * EmailReporter
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import org.apache.log4j.Logger;

import com.leonarduk.webscraper.core.email.EmailException;
import com.leonarduk.webscraper.core.email.EmailSender;
import com.leonarduk.webscraper.core.email.EmailSession;
import com.leonarduk.webscraper.core.email.impl.EmailSenderImpl;
import com.leonarduk.webscraper.core.email.impl.EmailSessionImpl;

public class EmailReporter {
	static final Logger LOGGER = Logger.getLogger(EmailReporter.class);

	private final EmailConfig config;

	public EmailReporter(final EmailConfig config) {
		this.config = config;
	}

	/**
	 * Email notification.
	 *
	 * @param config
	 *            the config
	 * @param changes
	 *            the changes
	 * @param subject
	 * @throws EmailException
	 *             the email exception
	 */
	public void emailNotification(final StringBuilder changes, final String subject)
	        throws EmailException {
		EmailReporter.LOGGER.debug("emailNotification");
		final String[] toEmail = this.config.getEmailTo();
		final String user = this.config.getEmailUser();
		final String server = this.config.getEmailServer();
		final String password = this.config.getEmailPassword();
		final String port = this.config.getEmailPort();
		final boolean useHtml = this.config.getUseHtml();

		final EmailSender emailSender = new EmailSenderImpl();

		final EmailSession session = new EmailSessionImpl(user, password, server, port);

		emailSender.sendMessage(this.config.getFromEmail(), this.config.getFromEmailName(), subject,
		        changes.toString(), useHtml, session, toEmail);
	}

}
