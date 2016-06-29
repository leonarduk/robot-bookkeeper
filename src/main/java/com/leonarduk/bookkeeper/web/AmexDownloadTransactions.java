/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.leonarduk.bookkeeper.web.clearcheckbook.ClearCheckbook;
import com.leonarduk.bookkeeper.web.clearcheckbook.ClearCheckbook.Setting;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

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

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Throwable
	 *             the throwable
	 */
	public static void main(final String[] args) throws Throwable {
		final String userName = "stevel56";
		final String password = "N0bigm0m";
		final File downloadDir = FileUtils.createTempDir();
		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(downloadDir);

		final AmexDownloadTransactions transactions = new AmexDownloadTransactions(webDriver);
		transactions.downloadTransactions(userName, password);

		final String ccbuserName = "stevel56";
		final String ccbpassword = "N0bigm0mas!";
		final String account = "CC - AMEX";
		final String fileToUpload = downloadDir.getAbsolutePath() + "/ofx.qif";

		final String results = ClearCheckbook.uploadToClearCheckbook(ccbuserName,
		        ccbpassword, account, fileToUpload, webDriver, Setting.AMEX);
		System.out.println(results);
		// transactions.finalize();

	}

	/**
	 * Instantiates a new amex download transactions.
	 *
	 * @param webDriver
	 *            the web driver
	 * @throws Exception
	 *             the exception
	 */
	public AmexDownloadTransactions(final WebDriver webDriver) throws Exception {
		this.driver = webDriver;
		this.baseUrl = "https://www.americanexpress.com/";
		final int fewSeconds = 3;
		this.driver.manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
	}

	/**
	 * Download transactions.
	 *
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @throws Exception
	 *             the exception
	 */
	public final void downloadTransactions(final String userName, final String password)
	        throws Exception {
		this.driver.get(this.baseUrl + "/uk/");
		this.driver.findElement(By.id("LabelUserID")).click();
		this.driver.findElement(By.id("UserID")).clear();
		this.driver.findElement(By.id("UserID")).sendKeys(userName);
		this.driver.findElement(By.id("Password")).clear();
		this.driver.findElement(By.id("Password")).sendKeys(password);
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
	}

}
