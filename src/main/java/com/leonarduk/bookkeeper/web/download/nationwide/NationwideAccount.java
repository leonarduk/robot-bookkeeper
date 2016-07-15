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

import com.leonarduk.bookkeeper.file.NationwideCsvFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.bookkeeper.web.download.nationwide.NationwideAccount.FileType;
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
public class NationwideAccount extends BaseSeleniumPage implements TransactionDownloader {

	/** The Constant _logger. */
	private static final Logger _logger = Logger.getLogger(NationwideAccount.class);

	/** The login. */
	private final NationwideLogin login;

	/** The account id. */
	private final int accountId;

	/**
	 * Instantiates a new nationwide account.
	 *
	 * @param aLogin
	 *            the a login
	 * @param aAccountId
	 *            the a account id
	 */
	public NationwideAccount(final NationwideLogin aLogin, final int aAccountId) {
		super(aLogin.getWebDriver(), aLogin.getConfig().getAccountListUrl()); // getFullStatementUrl(aAccountId));
		this.login = aLogin;
		this.accountId = aAccountId;
	}

	/**
	 * Account name.
	 *
	 * @return the string
	 */
	public final String accountName() {
		this.getWebDriver().get(this.login.getConfig().getFullStatementUrl(this.accountId));
		return this.getWebDriver().findElement(By.xpath("//*[@id=\"stageInner\"]/div[3]/h2"))
		        .getText();
	}

	@Override
	public void close() throws Exception {
		this.waitForPageToLoad();
		// this.getWebDriver().findElement(By.xpath("//*[@id=\"logoutForm\"]/a/b")).click();
		// this.getWebDriver().findElement(By.xpath("//*[@id=\"lbBtnYes\"]/i")).click();

		super.close();
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
		this.downloadTransactionsFile();
		final File[] files = this.login.getConfig().getDownloadDir().listFiles();
		if (files.length > 0) {
			final NationwideCsvFileParser parser = new NationwideCsvFileParser();
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
		this.getWebDriver().get(this.login.getConfig().getFullStatementUrl(this.accountId));
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

		final FileType fileType = NationwideAccount.FileType.CSV;
		downloadLinks.get(0).click();
		this.getWebDriver()
		        .findElement(By.xpath("(//form[@action='/Transactions/FullStatement/DownloadFS'])["
		                + FileType.CSV.getIndex() + "]"))
		        .click();
		this.getWebDriver().findElement(By.cssSelector("b.reveal-info-down")).click();
		try {
			this.getWebDriver().findElement(By.linkText("Download " + fileType.name() + " file"))
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
		CSV(1), /** The ofx. */
		OFX(2);

		private int index;

		FileType(final int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		};

	}
}
