/**
 * TransactionRecord
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.util.Date;

public class TransactionRecord {
	private final double	amount;
	private final String	description;
	private final Date		date;

	public TransactionRecord(final double amount, final String description, final Date date2) {
		this.amount = amount;
		this.description = description;
		this.date = date2;
	}

	public double getAmount() {
		return this.amount;
	}

	public Date getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}
}
