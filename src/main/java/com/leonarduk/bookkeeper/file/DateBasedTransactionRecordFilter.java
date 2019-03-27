package com.leonarduk.bookkeeper.file;

import java.time.LocalDate;

public class DateBasedTransactionRecordFilter implements TransactionRecordFilter {

	private LocalDate fromDate;
	private LocalDate toDate;

	public DateBasedTransactionRecordFilter(LocalDate fromDate, LocalDate toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	@Override
	public boolean include(TransactionRecord record) {
		LocalDate date = record.getDate();
		return ((fromDate == null || date.isAfter(fromDate) || date.isEqual(fromDate))
				&& (toDate == null || date.isBefore(toDate) || date.isEqual(toDate)));
	}

}
