/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.amex;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

	/** The driver. */
	private final WebDriver driver;

	/** The base url. */
	private final String baseUrl;

	private final AmexConfig config;

	private final File downloadDir;

	/**
	 * Instantiates a new amex download transactions.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param config
	 * @param downloadDir
	 * @throws Exception
	 *             the exception
	 */
	public AmexDownloadTransactions(final WebDriver webDriver, final AmexConfig config,
	        final File downloadDir) throws Exception {
		this.driver = webDriver;
		this.baseUrl = "https://www.americanexpress.com/";
		final int fewSeconds = 3;
		this.config = config;
		this.downloadDir = downloadDir;
		this.driver.manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
	}

	public final List<TransactionRecord> downloadTransactions() throws Exception {
		final QifFileParser parser = new QifFileParser();
		final String fileName = this.downloadTransactionsFile();
		return parser.parse(fileName);
	}

	/**
	 * Download transactions.
	 *
	 * @return
	 *
	 * @throws Exception
	 *             the exception
	 */
	public final String downloadTransactionsFile() throws Exception {
		this.driver.get(this.baseUrl + "/uk/");
		this.driver.findElement(By.id("LabelUserID")).click();
		this.driver.findElement(By.id("UserID")).clear();
		this.driver.findElement(By.id("UserID")).sendKeys(this.config.getUserName());
		this.driver.findElement(By.id("Password")).clear();
		this.driver.findElement(By.id("Password")).sendKeys(this.config.getPassword());
		this.driver.findElement(By.id("loginButton")).click();
		this.driver.findElement(By.id("estatement-link")).click();
		this.driver.findElement(By.cssSelector("#downloads-link > span.copy")).click();
		this.driver
		        .findElement(By.cssSelector(
		                "a[title=\"Export your statement data into a variety of file formats\"]"))
		        .click();
		this.driver.findElement(By.id("QIF")).click();
		this.driver.findElement(By.id("selectCard10")).click();
		this.driver.findElement(By.id("radioid00")).click();
		this.driver.findElement(By.id("radioid01")).click();
		this.driver.findElement(By.id("radioid02")).click();
		this.driver.findElement(By.id("radioid03")).click();
		this.driver.findElement(By.linkText("Download Now")).click();
		return this.downloadDir.getAbsolutePath() + "/ofx.qif";
	}

}
