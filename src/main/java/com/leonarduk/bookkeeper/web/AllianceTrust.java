/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.bookkeeper.web.clearcheckbook.UploadToClearCheckbook;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class AllianceTrust.
 */
public class AllianceTrust extends BaseSeleniumPage {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		final WebDriver webDriver = new FirefoxDriver();
		final WebDriver freeAgentWebdriver = new FirefoxDriver();
		final String ccbuserName = "stevel56";
		final String ccbpassword = "";
		final String steveaccount = "155266";
		final String lucyAccount = "155385";

		final AllianceTrust trust = new AllianceTrust(webDriver);
		trust.get();

		final String pensionValue = trust.getValue(1, steveaccount);
		System.out.println("Pension:" + pensionValue);

		final String steveLisaValue = trust.getValue(2, steveaccount);
		System.out.println("Steve ISA:" + steveLisaValue);

		final String lucyIsaValue = trust.getValue(1, lucyAccount);
		System.out.println("Lucy ISA:" + lucyIsaValue);
		UploadToClearCheckbook.updateEstimate("AT SIPP (StockMarket)", pensionValue, ccbuserName,
		        ccbpassword, freeAgentWebdriver, "//*[@id=\"account-overviews\"]/div[3]/div[3]",
		        "Updated from Alliance Trust");
		UploadToClearCheckbook.updateEstimate("AT ISA Steve (StockMarket)", steveLisaValue,
		        ccbuserName, ccbpassword, freeAgentWebdriver,
		        "//*[@id=\"account-overviews\"]/div[2]/div[3]", "Updated from Alliance Trust");
		UploadToClearCheckbook.updateEstimate("AT ISA Lucy (StockMarket)", lucyIsaValue,
		        ccbuserName, ccbpassword, freeAgentWebdriver,
		        "//*[@id=\"account-overviews\"]/div[1]/div[3]", "Updated from Alliance Trust");

	}

	/**
	 * Instantiates a new alliance trust.
	 *
	 * @param webDriver
	 *            the web driver
	 */
	public AllianceTrust(final WebDriver webDriver) {
		super(webDriver,
		        // "https://atonline.alliancetrust.co.uk/atonline/index.jsp");
		        "https://atonline.alliancetrust.co.uk/atonline/secure/CustomerListView.action");
	}

	/**
	 * Enter password.
	 *
	 * @param password
	 *            the password
	 */
	public final void enterPassword(final String password) {
		final int numberOfDigitsToEnter = 3;
		for (int i = 0; i < numberOfDigitsToEnter; i++) {
			final String xpathExpression = "//*[@id=\"login-r\"]/form/table/tbody/tr[1]/td["
			        + (i + 1) + "]";

			final WebElement indexElement = this.getWebDriver()
			        .findElement(By.xpath(xpathExpression));
			final int index = Integer.valueOf(indexElement.getText().replaceAll("\\D+", ""));

			this.getWebDriver().findElement(By.id("ppd" + (i))).clear();
			final String character = password.substring(index - 1, index);
			this.getWebDriver().findElement(By.id("ppd" + (i))).sendKeys(character);
		}
		this.getWebDriver().findElement(By.xpath("//input[@value='Continue >']")).click();
	}

	/**
	 * Gets the value.
	 *
	 * @param row
	 *            the row
	 * @param account
	 *            the account
	 * @return the value
	 */
	public final String getValue(final int row, final String account) {
		this.waitForPageToLoad(By.linkText(account)).click();

		final String xpath = "//*[@id=\"hor-minimalist-b\"]/tbody/tr[" + (row + 1) + "]/td[5]";

		final String value = this.waitForPageToLoad(By.xpath(xpath)).getAttribute("textContent")
		        .trim();

		this.waitForPageToLoad(By.linkText("Home")).click();

		return value;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	public final void load() {
		this.getWebDriver().get("https://atonline.alliancetrust.co.uk/atonline/login.jsp");
		final String accountNumber = "84871967";
		final String password = "";

		this.getWebDriver().findElement(By.id("pid")).clear();
		this.getWebDriver().findElement(By.id("pid")).sendKeys(accountNumber);
		this.getWebDriver().findElement(By.cssSelector("input[type=\"image\"]")).click();
		this.enterPassword(password);
		this.waitForPageToLoad();

	}

	/**
	 * Wait for page to load.
	 *
	 * @param by
	 *            the by
	 * @return the web element
	 */
	private WebElement waitForPageToLoad(final By by) {
		while (!this.isElementPresent(by)) {
			this.waitForPageToLoad();
		}
		return this.getWebDriver().findElement(by);
	}

}
