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
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;

public interface TransactionDownloader {

	List<TransactionRecord> saveTransactions(TransactionRecordFilter filter) throws IOException;

	String downloadTransactionsFile() throws IOException;

}
