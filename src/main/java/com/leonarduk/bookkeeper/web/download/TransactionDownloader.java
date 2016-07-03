/**
 * TransactionsDownloader
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download;

import java.io.IOException;
import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;

public interface TransactionDownloader {

	List<TransactionRecord> downloadTransactions() throws IOException;

	String downloadTransactionsFile();

}
