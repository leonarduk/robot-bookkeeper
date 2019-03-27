package com.leonarduk.bookkeeper.file;

public interface TransactionRecordFilter {
	public boolean include(TransactionRecord record);
}
