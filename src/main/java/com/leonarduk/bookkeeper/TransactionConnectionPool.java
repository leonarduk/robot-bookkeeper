/**
 * ConnectionPool
 *
 * @author ${author}
 * @since 27-Jul-2016
 */
package com.leonarduk.bookkeeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.leonarduk.bookkeeper.file.TransactionRecord;

public class TransactionConnectionPool {
	private final Collection<TransactionWorker>	tasks;
	private final int							numThreads;
	private final ExecutorService				executor;

	public TransactionConnectionPool(final int numThreads) {
		this.tasks = new ArrayList<>();
		this.numThreads = numThreads;

		this.executor = Executors.newFixedThreadPool(numThreads);
	}

	public void addTransactionWorker(final TransactionWorker worker) {
		this.tasks.add(worker);
	}

	public void process() throws InterruptedException, ExecutionException {
		try {
			final List<Future<List<TransactionRecord>>> results = this.executor
			        .invokeAll(this.tasks);
			for (final Future<List<TransactionRecord>> result : results) {
				final List<TransactionRecord> pingResult = result.get();
			}
		}
		finally {
			this.executor.shutdown(); // always reclaim resources
		}
	}
}
