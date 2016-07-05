/**
 * ClearCheckBookValueUpdaterIT
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.webscraper.core.config.Config;

public class ClearCheckBookValueUpdaterIT {

	private ValueSnapshotProvider getValueUpdater() {
		final ValueSnapshotProvider valueSnapshotProvider = new ValueSnapshotProvider() {

			@Override
			public double getCurrentValue() {
				return new Date().getTime();
			}

			@Override
			public String getDescription() {
				return "Dummy service";
			}
		};
		return valueSnapshotProvider;
	}

	/**
	 * Test update estimate.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testUpdateEstimate() throws Exception {
		final ValueSnapshotProvider valueSnapshotProvider = this.getValueUpdater();
		try (final ClearCheckBookValueUpdater updater = new ClearCheckBookValueUpdater(
		        valueSnapshotProvider,
		        new ClearCheckbookConfig(new Config("bookkeeper-sit.properties")), "zoopla");) {
			final List<TransactionRecord> transactions = updater.downloadTransactions();
			System.out.println(transactions.toString());
		}
	}

}
