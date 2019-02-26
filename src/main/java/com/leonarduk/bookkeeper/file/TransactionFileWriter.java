package com.leonarduk.bookkeeper.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import com.leonarduk.bookkeeper.web.upload.TransactionUploader;

public class TransactionFileWriter implements TransactionUploader {

	private File file;
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public TransactionFileWriter(File outputFile) {
		this.file = outputFile;
	}

	@Override
	public List<TransactionRecord> writeTransactions(List<TransactionRecord> transactions) throws Exception {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			String delimiter = ",";
			writer.write(TransactionRecord.getHeaderString(delimiter));
			writer.newLine();
			for (TransactionRecord transactionRecord : transactions) {
				writer.write(transactionRecord.toDataString(delimiter, dateformat));
				writer.newLine();
			}

			return transactions;
		}
	}

}
