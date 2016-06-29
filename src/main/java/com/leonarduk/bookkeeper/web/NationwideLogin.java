/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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
	private static final Logger _logger = Logger.getLogger(NationwideLogin.class);

	/** The customer number. */
	private final String customerNumber;

	/** The password. */
	private final String password;

	/** The memorable word. */
	private final String memorableWord;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		// Steve
		final String customerNumber2 = "2901722608";
		final String memorableword2 = "olympia";
		final String password2 = "971659";

		new NationwideLogin(new ChromeDriver(), customerNumber2, memorableword2, password2).get();

	}

	/**
	 * Instantiates a new nationwide login.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param aCustomerNumber
	 *            the customer number
	 * @param aMemorableword
	 *            the memorableword
	 * @param aPassword
	 *            the password
	 */
	public NationwideLogin(final WebDriver webDriver, final String aCustomerNumber,
	        final String aMemorableword, final String aPassword) {
		super(webDriver, "https://onlinebanking.nationwide.co.uk/AccountList");
		this.customerNumber = aCustomerNumber;
		this.password = aPassword;
		this.memorableWord = aMemorableword;
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
		final int value = Integer.valueOf(text.substring(0, 1));
		final String character = this.password.substring(value - 1, value);
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

		this.getWebDriver().get("https://onlinebanking.nationwide.co.uk/AccessManagement/Login");
		try {
			final int halfSecond = 500;
			Thread.sleep(halfSecond);
		}
		catch (final InterruptedException e) {
		}
		this.getWebDriver().findElement(By.id("CustomerNumber")).sendKeys(this.customerNumber);
		this.getWebDriver().findElement(By.id("Continue")).click();

		this.getWebDriver().findElement(By.xpath("//*[@id=\"logInWithMemDataLink\"]/div/b"))
		        .click();
		this.getWebDriver().findElement(By.id("SubmittedMemorableInformation"))
		        .sendKeys(this.memorableWord);

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
