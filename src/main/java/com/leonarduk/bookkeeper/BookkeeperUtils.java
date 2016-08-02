/**
 * Bookkeeper
 *
 * @author ${author}
 * @since 03-Jul-2016
 */
package com.leonarduk.bookkeeper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.bookkeeper.web.download.amex.AmexConfig;
import com.leonarduk.bookkeeper.web.download.amex.AmexDownloadTransactions;
import com.leonarduk.bookkeeper.web.download.clearcheckbook.ClearCheckBookValueUpdater;
import com.leonarduk.bookkeeper.web.download.nationwide.NationwideAccount;
import com.leonarduk.bookkeeper.web.download.nationwide.NationwideConfig;
import com.leonarduk.bookkeeper.web.download.nationwide.NationwideLogin;
import com.leonarduk.bookkeeper.web.download.santander.SantanderConfig;
import com.leonarduk.bookkeeper.web.download.santander.SantanderDownloadTransactions;
import com.leonarduk.bookkeeper.web.download.santander.SantanderLogin;
import com.leonarduk.bookkeeper.web.download.zoopla.ZooplaConfig;
import com.leonarduk.bookkeeper.web.download.zoopla.ZooplaEstimate;
import com.leonarduk.bookkeeper.web.upload.TransactionUploader;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookTransactionUploader;
import com.leonarduk.bookkeeper.web.upload.freeagent.FreeAgentConfig;
import com.leonarduk.bookkeeper.web.upload.freeagent.FreeAgentLogin;
import com.leonarduk.bookkeeper.web.upload.freeagent.FreeAgentUploadTransactions;
import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.webscraper.core.config.Config;

public class BookkeeperUtils {
	private static final Logger LOGGER = Logger.getLogger(BookkeeperUtils.class);

	public static TransactionWorker getZooplaValueInClearcheckbookTransactionWorker(
	        final Config config, final String ccbAccountName) throws Exception {
		final ClearCheckbookConfig config2 = new ClearCheckbookConfig(config);
		final ZooplaConfig zooplaConfig = new ZooplaConfig(config);
		try (final ClearCheckbookTransactionUploader clearCheckBook = new ClearCheckbookTransactionUploader(
		        config2);
		        final ZooplaEstimate zooplaEstimate = new ZooplaEstimate(zooplaConfig);
		        ClearCheckBookValueUpdater updater = new ClearCheckBookValueUpdater(zooplaEstimate,
		                config2, ccbAccountName);) {
			clearCheckBook.setAccount(config.getProperty("bookkeeper.web.clearcheckbook.zoopla"));
			final TransactionWorker worker = new TransactionWorker(updater, clearCheckBook);
			return worker;
		}
	}

	public static List<TransactionRecord> updateZooplaValueInClearcheckbook(final Config config,
	        final String ccbAccountName) throws Exception {
		final ClearCheckbookConfig config2 = new ClearCheckbookConfig(config);
		final ZooplaConfig zooplaConfig = new ZooplaConfig(config);
		try (final ClearCheckbookTransactionUploader clearCheckBook = new ClearCheckbookTransactionUploader(
		        config2);
		        final ZooplaEstimate zooplaEstimate = new ZooplaEstimate(zooplaConfig);
		        ClearCheckBookValueUpdater updater = new ClearCheckBookValueUpdater(zooplaEstimate,
		                config2, ccbAccountName);) {
			clearCheckBook.setAccount(config.getProperty("bookkeeper.web.clearcheckbook.zoopla"));
			final TransactionWorker worker = new TransactionWorker(updater, clearCheckBook);
			return worker.call();
		}
	}

	public static List<TransactionRecord> uploadAmexTransactionsToClearCheckBook(
	        final Config config) throws Exception {
		final ClearCheckbookConfig ccbConfig = new ClearCheckbookConfig(config);
		try (final ClearCheckbookTransactionUploader clearCheckBook = new ClearCheckbookTransactionUploader(
		        ccbConfig);
		        final AmexDownloadTransactions amexTransactions = new AmexDownloadTransactions(
		                new AmexConfig(config));) {
			final String ccbAccountName = config.getProperty("bookkeeper.web.clearcheckbook.amex");
			clearCheckBook.setAccount(ccbAccountName);
			final List<TransactionRecord> importedTransactions = BookkeeperUtils
			        .uploadTransactionsFromSource(amexTransactions, clearCheckBook);
			final ClearCheckBookValueUpdater updater = new ClearCheckBookValueUpdater(
			        amexTransactions, ccbConfig, ccbAccountName);
			final List<TransactionRecord> balancingTransaction = BookkeeperUtils
			        .uploadTransactionsFromSource(updater, clearCheckBook);
			importedTransactions.addAll(balancingTransaction);
			return importedTransactions;
		}
	}

	public static List<TransactionRecord> uploadNationwideTransactionsToClearCheckBook(
	        final Config config, final int accountId) throws Exception {
		final ClearCheckbookConfig ccbConfig = new ClearCheckbookConfig(config);
		try (final ClearCheckbookTransactionUploader clearCheckBook = new ClearCheckbookTransactionUploader(
		        ccbConfig);
		        final NationwideAccount transactions = new NationwideAccount(
		                new NationwideLogin(new NationwideConfig(config)), accountId);) {
			final String ccbAccountName = config.getProperty("bookkeeper.web.clearcheckbook.joint");
			clearCheckBook.setAccount(ccbAccountName);
			final TransactionWorker worker = new TransactionWorker(transactions, clearCheckBook);
			return worker.call();
		}
	}

	public static List<TransactionRecord> uploadSantanderTransactionsToFreeAgent(
	        final Config config) throws Exception {
		try (final FreeAgentUploadTransactions freeAgent = new FreeAgentUploadTransactions(
		        new FreeAgentLogin(new FreeAgentConfig(config)));
		        final SantanderDownloadTransactions santanderTransactions = new SantanderDownloadTransactions(
		                new SantanderLogin(new SantanderConfig(config)));) {
			final TransactionWorker worker = new TransactionWorker(santanderTransactions,
			        freeAgent);
			return worker.call();
		}
	}

	public static List<TransactionRecord> uploadTransactionsFromSource(
	        final TransactionDownloader downloader, final TransactionUploader uploader)
	                throws Exception {
		try {
			if (downloader instanceof BaseSeleniumPage) {
				((BaseSeleniumPage) downloader).get();
			}
			List<TransactionRecord> transactions = downloader.downloadTransactions();
			if (null == transactions) {
				return new ArrayList<>();
			}
			BookkeeperUtils.LOGGER.info("There are " + transactions.size() + " records to upload");
			if (transactions.size() > 0) {
				if (uploader instanceof BaseSeleniumPage) {
					((BaseSeleniumPage) uploader).get();
				}
				transactions = uploader.uploadTransactions(transactions);
			}
			return transactions;
		}
		catch (final Throwable e) {
			BookkeeperUtils.LOGGER.error("Error uploading fetching or uploading", e);
			final List<TransactionRecord> transactions = new ArrayList<>();
			transactions.add(new TransactionRecord(0, "Error: " + e.getLocalizedMessage(),
			        new Date(), "", ""));
			return transactions;
		}
	}
}
