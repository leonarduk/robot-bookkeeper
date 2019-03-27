package com.leonarduk.bookkeeper.file;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.leonarduk.webscraper.core.FileUtils;

public class TransactionFileReaderTest {

	private TransactionFileReader reader;
	private File tempFile;

	@Before
	public void setUp() throws Exception {
		File tempDir = FileUtils.createTempDir();
		tempFile = File.createTempFile("data", "csv", tempDir);
		reader = new TransactionFileReader(tempFile);
	}

	@Test
	public void testSaveTransactions() throws IOException, ParseException {
		String records = "amount,description,date,checkNumber,payee\n"
				+ "12.12,description,2019-02-02,checkNumber,payee\n" + "34.21,description,2019-02-02,checkNumber,payee";
		TransactionRecordFilter filter = (record) -> true;
		FileUtils.writeStringToFile(tempFile.getAbsolutePath(), records);
		List<TransactionRecord> expected = ImmutableList.of(
				new TransactionRecord(12.12, "description", DateUtils.parse("2019-02-02"), "checkNumber", "payee"),
				new TransactionRecord(34.21, "description", DateUtils.parse("2019-02-02"), "checkNumber", "payee"));

		List<TransactionRecord> actual = this.reader.saveTransactions(filter);
		assertEquals(expected, actual);
	}

	@Test
	public void testDownloadTransactionsFile() throws IOException {
		assertEquals(tempFile.getAbsolutePath(), this.reader.downloadTransactionsFile());
	}

}
