/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;

/**
 * The Class FreeAgentLogin.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 4 Mar 2015
 */
public class FreeAgentLogin extends BaseSeleniumPage {

	/** The Constant LOGGER. */
	static final Logger LOGGER = Logger.getLogger(FreeAgentLogin.class);

	/** The login url. */
	private final String loginUrl;

	/** The password. */
	private final String password;

	/** The user name. */
	private final String userName;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String[] args) throws Exception {
		final Config config = new Config("bookkeeper.properties");
		final File tempDir = FileUtils.createTempDir();

		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);

		final FreeAgentLogin freeAgentLogin = new FreeAgentLogin(webDriver, config);
		freeAgentLogin.get();

	}

	/**
	 * Instantiates a new free agent login.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param config
	 *            the config
	 */
	public FreeAgentLogin(final WebDriver webDriver, final Config config) {
		super(webDriver, "https://leonarduk.freeagentcentral.com/overview");
		this.loginUrl = "https://leonarduk.freeagentcentral.com/login";
		this.userName = "accounts@leonarduk.com";
		this.password = "N0bigm0mas!";

		FreeAgentLogin.LOGGER.info("URL: " + this.loginUrl);
		FreeAgentLogin.LOGGER.info("ID: " + this.userName);

		FreeAgentLogin.LOGGER.info("Password: " + this.password);

	}

	/**
	 * Gets the profits.
	 *
	 * @return the profits
	 */
	public String getProfits() {
		final WebElement profits = this.getWebDriver()
		        .findElement(By.id("profit_and_loss_carried_forward"));
		return profits.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() {
		this.getWebDriver().get(this.loginUrl);
		this.waitForPageToLoad();
		this.enterValueIntoField(this.userName, "//*[@id=\"email\"]");
		this.enterValueIntoField(this.password, "//*[@id=\"password\"]");
		this.clickField("/html/body/div/div/form[1]/p[2]/input");
		try {
			this.waitForPageToLoad();
		}
		catch (final NoSuchElementException e) {
			FreeAgentLogin.LOGGER.info("No filter screen. Ignore and try next page");
		}
		this.waitForPageToLoad();
	}

}
