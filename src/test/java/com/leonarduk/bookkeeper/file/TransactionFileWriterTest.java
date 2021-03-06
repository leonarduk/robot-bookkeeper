package com.leonarduk.bookkeeper.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.leonarduk.webscraper.core.FileUtils;

public class TransactionFileWriterTest {

	TransactionFileWriter writer;
	private File tempFile;

	@Before
	public void startUp() throws IOException {
		File tempDir = FileUtils.createTempDir();
		tempFile = File.createTempFile("data", "csv", tempDir);
		this.writer = new TransactionFileWriter();
	}

	@Test
	public void testWriteTransactions() throws Exception {
		TransactionRecordFilter filter = (record) -> true;

		List<TransactionRecord> transactions = ImmutableList.of(
				new TransactionRecord(12.12, "description", DateUtils.parse("2019-02-02"), "checkNumber", "payee"),
				new TransactionRecord(34.21, "description", DateUtils.parse("2019-02-02"), "checkNumber", "payee"));
		this.writer.writeTransactions(transactions, tempFile, filter);

		String actual = FileUtils.getFileContents(this.tempFile.getAbsolutePath());
		String expected = "amount,description,date,checkNumber,payee\n"
				+ "12.12,description,2019-02-02,checkNumber,payee\n" + "34.21,description,2019-02-02,checkNumber,payee";
		assertEquals(expected, actual);
	}
}
