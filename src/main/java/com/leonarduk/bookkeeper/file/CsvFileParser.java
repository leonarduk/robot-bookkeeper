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
public class CsvFileParser implements FileParser {

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
			 * "Account Name:","Joint_Current_Acct ****48557"
			 * 
			 * "Account Balance:","£3929.24"
			 * 
			 * "Available Balance: ","£3929.24"
			 * 
			 * "Date","Transaction type","Description","Paid out","Paid in","Balance"
			 * 
			 * "30 May 2016","Contactless Payment","TFL.GOV.UK/CP TFL TRAVEL CH GB"
			 * ,"£1.50","","£2616.89"
			 * 
			 * "31 May 2016","Direct debit SKY DIGITAL","SKY DIGITAL","£31.96","","£2584.93"
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
						checkNumber = value;
						break;
					case 'M': // Description
						description = value;
						break;
					case 'P': // Description
						payee = value;
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
