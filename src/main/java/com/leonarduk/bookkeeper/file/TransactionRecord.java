/**
 * TransactionRecord
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.file;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;

/**
 * The Class TransactionRecord.
 */
public class TransactionRecord {

	/** The amount. */
	private final double amount;

	/** The description. */
	private final String description;

	/** The date. */
	private final LocalDate date;

	/** The check number. */
	private final String checkNumber;

	/** The payee. */
	private final String payee;

	/**
	 * Instantiates a new transaction record.
	 *
	 * @param amount      the amount
	 * @param description the description
	 * @param date2       the date2
	 * @param checkNumber the check number
	 * @param payee       the payee
	 */
	public TransactionRecord(final double amount, final String description, final LocalDate date2,
			final String checkNumber, final String payee) {
		this.amount = amount;
		this.description = description;
		this.date = date2;
		this.checkNumber = checkNumber;
		this.payee = payee;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final TransactionRecord other = (TransactionRecord) obj;
		if (Double.doubleToLongBits(this.amount) != Double.doubleToLongBits(other.amount)) {
			return false;
		}
		if (this.checkNumber == null) {
			if (other.checkNumber != null) {
				return false;
			}
		} else if (!this.checkNumber.equals(other.checkNumber)) {
			return false;
		}
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!this.date.equals(other.date)) {
			return false;
		}
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		if (this.payee == null) {
			if (other.payee != null) {
				return false;
			}
		} else if (!this.payee.equals(other.payee)) {
			return false;
		}
		return true;
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
	public LocalDate getDate() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.amount);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		result = (prime * result) + ((this.checkNumber == null) ? 0 : this.checkNumber.hashCode());
		result = (prime * result) + ((this.date == null) ? 0 : this.date.hashCode());
		result = (prime * result) + ((this.description == null) ? 0 : this.description.hashCode());
		result = (prime * result) + ((this.payee == null) ? 0 : this.payee.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "TransactionRecord [amount=" + this.amount + ", description=" + this.description + ", date=" + this.date
				+ ", checkNumber=" + this.checkNumber + ", payee=" + this.payee + "]";
	}

	public String toDataString(String delimiter, DateTimeFormatter dateformat) {
		return this.amount + delimiter + this.description + delimiter + this.date.format(dateformat) + delimiter
				+ this.checkNumber + delimiter + this.payee;

	}

	public static String getHeaderString(String delimiter) {
		return "amount" + delimiter + "description" + delimiter + "date" + delimiter + "checkNumber" + delimiter
				+ "payee";
	}

	public static TransactionRecord fromString(String dataString, String delimiter, DateFormat dateformat)
			throws NumberFormatException, ParseException {
		Iterator<String> fields = Arrays.asList(dataString.split(delimiter)).iterator();
		return new TransactionRecord(Double.parseDouble(fields.next()), fields.next(), DateUtils.parse(fields.next()),
				fields.next(), fields.next());
	}
}
