/**
 * EmailFormatter
 *
 * @author ${author}
 * @since 03-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.format.Formatter;

public interface EmailFormatter extends Formatter{

	String format(List<TransactionRecord> uploadSantanderTransactionsToFreeAgent);

}
