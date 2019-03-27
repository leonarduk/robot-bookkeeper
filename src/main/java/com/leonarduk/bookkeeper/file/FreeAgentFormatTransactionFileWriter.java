package com.leonarduk.bookkeeper.file;

import java.text.SimpleDateFormat;
import java.util.List;

import com.leonarduk.bookkeeper.web.upload.TransactionUploader;
import com.leonarduk.bookkeeper.web.upload.freeagent.FreeAgentUploadTransactions;

public class FreeAgentFormatTransactionFileWriter implements TransactionUploader {

	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<TransactionRecord> writeTransactions(List<TransactionRecord> transactions, String file, TransactionRecordFilter filter) throws Exception {
		FreeAgentUploadTransactions.getQifFileFormatter().format(transactions, file, filter);
		return transactions;
	}

}
