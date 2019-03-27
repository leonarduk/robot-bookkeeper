/**
 * FileParser
 * 
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.io.IOException;
import java.util.List;

public interface FileParser {

	List<TransactionRecord> parse(String fileName, TransactionRecordFilter filter) throws IOException;

}
