/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.DateUtils;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

/**
 * The Class UploadToClearCheckbookTest.
 */
public class ClearCheckbookTransactionUploaderIT {

	private static boolean						internetAvailable;
	private ClearCheckbookTransactionUploader	clearCheckbook;

	@BeforeClass
	public static void setupStatic() {
		ClearCheckbookTransactionUploaderIT.internetAvailable = SeleniumUtils.isInternetAvailable();
	}

	@Before
	public void setup() throws IOException {
		this.clearCheckbook = new ClearCheckbookTransactionUploader(
		        new ClearCheckbookConfig(SitConfig.getSitConfig()), "Cash");
	}

	@Test
	public void testUploadToClearCheckbookCash() throws Exception {
		this.uploadToClearcheckBook();

	}

	/**
	 * Test upload to clear checkbook.
	 *
	 * @throws Exception
	 */
	@Test
	public void testUploadToClearCheckbookChecking() throws Exception {
		this.clearCheckbook = new ClearCheckbookTransactionUploader(
		        new ClearCheckbookConfig(SitConfig.getSitConfig()), "Checking");

		this.uploadToClearcheckBook();
	}

	public void uploadToClearcheckBook() throws IOException, Exception {
		final FileFormatter formatter = new QifFileFormatter(QifFileFormatter.CCB_FORMAT);
		final List<TransactionRecord> transactionRecords = new ArrayList<>();
		transactionRecords.add(new TransactionRecord(-12.23, "Payment",
		        DateUtils.parse("2016/06/23"), "1", "Payee"));
		transactionRecords.add(new TransactionRecord(2.23, "Receipt",
				DateUtils.parse("2016/06/26"), "2", "Payee2"));
		final File tempDir = FileUtils.createTempDir();
		final String outputFileName = tempDir.getAbsolutePath() + "/output.qif";
		TransactionRecordFilter filter = (record) -> true; 
		formatter.format(transactionRecords, outputFileName, filter);
		final String results = this.clearCheckbook.uploadToClearCheckbook(outputFileName);
		System.out.println(results);
	}

}
