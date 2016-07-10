/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.File;
import java.io.IOException;
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
		        new ClearCheckbookConfig(SitConfig.getSitConfig()));
	}

	@Test
	public void testUploadToClearCheckbookCash() throws Exception {
		this.clearCheckbook.setAccount("Cash");
		this.uploadToClearcheckBook();

	}

	/**
	 * Test upload to clear checkbook.
	 *
	 * @throws Exception
	 */
	@Test
	public void testUploadToClearCheckbookChecking() throws Exception {
		this.clearCheckbook.setAccount("Checking");
		this.uploadToClearcheckBook();
	}

	public void uploadToClearcheckBook() throws IOException, Exception {
		final FileFormatter formatter = new QifFileFormatter(QifFileFormatter.CCB_FORMAT);
		final List<TransactionRecord> transactionRecords = new ArrayList<>();
		transactionRecords.add(new TransactionRecord(-12.23, "Payment",
		        DateUtils.stringToDate("2016/06/23"), "1", "Payee"));
		transactionRecords.add(new TransactionRecord(2.23, "Receipt",
		        DateUtils.stringToDate("2016/06/26"), "2", "Payee2"));
		final File tempDir = FileUtils.createTempDir();
		final String outputFileName = tempDir.getAbsolutePath() + "/output.qif";
		formatter.format(transactionRecords, outputFileName);
		final String results = this.clearCheckbook.uploadToClearCheckbook(outputFileName);
		System.out.println(results);
	}

}
