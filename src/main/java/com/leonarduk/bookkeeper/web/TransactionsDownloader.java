/**
 * TransactionsDownloader
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;

public interface TransactionsDownloader {

	List<TransactionRecord> downloadTransactions(File tempDir) throws IOException;

	String downloadTransactionsFile(File tempDir);

}
