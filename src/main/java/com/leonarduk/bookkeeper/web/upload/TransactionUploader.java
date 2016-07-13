/**
 * TransactionUploader
 *
 * @author ${author}
 * @since 03-Jul-2016
 */
package com.leonarduk.bookkeeper.web.upload;

import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;

public interface TransactionUploader {

	List<TransactionRecord> uploadTransactions(List<TransactionRecord> transactions)
	        throws Exception;

}
