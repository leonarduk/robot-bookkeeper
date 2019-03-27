/**
 * QifFileFormatter
 *
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QifFileFormatter implements FileFormatter {

	public final static String FREEAGENT_FORMAT = "yyyy/MM/dd";

	public final static String CCB_FORMAT = "MM/dd/yyyy";
	private final String dateString;

	public QifFileFormatter(final String dateString) {
		this.dateString = dateString;
	}

	@Override
	public File format(final List<TransactionRecord> transactionRecords, final String outputFileName,
			TransactionRecordFilter filter) throws IOException {
		final File outputFile = new File(outputFileName);
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.dateString);
		try (PrintWriter writer = new PrintWriter(outputFile, "UTF-8");) {
			writer.println("!Type:Oth L");
			for (final TransactionRecord transactionRecord : transactionRecords) {
				if (filter.include(transactionRecord)) {
					final LocalDate date = transactionRecord.getDate();
					writer.println("D" + date.format(formatter));
					writer.println("T" + transactionRecord.getAmount());
					writer.println("M" + transactionRecord.getDescription());
					writer.println("P" + transactionRecord.getPayee());
					writer.println("N" + transactionRecord.getCheckNumber());
					writer.println("^");
				}
			}
		}
		return outputFile;
	}

}
