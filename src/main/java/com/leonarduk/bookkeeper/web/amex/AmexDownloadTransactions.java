/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.amex;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.TransactionRecord;

/**
 * The Class AmexDownloadTransactions.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 24 Mar 2015
 */
public class AmexDownloadTransactions {

	private static final Logger LOGGER = Logger.getLogger(AmexDownloadTransactions.class);

	private final AmexConfig config;

	/**
	 * Instantiates a new amex download transactions.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param config
	 *            the config
	 * @param downloadDir
	 *            the download dir
	 * @throws Exception
	 *             the exception
	 */
	public AmexDownloadTransactions(final AmexConfig config, final File downloadDir)
	        throws Exception {
		final int fewSeconds = 3;
		this.config = config;
		this.config.getWebDriver().manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
	}

	public final List<TransactionRecord> downloadTransactions() throws Exception {
		final QifFileParser parser = new QifFileParser();
		final String fileName = this.downloadTransactionsFile();
		return parser.parse(fileName);
	}

	/**
	 * Download transactions.
	 *
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public final String downloadTransactionsFile() throws Exception {
		this.config.getWebDriver().get(this.config.getBaseUrl() + "/uk/");
		this.config.getWebDriver().findElement(By.id("LabelUserID")).click();
		this.config.getWebDriver().findElement(By.id("UserID")).clear();
		this.config.getWebDriver().findElement(By.id("UserID")).sendKeys(this.config.getUserName());
		this.config.getWebDriver().findElement(By.id("Password")).clear();
		this.config.getWebDriver().findElement(By.id("Password"))
		        .sendKeys(this.config.getPassword());
		this.config.getWebDriver().findElement(By.id("loginButton")).click();
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

	protected final WebElement findElementByXpath(final String xpath) {
		final WebElement findElement = this.config.getWebDriver().findElement(By.xpath(xpath));
		if (null == findElement) {
			throw new NoSuchElementException("Could not find xpath " + xpath);
		}
		return findElement;

	}

}
