package com.leonarduk.bookkeeper.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.leonarduk.bookkeeper.web.upload.TransactionUploader;

public class TransactionFileWriter implements TransactionUploader {

	DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public List<TransactionRecord> writeTransactions(List<TransactionRecord> transactions, String fileName,
			TransactionRecordFilter filter) throws Exception {
		return writeTransactions(transactions, new File(fileName), filter);
	}

	public List<TransactionRecord> writeTransactions(List<TransactionRecord> transactions, File downloadFile,
			TransactionRecordFilter filter) throws Exception {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(downloadFile))) {
			String delimiter = ",";
			writer.write(TransactionRecord.getHeaderString(delimiter));
			writer.newLine();
			for (TransactionRecord transactionRecord : transactions) {
				if (filter.include(transactionRecord)) {
					writer.write(transactionRecord.toDataString(delimiter, dateformat));
					writer.newLine();
				}
			}

			return transactions;
		}
	}

}
