/**
 * ClearCheckbookTransactionUploaderTest
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;

public class ClearCheckbookTransactionUploaderTest {

	private ClearCheckbookConfig				config;
	private ClearCheckbookTransactionUploader	uploader;
	private WebDriver							driver;

	@Before
	public void setUp() throws Exception {
		this.config = Mockito.mock(ClearCheckbookConfig.class);
		this.driver = Mockito.mock(WebDriver.class);
		final Options options = Mockito.mock(Options.class);
		Mockito.when(this.driver.manage()).thenReturn(options);
		final Timeouts timeouts = Mockito.mock(Timeouts.class);
		Mockito.when(options.timeouts()).thenReturn(timeouts);
		Mockito.when(this.config.getWebDriver()).thenReturn(this.driver);
		this.uploader = new ClearCheckbookTransactionUploader(this.config);
	}

	@Test
	public final void testGetQifFileFormatter() {
		final QifFileFormatter actual = this.uploader.getQifFileFormatter();
		Assert.assertNotNull(actual);
	}

	@Ignore
	@Test
	public void testUploadTransactions() throws Exception {
		final WebElement toolsPage = Mockito.mock(WebElement.class);
		final WebElement importPage = Mockito.mock(WebElement.class);
		final WebElement importLink = Mockito.mock(WebElement.class);
		final WebElement importToAccountLink = Mockito.mock(WebElement.class);
		final String accountName = "testAccount testAccount2 ";
		this.uploader.setAccount(accountName);
		Mockito.when(importToAccountLink.getTagName()).thenReturn("select");
		Mockito.when(importToAccountLink.getText()).thenReturn(accountName);

		final WebElement uploadButton = Mockito.mock(WebElement.class);
		Mockito.when(this.driver.findElement(ClearCheckbookTransactionUploader.TOOLS_PAGE))
		        .thenReturn(toolsPage);
		Mockito.when(this.driver.findElement(ClearCheckbookTransactionUploader.IMPORT_PAGE))
		        .thenReturn(importPage);
		Mockito.when(this.driver.findElement(ClearCheckbookTransactionUploader.IMPORT_LINK))
		        .thenReturn(importLink);
		Mockito.when(
		        this.driver.findElement(ClearCheckbookTransactionUploader.IMPORT_TO_ACCOUNT_LINK))
		        .thenReturn(importToAccountLink);
		Mockito.when(this.driver.findElement(ClearCheckbookTransactionUploader.UPLOAD_BUTTON))
		        .thenReturn(uploadButton);

		final List<TransactionRecord> transactions = new ArrayList<>();
		transactions
		        .add(new TransactionRecord(12.23, "Test payment", new Date(), "123", "Test payee"));
		this.uploader.uploadTransactions(transactions);
	}
}
