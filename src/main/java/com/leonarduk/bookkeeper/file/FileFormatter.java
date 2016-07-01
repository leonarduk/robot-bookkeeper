/**
 * FileFormatter
 * 
 * @author ${author}
 * @since 29-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileFormatter {

	File format(List<TransactionRecord> transactionRecords, String outputFileName)
	        throws IOException;

}
