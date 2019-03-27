/**
 * TransactionUploader
 *
 * @author ${author}
 * @since 03-Jul-2016
 */
package com.leonarduk.bookkeeper.web.upload;

import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;

public interface TransactionUploader {

	List<TransactionRecord> writeTransactions(List<TransactionRecord> transactions, String file,
			TransactionRecordFilter filter) throws Exception;

}
