/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.freeagent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.StringConversionUtils;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class FreeAgentLogin.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 4 Mar 2015
 */
public class FreeAgentLogin extends BaseSeleniumPage implements ValueSnapshotProvider {

	/** The Constant LOGGER. */
	private static final Logger		LOGGER	= Logger.getLogger(FreeAgentLogin.class);
	private final FreeAgentConfig	config;

	/**
	 * Instantiates a new free agent login.
	 *
	 * @param config
	 *            the config
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public FreeAgentLogin(final FreeAgentConfig config) throws IOException {
		super(config.getWebDriver(), config.getFreeagentUrl() + "/overview");
		this.config = config;
	}

	@Override
	public double getCurrentValue() throws IOException {
		this.load();

		return StringConversionUtils.convertMoneyString(this.getProfits());
	}

	@Override
	public String getDescription() {
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return "From Freeagent Website " + format.format(new Date());
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
		this.getWebDriver().get(this.config.getFreeagentUrl() + "/login");
		this.waitForPageToLoad();
		this.enterValueIntoField(this.config.getUsername(), "//*[@id=\"login_email\"]");
		this.enterValueIntoField(this.config.getPassword(), "//*[@id=\"login_password\"]");
		this.clickField("/html/body/div/div/form[1]/p[2]/input");
		try {
			this.waitForPageToLoad();
		}
		catch (final NoSuchElementException e) {
			FreeAgentLogin.LOGGER.info("No filter screen. Ignore and try next page", e);
		}
		try {
			final WebElement popup = this
			        .findElementByXpath("/html/body/div[2]/div[1]/button/span[2]");
			if (null != popup) {
				popup.click();
			}
		}
		catch (final NoSuchElementException e) {
			FreeAgentLogin.LOGGER.info("No pop up screen. Ignore and try next page", e);
		}

		this.waitForPageToLoad();
	}

}
