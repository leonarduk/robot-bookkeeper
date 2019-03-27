package com.leonarduk.bookkeeper.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ListIterator;

import com.google.common.io.Files;
import com.google.inject.internal.util.Lists;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;

public class TransactionFileReader implements TransactionDownloader {

	private File file;

	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public TransactionFileReader(File inputFile) {
		file = inputFile;
	}

	@Override
	public List<TransactionRecord> saveTransactions(TransactionRecordFilter filter) throws IOException {
		List<TransactionRecord> transactions = Lists.newArrayList();
		ListIterator<String> lineIter = Files.readLines(file, Charset.defaultCharset()).listIterator();
		if (lineIter.hasNext()) {
			lineIter.next(); // skip header

			while (lineIter.hasNext()) {
				try {
					TransactionRecord record = TransactionRecord.fromString(lineIter.next(), ",", dateformat);
					if (filter.include(record)) {
						transactions.add(record);
					}
				} catch (NumberFormatException | ParseException e) {
					throw new IOException(e);
				}
			}
		}
		return transactions;
	}

	@Override
	public String downloadTransactionsFile() throws IOException {
		return file.getAbsolutePath();
	}

}
