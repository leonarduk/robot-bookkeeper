/**
 * ClearCheckBookValueUpdaterIT
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;

public class ClearCheckBookValueUpdaterIT {

	private ValueSnapshotProvider getValueUpdater() {
		final ValueSnapshotProvider valueSnapshotProvider = new ValueSnapshotProvider() {

			@Override
			public double getCurrentValue() {
				final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HH");
				return Double.parseDouble(format.format(new Date()));
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
		        valueSnapshotProvider, new ClearCheckbookConfig(SitConfig.getSitConfig()),
		        "zoopla");) {
			TransactionRecordFilter filter = (record) -> true;
			final List<TransactionRecord> transactions = updater.saveTransactions(filter);
			System.out.println(transactions.toString());
		}
	}

}
