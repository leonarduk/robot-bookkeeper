/**
 * FreeAgentUploadTransactionsIT
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.web.upload.freeagent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.DateUtils;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;

public class FreeAgentUploadTransactionsIT {

	private FreeAgentUploadTransactions transactions;

	@SuppressWarnings("resource")
	@Before
	public void setUp() throws Exception {
		this.transactions = new FreeAgentUploadTransactions(
		        new FreeAgentLogin(new FreeAgentConfig(SitConfig.getSitConfig())));
		this.transactions.get();
	}

	@After
	public void tearDown() throws Exception {
		this.transactions.close();
	}

	@Test
	public final void testUploadTransactions() throws IOException {
		final FileFormatter formatter = new QifFileFormatter(QifFileFormatter.FREEAGENT_FORMAT);
		final List<TransactionRecord> transactionRecords = new ArrayList<>();
		transactionRecords.add(new TransactionRecord(-12.23, "Payment",
		        DateUtils.stringToDate("2016/06/23"), "1", "Payee"));
		transactionRecords.add(new TransactionRecord(2.23, "Receipt",
		        DateUtils.stringToDate("2016/06/26"), "2", "Payee2"));
		final String outputFileName = "output.csv";
		formatter.format(transactionRecords, outputFileName);

		this.transactions.uploadTransactions(outputFileName);
	}

}
