/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
public class FreeAgentUploadTransactions extends BaseSeleniumPage {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(FreeAgentUploadTransactions.class);

	/** The login. */
	private final BaseSeleniumPage loginPage;

	/**
	 * Instantiates a new santander download transactions.
	 *
	 * @param login
	 *            the login
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
			Thread.sleep(BaseSeleniumPage.ONE_SECOND_IN_MS);
		}
		catch (final InterruptedException e) {
			FreeAgentUploadTransactions.LOGGER.info("Interrupted", e);
		}
	}

	/**
	 * Upload transactions.
	 *
	 * @param fileName
	 *            the file name
	 */
	public final void uploadTransactions(final String fileName) {
		this.clickField("//*[@id=\"primary_nav\"]/ul/li[6]/a");
		this.waitForPageToLoad();

		this.clickField("//*[@id=\"title_actions\"]/ul/li[1]/a/span/em");
		this.waitForPageToLoad();

		final File file = new File(fileName);
		final WebElement element = this.getWebDriver().findElement(By.name("upload[attachment]"));
		element.sendKeys(file.getAbsolutePath());
		// *[@id="upload_statement_form"]/p[3]/input
		this.waitForPageToLoad();
		this.getWebDriver().findElement(By.name("commit")).click();
	}

}
