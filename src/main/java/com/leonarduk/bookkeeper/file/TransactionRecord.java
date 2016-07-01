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
	private final String	checkNumber;
	private final String	payee;

	public TransactionRecord(final double amount, final String description, final Date date2,
	        final String checkNumber, final String payee) {
		this.amount = amount;
		this.description = description;
		this.date = date2;
		this.checkNumber = checkNumber;
		this.payee = payee;
	}

	public double getAmount() {
		return this.amount;
	}

	public String getCheckNumber() {
		return this.checkNumber;
	}

	public Date getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public String getPayee() {
		return this.payee;
	}
}
