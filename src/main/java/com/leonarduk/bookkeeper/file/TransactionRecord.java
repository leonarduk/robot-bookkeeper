/**
 * TransactionRecord
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.util.Date;

/**
 * The Class TransactionRecord.
 */
public class TransactionRecord {

	/** The amount. */
	private final double amount;

	/** The description. */
	private final String description;

	/** The date. */
	private final Date date;

	/** The check number. */
	private final String checkNumber;

	/** The payee. */
	private final String payee;

	/**
	 * Instantiates a new transaction record.
	 *
	 * @param amount
	 *            the amount
	 * @param description
	 *            the description
	 * @param date2
	 *            the date2
	 * @param checkNumber
	 *            the check number
	 * @param payee
	 *            the payee
	 */
	public TransactionRecord(final double amount, final String description, final Date date2,
	        final String checkNumber, final String payee) {
		this.amount = amount;
		this.description = description;
		this.date = date2;
		this.checkNumber = checkNumber;
		this.payee = payee;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public double getAmount() {
		return this.amount;
	}

	/**
	 * Gets the check number.
	 *
	 * @return the check number
	 */
	public String getCheckNumber() {
		return this.checkNumber;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the payee.
	 *
	 * @return the payee
	 */
	public String getPayee() {
		return this.payee;
	}
}
