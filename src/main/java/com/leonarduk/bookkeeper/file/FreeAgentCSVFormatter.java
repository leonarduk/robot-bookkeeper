/**
 * FreeAgentCSVFormatter
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FreeAgentCSVFormatter {

	public void format(final List<TransactionRecord> transactionRecords,
	        final String outputFileName) throws IOException {
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try (PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");) {
			for (final TransactionRecord transactionRecord : transactionRecords) {
				final Date date = transactionRecord.getDate();
				writer.println(formatter.format(date) + "," + transactionRecord.getAmount() + ","
				        + transactionRecord.getDescription().replaceAll(",", ";"));
			}
		}

	}
}
