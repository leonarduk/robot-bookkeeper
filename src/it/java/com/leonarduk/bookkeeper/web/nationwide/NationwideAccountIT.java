/**
 * NationwideAccountIT
 *
 * @author ${author}
 * @since 30-Jun-2016
 */
package com.leonarduk.bookkeeper.web.nationwide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.file.CsvFormatter;
import com.leonarduk.bookkeeper.file.FileFormatter;
import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.nationwide.NationwideAccount.FileType;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

public class NationwideAccountIT {

	@Test
	public final void testAccountName() throws IOException {
		final File tempDir = FileUtils.createTempDir();
		final WebDriver downloadCapableBrowser = SeleniumUtils.getDownloadCapableBrowser(tempDir);
		final FileType type = FileType.OFX;
		final NationwideAccount nationwideAccount = (NationwideAccount) new NationwideAccount(
		        new NationwideLogin(downloadCapableBrowser,
		                new NationwideConfig(new Config("bookkeeper-sit.properties"))),
		        1, type).get();
		final File[] files = tempDir.listFiles();
		if (files.length > 0) {
			final QifFileParser parser = new QifFileParser();
			final List<TransactionRecord> records = parser.parse(files[0].getAbsolutePath());
			final FileFormatter formatter = new CsvFormatter();
			final String outputFileName = "test.csv";
			formatter.format(records, outputFileName);
			System.out.println(FileUtils.getFileContents(outputFileName));
		}
		downloadCapableBrowser.close();
	}

}
