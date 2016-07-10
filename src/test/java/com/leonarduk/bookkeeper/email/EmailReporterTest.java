/**
 * EmailReporterTest
 *
 * @author ${author}
 * @since 06-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.email.EmailSender;

public class EmailReporterTest {

	private EmailReporter	reporter;
	private EmailSender		sender;

	@Before
	public void setUp() throws Exception {
		final EmailConfig config = Mockito.mock(EmailConfig.class);
		final EmailFormatter emailFormatter = new HtmlEmailFormatter();
		this.sender = Mockito.mock(EmailSender.class);
		this.reporter = new EmailReporter(config, emailFormatter, this.sender);
	}

	@Test
	public final void testAddTransactions() {
		final List<TransactionRecord> transactions = new ArrayList<>();
		transactions
		        .add(new TransactionRecord(12.23, "deposit", new Date(2016, 3, 3), "12", "work"));
		this.reporter.addTransactions("test", transactions);
		Assert.assertEquals(
		        "<html><body><h1>Todays Transactions</h1><h2>test</h2><table><tr><th>Date</th><th>Amount</th><th>Description</th><th>Payee</th><th>CheckNumber</th></tr><tr><td>3916/04/03</td><td>12.23</td><td>deposit</td><td>work</td><td>12</td></tr></table>",
		        this.reporter.getChanges().toString());
	}

	@Test
	public final void testAppend() {
		this.reporter.append("eggs");
		Assert.assertEquals("<html><body><h1>Todays Transactions</h1>eggs",
		        this.reporter.getChanges().toString());
	}

	@Test
	public final void testEndEmailBody() {
		this.reporter.endEmailBody();
		Assert.assertEquals("<html><body><h1>Todays Transactions</h1></body></html>",
		        this.reporter.getChanges().toString());
	}

	@Test
	public final void testStartEmailBody() {
		this.reporter.startEmailBody();
		Assert.assertEquals(
		        "<html><body><h1>Todays Transactions</h1><html><body><h1>Todays Transactions</h1>",
		        this.reporter.getChanges().toString());
	}

}
