/**
 * FreeAgentCSVFormatter
 *
 * @author ${author}
 * @since 28-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvFormatter implements FileFormatter {

	@Override
	public File format(final List<TransactionRecord> transactionRecords, final String outputFileName,
			TransactionRecordFilter filter) throws IOException {
		final File outputFile = new File(outputFileName);
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		try (PrintWriter writer = new PrintWriter(outputFile, "UTF-8");) {
			for (final TransactionRecord transactionRecord : transactionRecords) {
				if (filter.include(transactionRecord)) {
					final LocalDate date = transactionRecord.getDate();
					writer.println(date.format(formatter) + "," + transactionRecord.getAmount() + ","
							+ transactionRecord.getDescription().replaceAll(",", ";"));
				}
			}
		}
		return outputFile;
	}
}
