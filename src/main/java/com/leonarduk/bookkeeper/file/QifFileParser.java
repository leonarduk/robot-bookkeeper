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

import org.apache.commons.lang3.StringUtils;

/**
 * The Class QifFileParser.
 */
public class QifFileParser implements FileParser {

	/**
	 * https://en.wikipedia.org/wiki/Quicken_Interchange_Format
	 *
	 * @param fileName
	 *            the file name
	 * @return the list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public List<TransactionRecord> parse(final String fileName) throws IOException {
		// Open the file
		final List<TransactionRecord> records = new ArrayList<>();
		try (FileInputStream fstream = new FileInputStream(fileName);
		        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));) {

			String strLine;

			/*
			 * !Type:CCard D20/06/2016 N10626908 T-9.70 PTFL TRAVEL CHARGE TFL.GOV.UK/CP M Process
			 * Date 20/06/2016 ^
			 *
			 */
			// Read File Line By Line
			double amount = 0;
			String description = "";
			Date date = null;
			String checkNumber = "";
			String payee = "";

			while ((strLine = br.readLine()) != null) {
				if (StringUtils.isEmpty(strLine)) {
					continue;
				}
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
					case 'N': // Description
						checkNumber = value.trim();
						break;
					case 'M': // Description
						description = value.trim();
						break;
					case 'P': // Description
						payee = value.trim();
						break;
					case '^': // End of record
						records.add(new TransactionRecord(amount, description, date, checkNumber,
						        payee));
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
