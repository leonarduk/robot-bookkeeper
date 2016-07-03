/**
 * EmailFormatter
 *
 * @author ${author}
 * @since 03-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;

public interface EmailFormatter {

	String endMessageBody();

	String format(List<TransactionRecord> uploadSantanderTransactionsToFreeAgent);

	String formatHeader(String string);

	String formatSubHeader(String string);

	String startMessageBody();

}
