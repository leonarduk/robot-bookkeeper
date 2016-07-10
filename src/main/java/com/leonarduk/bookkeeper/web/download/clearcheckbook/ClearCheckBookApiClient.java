/**
 * ClearCheckBookApiClient
 *
 * @author ${author}
 * @since 10-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.text.SimpleDateFormat;

import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.clearcheckbook.ClearCheckBookConnection;
import com.leonarduk.clearcheckbook.ClearcheckbookException;
import com.leonarduk.clearcheckbook.calls.AccountCall;
import com.leonarduk.clearcheckbook.calls.TransactionCall;
import com.leonarduk.clearcheckbook.dto.AccountDataType;
import com.leonarduk.clearcheckbook.dto.TransactionDataType;

public class ClearCheckBookApiClient {
	private final AccountCall		accountCall;
	private final SimpleDateFormat	dateFormatter;
	private final TransactionCall	transactionCall;

	public ClearCheckBookApiClient(final ClearCheckbookConfig config) {
		this(config.getUserName(), config.getPassword());
	}

	public ClearCheckBookApiClient(final String user, final String password) {
		final ClearCheckBookConnection connection = new ClearCheckBookConnection(user, password);
		this.accountCall = new AccountCall(connection);
		this.transactionCall = new TransactionCall(connection);
		this.dateFormatter = new SimpleDateFormat(QifFileFormatter.CCB_FORMAT);
	}

	public TransactionDataType convertToTransactionDataType(final TransactionRecord record,
	        final String accountName) throws ClearcheckbookException {
		final Boolean jive = Boolean.TRUE;
		final String memo = record.getDescription();
		final Long fromAccountId = null;
		final Long categoryId = null;
		final AccountDataType account = this.accountCall.get(accountName);
		final Long toAccountId = Long.valueOf(account.getId());
		return TransactionDataType.create(this.dateFormatter.format(record.getDate()),
		        Double.valueOf(record.getAmount()), toAccountId, categoryId,
		        record.getDescription(), jive, fromAccountId, toAccountId, record.getCheckNumber(),
		        memo, record.getPayee());
	}

	public double getBalanceForAccount(final String accountName) throws ClearcheckbookException {
		return this.accountCall.get(accountName).getCurrentBalance().doubleValue();
	}

	public String insertRecord(final TransactionRecord record, final String accountName)
	        throws ClearcheckbookException {
		return this.transactionCall.insert(this.convertToTransactionDataType(record, accountName));
	}
}
