/**
 * QifFileParser
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QifFileParser {

	public List<TransactionRecord> parse(final String fileName) throws IOException {
		// Open the file
		final List<TransactionRecord> records = new ArrayList<>();
		try (FileInputStream fstream = new FileInputStream(fileName);
		        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));) {

			String strLine;

			// Read File Line By Line
			double amount = 0;
			String description = "";
			Date date = null;
			while ((strLine = br.readLine()) != null) {
				final char leadingChar = strLine.charAt(0);
				final String value = strLine.substring(1);

				switch (leadingChar) {
					case '!': // Start of file
						break;
					case 'D': // Date
						date = DateUtils.stringToDate(value);
						break;
					case 'T': // Amount
						amount = Double.valueOf(value).doubleValue();
						break;
					case 'P': // Description
						description = value;
						break;
					case '^': // End of record
						records.add(new TransactionRecord(amount, description, date));
						amount = 0;
						description = "";
						date = null;
						break;
					default:
						break;
				}
			}

			return records;
		}
	}

}
