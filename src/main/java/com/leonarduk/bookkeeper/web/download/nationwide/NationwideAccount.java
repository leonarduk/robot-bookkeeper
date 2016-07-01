/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.nationwide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.TransactionsDownloader;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class NationwideAccount.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 28 Mar 2015
 */
public class NationwideAccount extends BaseSeleniumPage implements TransactionsDownloader {

	private static final String OFX_FORMAT = "2";

	/** The Constant _logger. */
	private static final Logger _logger = Logger.getLogger(NationwideAccount.class);

	private static final String CSV_FORMAT = "1";

	/** The login. */
	private final NationwideLogin login;

	/** The file type. */
	private final FileType fileType;

	/**
	 * Instantiates a new nationwide account.
	 *
	 * @param aLogin
	 *            the login
	 * @param aAccountId
	 *            the account id
	 * @param aFileType
	 *            the file type
	 */
	public NationwideAccount(final NationwideLogin aLogin, final int aAccountId,
	        final FileType aFileType) {
		super(aLogin.getWebDriver(), aLogin.getConfig().getAccountListUrl()); // getFullStatementUrl(aAccountId));
		this.login = aLogin;
		this.fileType = aFileType;
	}

	/**
	 * Account name.
	 *
	 * @return the string
	 */
	public final String accountName() {
		this.get();
		return this.getWebDriver().findElement(By.xpath("//*[@id=\"stageInner\"]/div[3]/h2"))
		        .getText();
	}

	/**
	 * Creates the file name.
	 *
	 * @return the string
	 */
	public final String createFileName() {
		final StringBuilder buf = new StringBuilder();
		buf.append(this.accountName().replaceAll(" ", "_"));
		buf.append("_");
		buf.append(this.getDates().replaceAll(" ", "_"));
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.bookkeeper.web.download.TransactionsDownloader#downloadTransactions()
	 */
	@Override
	public List<TransactionRecord> downloadTransactions() throws IOException {
		final File[] files = this.login.getConfig().getDownloadDir().listFiles();
		if (files.length > 0) {
			final QifFileParser parser = new QifFileParser();
			return parser.parse(files[0].getAbsolutePath());
		}
		return new ArrayList<>();
	}

	/**
	 * Download for account.
	 *
	 * @return the string
	 */
	@Override
	public String downloadTransactionsFile() {
		this.getWebDriver().get(this.getExpectedUrl());
		try {
			this.getWebDriver().switchTo().alert().accept();
		}
		catch (final NoAlertPresentException e) {
			NationwideAccount._logger.info("no alert to close");
		}
		final List<WebElement> downloadLinks = this.getWebDriver()
		        .findElements(By.cssSelector("a.downloadFileLink.custom-tooltip-link"));
		if ((downloadLinks.size() < 1) || !downloadLinks.get(0).isDisplayed()) {
			return null;
		}

		downloadLinks.get(0).click();
		this.getWebDriver()
		        .findElement(By.xpath("(//form[@action='/Transactions/FullStatement/DownloadFS'])["
		                + NationwideAccount.CSV_FORMAT + "]"))
		        .click();
		this.getWebDriver().findElement(By.cssSelector("b.reveal-info-down")).click();
		try {
			this.getWebDriver().findElement(By.linkText("Download " + this.fileType + " file"))
			        .click();
		}
		catch (final NoSuchElementException e) {
			NationwideAccount._logger.info("no data");
			// Ignore as this means there is no data
		}
		return null;
	}

	/**
	 * Gets the dates.
	 *
	 * @return the dates
	 */
	public final String getDates() {
		this.get();
		return this.getWebDriver().findElement(By.id("date-display-dates")).getText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() {
		if (this.getWebDriver().findElements(By.id("logoutForm")).size() == 0) {
			this.login.get();
		}
	}

	/**
	 * The Enum FileType.
	 */
	public enum FileType {

		/** The csv. */
		CSV, /** The ofx. */
		OFX;

	}
}
