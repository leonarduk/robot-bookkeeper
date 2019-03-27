/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.freeagent;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.upload.TransactionUploader;
import com.leonarduk.web.BasePage;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class SantanderDownloadTransactions.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 6 Feb 2015
 */
public class FreeAgentUploadTransactions extends BaseSeleniumPage implements TransactionUploader {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(FreeAgentUploadTransactions.class);

	private final FreeAgentLogin loginPage;

	public static QifFileFormatter getQifFileFormatter() {
		return new QifFileFormatter(QifFileFormatter.FREEAGENT_FORMAT);

	}

	/**
	 * Instantiates a new santander download transactions.
	 *
	 * @param login the login
	 */
	public FreeAgentUploadTransactions(final FreeAgentLogin login) {
		super(login.getWebDriver(), login.getExpectedUrl());
		this.loginPage = login;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() {
		this.loginPage.get();
		try {
			Thread.sleep(BasePage.ONE_SECOND_IN_MS);
		} catch (final InterruptedException e) {
			FreeAgentUploadTransactions.LOGGER.info("Interrupted", e);
		}
	}

	@Override
	public final List<TransactionRecord> writeTransactions(final List<TransactionRecord> transactions,
			final String outputFileName, TransactionRecordFilter filter) throws IOException {
		FreeAgentUploadTransactions.getQifFileFormatter().format(transactions, outputFileName, filter);
		this.uploadTransactions(new FreeagentFile(outputFileName));
		return transactions;
	}

	public final void uploadTransactions(final FreeagentFile file) {
		this.getWebDriver().get("https://leonarduk.freeagent.com/bank_accounts/2234/upload/new");
		this.waitForPageToLoad();
		final WebElement element = this.getWebDriver().findElement(By.name("upload[attachment]"));
		element.sendKeys(file.getFreeagentFile().getAbsolutePath());
		// *[@id="upload_statement_form"]/p[3]/input
		this.waitForPageToLoad();
		this.getWebDriver().findElement(By.name("commit")).click();
	}
}
