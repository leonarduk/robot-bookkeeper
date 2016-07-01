/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.nationwide;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class NationwideLogin.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 28 Mar 2015
 */
public class NationwideLogin extends BaseSeleniumPage {

	/** The Constant _logger. */
	private static final Logger		_logger	= Logger.getLogger(NationwideLogin.class);
	private final NationwideConfig	config;

	public NationwideLogin(final NationwideConfig nationwideConfig) {
		super(nationwideConfig.getWebDriver(), nationwideConfig.getAccountListUrl());
		this.config = nationwideConfig;
	}

	/**
	 * Gets the required character.
	 *
	 * @param index
	 *            the index
	 * @return the required character
	 */
	private String getRequiredCharacter(final int index) {
		final WebElement element = this.getWebDriver()
		        .findElement(By.xpath("//*[@id=\"memDataForm\"]/div[2]/div[" + index + "]/label"));

		final String text = element.getText();
		final int value = Integer.valueOf(text.substring(0, 1)).intValue();
		final String character = this.config.getPassword().substring(value - 1, value);
		NationwideLogin._logger.info(text + " " + value + " " + character);

		return character;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() {
		final int fewSeconds = 3;
		this.getWebDriver().manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);

		if (this.getWebDriver().findElements(By.id("logoutForm")).size() > 0) {
			return;
		}

		this.getWebDriver().get(this.config.getLoginUrl());
		try {
			final int halfSecond = 500;
			Thread.sleep(halfSecond);
		}
		catch (final InterruptedException e) {
		}
		this.getWebDriver().findElement(By.id("CustomerNumber"))
		        .sendKeys(this.config.getCustomerNumber());
		this.getWebDriver().findElement(By.id("Continue")).click();

		this.getWebDriver().findElement(By.xpath("//*[@id=\"logInWithMemDataLink\"]/div/b"))
		        .click();
		this.getWebDriver().findElement(By.id("SubmittedMemorableInformation"))
		        .sendKeys(this.config.getMemorableWord());

		this.getWebDriver().findElement(By.id("firstSelect"))
		        .sendKeys(this.getRequiredCharacter(1));
		this.getWebDriver().findElement(By.id("secondSelect"))
		        .sendKeys(this.getRequiredCharacter(2));
		this.getWebDriver().findElement(By.id("thirdSelect"))
		        .sendKeys(this.getRequiredCharacter(fewSeconds));

		this.getWebDriver().findElement(By.xpath("//*[@id=\"Continue\"]/i")).click();
		if (this.getWebDriver().getCurrentUrl().equals(
		        "https://onlinebanking.nationwide.co.uk/Campaigns/PresentPrompt/DisplayInterstitialPrompt")) {
			this.findElementByXpath("//*[@id=\"interstitialContinueButton\"]/i").click();
		}

	}
}
