/**
 * ClearCheckBookApiClient
 *
 * @author ${author}
 * @since 10-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.file.DateUtils;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.clearcheckbook.ClearCheckBookConnection;
import com.leonarduk.clearcheckbook.ClearcheckbookException;
import com.leonarduk.clearcheckbook.calls.AccountCall;
import com.leonarduk.clearcheckbook.calls.TransactionCall;
import com.leonarduk.clearcheckbook.dto.AccountDataType;
import com.leonarduk.clearcheckbook.dto.AccountDataType.Type;
import com.leonarduk.clearcheckbook.dto.ParsedNameValuePair;
import com.leonarduk.clearcheckbook.dto.TransactionDataType;

public class ClearCheckBookApiClient {
	private static final Logger _logger = Logger.getLogger(ClearCheckBookApiClient.class);

	private final AccountCall accountCall;
	private final SimpleDateFormat dateFormatter;
	private final TransactionCall transactionCall;

	public ClearCheckBookApiClient(final ClearCheckbookConfig config) {
		this(config.getUserName(), config.getPassword());
	}

	public ClearCheckBookApiClient(final String user, final String password) {
		final ClearCheckBookConnection connection = new ClearCheckBookConnection(user, password);
		this.accountCall = new AccountCall(connection);
		this.transactionCall = new TransactionCall(connection);
		this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public TransactionDataType convertToTransactionDataType(final TransactionRecord record,
			final AccountDataType account) {
		final Boolean jive = Boolean.TRUE;
		final String memo = record.getDescription();
		final Long fromAccountId = null;
		final Long categoryId = Long.valueOf(0);
		final Long toAccountId = Long.valueOf(account.getId());
		return TransactionDataType.create(this.dateFormatter.format(record.getDate()),
				Double.valueOf(record.getAmount()), toAccountId, categoryId, record.getDescription(), jive,
				fromAccountId, toAccountId, record.getCheckNumber(), memo, record.getPayee());
	}

	public TransactionRecord convertToTransactionRecord(final TransactionDataType record)
			throws ParseException, ClearcheckbookException {
		double amount = record.getAmount().doubleValue();
		final com.leonarduk.clearcheckbook.dto.TransactionDataType.Type transactionType = record.getTransactionType();
		if (transactionType.equals(TransactionDataType.Type.WITHDRAWAL)) {
			amount *= -1;
		}
		return new TransactionRecord(amount, this.trim(record.getDescription()),
				DateUtils.parse(record.getDate()), this.trim(record.getCheckNum()),
				this.trim(record.getPayee()));
	}

	public void createAccount(final String accountName, final Type accountType, final double initialBalance)
			throws ClearcheckbookException {
		ClearCheckBookApiClient._logger.info("Create Account: " + accountName);
		this.accountCall.insert(AccountDataType.create(accountName, accountType, initialBalance));

	}

	public boolean deleteAccount(final String accountName) throws ClearcheckbookException {
		ClearCheckBookApiClient._logger.info("Delete Account: " + accountName);
		final AccountDataType accountDataType = this.getAccount(accountName);
		final ParsedNameValuePair idParameter = accountDataType.getIdParameter();
		return this.accountCall.delete(idParameter);
	}

	public AccountDataType getAccount(final String accountName) throws ClearcheckbookException {
		final AccountDataType account = this.accountCall.get(accountName);
		return account;
	}

	public double getBalanceForAccount(final String accountName) throws ClearcheckbookException {
		return this.accountCall.get(accountName).getCurrentBalance().doubleValue();
	}

	public SimpleDateFormat getDateFormatter() {
		return this.dateFormatter;
	}

	public List<TransactionRecord> getTransactionRecordsForAccount(final AccountDataType accountId,
			final int numberOfRecords) throws ClearcheckbookException, ParseException {
		final List<TransactionDataType> existing = this.transactionCall.getAll(accountId.getId(), 1, numberOfRecords);
		final List<TransactionRecord> existingRecords = new ArrayList<>();
		for (final TransactionDataType transactionDataType : existing) {
			existingRecords.add(this.convertToTransactionRecord(transactionDataType));
		}
		return existingRecords;
	}

	public String insertRecord(final TransactionRecord record, final String accountName)
			throws ClearcheckbookException {
		return this.transactionCall.insert(this.convertToTransactionDataType(record, this.getAccount(accountName)));
	}

	public List<TransactionRecord> insertRecords(final List<TransactionRecord> records, final String accountName,
			final int numberOfExistingTransactionsToCheck, TransactionRecordFilter filter)
			throws ClearcheckbookException, ParseException {
		final AccountDataType accountId = this.getAccount(accountName);
		final List<TransactionRecord> existingRecords = this.getTransactionRecordsForAccount(accountId,
				numberOfExistingTransactionsToCheck);
		final List<TransactionRecord> added = new ArrayList<>();
		for (final TransactionRecord transactionRecord : records) {
			if (!existingRecords.contains(transactionRecord)) {
				final TransactionDataType convertToTransactionDataType = this
						.convertToTransactionDataType(transactionRecord, accountId);
				this.transactionCall.insert(convertToTransactionDataType);
				if (filter.include(transactionRecord)) {
					added.add(transactionRecord);
					existingRecords.add(transactionRecord);
				}
			} else {
				ClearCheckBookApiClient._logger.info("Skip duplicate: " + transactionRecord);
			}
		}
		return added;
	}

	public String trim(final String text) {
		if (null == text) {
			return "";
		}
		return text.trim();
	}

}
