/**
 * ClearCheckBookValueUpdater
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.TransactionRecord;
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
	public List<TransactionRecord> downloadTransactions() throws IOException {
		final List<TransactionRecord> updates = new ArrayList<>();
		try {
			ClearCheckBookValueUpdater.LOGGER.info("downloadTransactions");

			final ClearCheckBookApiClient apiClient = new ClearCheckBookApiClient(this.config);
			final double ccbAmount = apiClient.getBalanceForAccount(this.accountname);

			final double amount = this.valueSnapshotProvider.getCurrentValue() - ccbAmount;
			if (Math.abs(amount) < 1) {
				ClearCheckBookValueUpdater.LOGGER.info("No change");
				return updates;
			}
			final int veryLargeChange = 100000;
			if (Math.abs(amount) > veryLargeChange) {
				ClearCheckBookValueUpdater.LOGGER
				        .warn("Suspected error, ignoring move from " + ccbAmount + " to " + amount);
				return updates;
			}
			ClearCheckBookValueUpdater.LOGGER
			        .info("Updating value from " + ccbAmount + " to " + amount);

			final Date date2 = new Date();

			updates.add(new TransactionRecord(amount, this.valueSnapshotProvider.getDescription(),
			        date2, "", ""));
		}
		catch (final ClearcheckbookException e) {
			updates.add(new TransactionRecord(0, e.getMessage(), new Date(), "error",
			        e.getLocalizedMessage()));
		}
		return updates;
	}

	@Override
	public String downloadTransactionsFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
