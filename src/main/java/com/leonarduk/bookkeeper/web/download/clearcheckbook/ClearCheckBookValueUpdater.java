/**
 * ClearCheckBookValueUpdater
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.clearcheckbook.ClearcheckbookException;

public class ClearCheckBookValueUpdater implements AutoCloseable, TransactionDownloader {

	private final static Logger LOGGER = Logger.getLogger(ClearCheckBookValueUpdater.class);

	private final ValueSnapshotProvider valueSnapshotProvider;

	private final ClearCheckbookConfig config;

	private final String accountname;

	public ClearCheckBookValueUpdater(final ValueSnapshotProvider valueSnapshotProvider,
			final ClearCheckbookConfig config, final String ccbAccountName) {
		this.config = config;
		this.valueSnapshotProvider = valueSnapshotProvider;
		this.accountname = ccbAccountName;
	}

	@Override
	public void close() throws Exception {
		this.config.getWebDriver().close();
	}

	@Override
	public List<TransactionRecord> saveTransactions(TransactionRecordFilter filter) throws IOException {
		final List<TransactionRecord> updates = new ArrayList<>();
		try {
			ClearCheckBookValueUpdater.LOGGER.info("downloadTransactions");

			final ClearCheckBookApiClient apiClient = new ClearCheckBookApiClient(this.config);
			final double ccbAmount = apiClient.getBalanceForAccount(this.accountname);

			final double currentValue = this.valueSnapshotProvider.getCurrentValue();
			final double amount = currentValue - ccbAmount;
			if (Math.abs(amount) < 0.01) {
				ClearCheckBookValueUpdater.LOGGER.info("No change");
				return updates;
			}
			// final int veryLargeChange = 100000;
			// if (Math.abs(amount) > veryLargeChange) {
			// ClearCheckBookValueUpdater.LOGGER.warn(
			// "Suspected error, ignoring move from " + ccbAmount + " to " + currentValue);
			// return updates;
			// }
			ClearCheckBookValueUpdater.LOGGER.info("Updating value from " + ccbAmount + " to " + currentValue);

			TransactionRecord record = new TransactionRecord(amount, this.valueSnapshotProvider.getDescription(),
					LocalDate.now(), "", "");
			if (filter.include(record)) {
				updates.add(record);
			}
		} catch (final ClearcheckbookException e) {
			updates.add(new TransactionRecord(0, e.getMessage(), LocalDate.now(), "error", e.getLocalizedMessage()));
		}
		return updates;
	}

	@Override
	public String downloadTransactionsFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
