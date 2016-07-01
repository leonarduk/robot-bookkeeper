/**
 * HtmlFormatter
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.email;

import java.text.SimpleDateFormat;
import java.util.List;

import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.webscraper.core.format.HtmlFormatter;

public class HtmlRecordFormatter extends HtmlFormatter {

	private String addHeader(final String value) {
		return this.addNode(value, "th");
	}

	public String addNode(final String value, final String element) {
		return "<" + element + ">" + value + "</" + element + ">";
	}

	private String addValue(final String value) {
		return this.addNode(value, "td");
	}

	public String endHtml() {
		return "</html></body>";
	}

	public String format(final List<TransactionRecord> records) {
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

		final StringBuilder builder = new StringBuilder();
		builder.append("<tr>");
		builder.append(this.addHeader("Date"));
		builder.append(this.addHeader("Amount"));
		builder.append(this.addHeader("Description"));
		builder.append(this.addHeader("Payee"));
		builder.append(this.addHeader("CheckNumber"));
		builder.append("</tr>");

		for (final TransactionRecord transactionRecord : records) {
			builder.append("<tr>");
			builder.append(this.addValue(dateFormatter.format(transactionRecord.getDate())));
			builder.append(this.addValue(String.valueOf(transactionRecord.getAmount())));
			builder.append(this.addValue(transactionRecord.getDescription()));
			builder.append(this.addValue(transactionRecord.getPayee()));
			builder.append(this.addValue(transactionRecord.getCheckNumber()));
			builder.append("</tr>");
		}
		return builder.toString();
	}

	public String startHtml() {
		return "<html><body>";
	}
}