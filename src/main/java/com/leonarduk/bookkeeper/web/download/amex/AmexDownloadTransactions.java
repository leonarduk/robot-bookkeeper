/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.amex;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.StringConversionUtils;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.StatementDownloader;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class AmexDownloadTransactions.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 24 Mar 2015
 */
public class AmexDownloadTransactions implements TransactionDownloader, StatementDownloader,
        AutoCloseable, ValueSnapshotProvider {

	private static final Logger LOGGER = Logger.getLogger(AmexDownloadTransactions.class);

	private final AmexConfig config;

	/**
	 * Instantiates a new amex download transactions.
	 *
	 * @param config
	 *            the config
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public AmexDownloadTransactions(final AmexConfig config) throws IOException {
		final int fewSeconds = 3;
		this.config = config;
		this.config.getWebDriver().manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
	}

	@Override
	public void close() throws Exception {
		this.config.getWebDriver().close();
	}

	@Override
	public void downloadLatestStatement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<TransactionRecord> downloadTransactions() throws IOException {
		final QifFileParser parser = new QifFileParser();
		final String fileName = this.downloadTransactionsFile();
		BaseSeleniumPage.waitForPageToLoad(this.config.getWebDriver());
		return parser.parse(fileName);
	}

	/**
	 * Download transactions.
	 *
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public String downloadTransactionsFile() throws IOException {
		this.login();
		try {
			final WebElement popup = this.findElementByXpath("//*[@id=\"sprite-close_btn\"]");
			if (null != popup) {
				popup.click();
			}
		}
		catch (final NoSuchElementException e) {
			AmexDownloadTransactions.LOGGER.info("No pop up screen. Ignore and try next page", e);
		}

		this.config.getWebDriver().findElement(By.id("estatement-link")).click();
		this.config.getWebDriver().findElement(By.cssSelector("#downloads-link > span.copy"))
		        .click();
		this.config.getWebDriver()
		        .findElement(By.cssSelector(
		                "a[title=\"Export your statement data into a variety of file formats\"]"))
		        .click();
		this.config.getWebDriver().findElement(By.id("QIF")).click();
		this.config.getWebDriver().findElement(By.id("selectCard10")).click();
		this.config.getWebDriver().findElement(By.id("radioid00")).click();
		this.config.getWebDriver().findElement(By.id("radioid01")).click();
		this.config.getWebDriver().findElement(By.id("radioid02")).click();
		this.config.getWebDriver().findElement(By.id("radioid03")).click();
		this.config.getWebDriver().findElement(By.linkText("Download Now")).click();
		return this.config.getDownloadDir().getAbsolutePath() + "/ofx.qif";
	}

	protected final WebElement findElementByXpath(final String xpath) throws IOException {
		final WebElement findElement = this.config.getWebDriver().findElement(By.xpath(xpath));
		if (null == findElement) {
			throw new NoSuchElementException("Could not find xpath " + xpath);
		}
		return findElement;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.bookkeeper.ValueSnapshotProvider#getCurrentValue()
	 */
	@Override
	public double getCurrentValue() throws IOException {
		final String xpathToBalance = "//*[@id=\"card-balance\"]/h3/span[2]";
		final String overviewUrl = this.config.getAccountSummaryUrl();
		AmexDownloadTransactions.LOGGER.info("Get current balance from " + overviewUrl);
		final WebDriver webDriver = this.config.getWebDriver();
		webDriver.get(overviewUrl);
		final WebElement amountNode = webDriver.findElement(By.xpath(xpathToBalance));
		final String amountString = amountNode.getText();
		return StringConversionUtils.convertMoneyString(amountString);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.bookkeeper.ValueSnapshotProvider#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Account adjustment to reconcile with Amex site";
	}

	public void login() throws IOException {
		this.config.getWebDriver().get(this.config.getBaseUrl() + "/uk/");
		this.config.getWebDriver().findElement(By.id("LabelUserID")).click();
		this.config.getWebDriver().findElement(By.id("UserID")).clear();
		this.config.getWebDriver().findElement(By.id("UserID")).sendKeys(this.config.getUserName());
		this.config.getWebDriver().findElement(By.id("Password")).clear();
		this.config.getWebDriver().findElement(By.id("Password"))
		        .sendKeys(this.config.getPassword());
		this.config.getWebDriver().findElement(By.id("loginButton")).click();
	}

}
