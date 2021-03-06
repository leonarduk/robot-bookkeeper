/**
 * CsvFileParser
 *
 * @author ${author}
 * @since 15-Jul-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class NationwideCsvFileParser implements FileParser {

	public NationwideCsvFileParser() {
	}

	@Override
	public List<TransactionRecord> parse(final String fileName, TransactionRecordFilter filter) throws IOException {
		// Open the file
		final List<TransactionRecord> records = new ArrayList<>();
		try (FileInputStream fstream = new FileInputStream(fileName);
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));) {

			final int headerRows = 5;
			// Skip header
			for (int i = 0; i < headerRows; i++) {
				br.readLine();
			}
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (StringUtils.isEmpty(strLine)) {
					continue;
				}

				/**
				 * "Date","Transaction type","Description","Paid out","Paid in","Balance"
				 *
				 * "15 Jun 2016","Direct debit TV LICENCE MBP","TV LICENCE MBP"
				 * ,"£12.12","","£5541.45"
				 *
				 */

				final String[] fields = strLine.replaceAll("\"", "").split(",");
				final double amountIn = StringConversionUtils.convertMoneyString(fields[4]);
				final double amountOut = StringConversionUtils.convertMoneyString(fields[3]);
				final double amount = amountIn - amountOut;
				final String description = fields[1] + " " + fields[2];
				final String dateString = fields[0];
				final LocalDate date = DateUtils.parse(dateString);
				final String checkNumber = fields[5].replace("£", "");
				final String payee = fields[2];
				TransactionRecord record = new TransactionRecord(amount, description, date, checkNumber, payee);
				if (filter.include(record)) {
					records.add(record);
				}
			}

			return records;
		}
	}

}
