/**
 * EmailReporter
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.util.List;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.email.EmailException;
import com.leonarduk.webscraper.core.email.EmailSender;
import com.leonarduk.webscraper.core.email.EmailSession;
import com.leonarduk.webscraper.core.email.impl.EmailSessionImpl;

public class EmailReporter {
	static final Logger LOGGER = Logger.getLogger(EmailReporter.class);

	private final EmailConfig config;

	private final StringBuilder changes;

	private final EmailFormatter emailFormatter;

	private final EmailSender emailSender;

	public EmailReporter(final EmailConfig config, final EmailFormatter emailFormatter,
	        final EmailSender sender) {
		this.config = config;
		this.changes = new StringBuilder();
		this.emailFormatter = emailFormatter;
		this.startEmailBody();
		this.emailSender = sender;
	}

	public void addTransactions(final String accountName,
	        final List<TransactionRecord> transactions) {
		this.append(this.emailFormatter.formatSubHeader(accountName));
		this.append(this.emailFormatter.format(transactions));

	}

	public void append(final String text) {
		this.changes.append(text);
	}

	public void emailNotification(final String subject) throws EmailException {
		EmailReporter.LOGGER.debug("emailNotification");
		final String[] toEmail = this.config.getEmailTo();
		final String user = this.config.getEmailUser();
		final String server = this.config.getEmailServer();
		final String password = this.config.getEmailPassword();
		final String port = this.config.getEmailPort();
		final boolean useHtml = this.config.getUseHtml();

		final EmailSession session = new EmailSessionImpl(user, password, server, port);

		this.endEmailBody();
		this.emailSender.sendMessage(this.config.getFromEmail(), this.config.getFromEmailName(),
		        subject, this.changes.toString(), useHtml, session, toEmail);
	}

	public void endEmailBody() {
		this.append(this.emailFormatter.endMessageBody());
	}

	StringBuilder getChanges() {
		return this.changes;
	}

	public void startEmailBody() {
		this.append(this.emailFormatter.startMessageBody());
		this.append(this.emailFormatter.formatHeader("Todays Transactions"));

	}

}
