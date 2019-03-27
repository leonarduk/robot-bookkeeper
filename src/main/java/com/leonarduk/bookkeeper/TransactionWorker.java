/**
 * TransactionWorker
 *
 * @author ${author}
 * @since 27-Jul-2016
 */
package com.leonarduk.bookkeeper;

import java.util.List;
import java.util.concurrent.Callable;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.bookkeeper.web.upload.TransactionUploader;

/**
 * This class takes transactions from one source and uploads to the second.
 * 
 * @author Stephen Leonard
 *
 */
public class TransactionWorker implements Callable<List<TransactionRecord>> {
	private final TransactionDownloader downloader;
	private final TransactionUploader uploader;
	private TransactionRecordFilter filter;

	public TransactionWorker(final TransactionDownloader downloader, final TransactionUploader uploader,
			final TransactionRecordFilter filter) {
		this.downloader = downloader;
		this.uploader = uploader;
		this.filter = filter;
	}

	@Override
	public List<TransactionRecord> call() throws Exception {
		return BookkeeperUtils.uploadTransactionsFromSource(this.downloader, this.uploader, this.filter);
	}

}
