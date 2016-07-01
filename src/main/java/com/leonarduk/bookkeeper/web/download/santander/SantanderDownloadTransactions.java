/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.santander;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.StatementDownloader;
import com.leonarduk.bookkeeper.web.download.TransactionsDownloader;
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
public class SantanderDownloadTransactions extends BaseSeleniumPage
        implements TransactionsDownloader, StatementDownloader {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(SantanderDownloadTransactions.class);

	/** The login. */
	private final SantanderLogin loginPage;

	/**
	 * Instantiates a new santander download transactions.
	 *
	 * @param login
	 *            the login
	 */
	public SantanderDownloadTransactions(final SantanderLogin login) {
		super(login.getWebDriver(), login.getExpectedUrl());
		this.loginPage = login;
	}

	/**
	 * Click e documents.
	 */
	private void clickEDocuments() {
		this.findElementByXpath(MyAccountsMenu.EDocuments.url()).click();
	}

	/**
	 * Download latest statement.
	 */
	@Override
	public final void downloadLatestStatement() {
		this.isLoaded();
		this.clickEDocuments();
		this.waitForPageToLoad();
		this.downloadStatement(0);
		this.waitForPageToLoad();
	}

	/**
	 * Download statement.
	 *
	 * @param index
	 *            the index
	 */
	private void downloadStatement(final int index) {
		this.getWebDriver().switchTo().frame(0);
		this.findElementByXpath("//*[@id=\"" + index + "\"]").click();
	}

	@Override
	public final List<TransactionRecord> downloadTransactions() throws IOException {
		final QifFileParser parser = new QifFileParser();
		final String fileName = this.downloadTransactionsFile();
		if (fileName == null) {
			return new ArrayList<>();
		}
		return parser.parse(fileName);
	}

	/**
	 * Download transactions.
	 *
	 * @return the string
	 */
	@Override
	public final String downloadTransactionsFile() {
		// select transactions
		final String xpath = "//*[@id=\"submenu\"]/ul/li[2]/a";
		this.findElementByXpath(xpath).click();
		this.waitForPageToLoad();
		// select download transactions
		this.findElementByXpath("//*[@id=\"content\"]/p[4]/a").click();
		this.waitForPageToLoad();
		// select all since last download
		this.findElementByXpath("//*[@id=\"AllorLastL\"]").click();
		// select 1 - XLS ; 2 - QIF ; 3 - QIF ; 4 - PDF ; 5 - TXT
		this.findElementByXpath("//*[@id=\"sel_downloadto\"]/option[2]").click();
		// click Download
		this.findElementByXpath(
		        "//*[@id=\"content\"]/div[2]/div/form/fieldset/div[2]/span[1]/input").click();
		this.waitForPageToLoad();

		String file = null;
		final File tempDir = this.loginPage.getConfig().getDownloadDir();
		if (tempDir.list().length > 0) {
			SantanderDownloadTransactions.LOGGER.debug("uploadSantanderToFreeAgent");
			file = tempDir + "/" + tempDir.list()[0];
			System.out.println("File " + file);
		}
		return file;
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
			Thread.sleep(BaseSeleniumPage.ONE_SECOND_IN_MS);
		}
		catch (final InterruptedException e) {
			SantanderDownloadTransactions.LOGGER.info("Interrupted", e);
		}
	}

}
