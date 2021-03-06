/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.alliancetrust;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.StringConversionUtils;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class AllianceTrust.
 */
public class AllianceTrust extends BaseSeleniumPage implements ValueSnapshotProvider {

	private final AllianceTrustConfig	config;
	private final int					accountIndex;
	private final int					accountNumber;

	public AllianceTrust(final AllianceTrustConfig config, final int accountIndex,
	        final int accountNumber) throws IOException {
		super(config.getWebDriver(), config.getCustomerListViewUrl());
		this.config = config;
		this.accountIndex = accountIndex;
		this.accountNumber = accountNumber;
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
			final int index = Integer.valueOf(indexElement.getText().replaceAll("\\D+", ""))
			        .intValue();

			this.getWebDriver().findElement(By.id("ppd" + (i))).clear();
			final String character = password.substring(index - 1, index);
			this.getWebDriver().findElement(By.id("ppd" + (i))).sendKeys(character);
		}
		this.getWebDriver().findElement(By.xpath("//input[@value='Continue >']")).click();
	}

	@Override
	public double getCurrentValue() throws IOException {
		this.load();

		this.waitForPageToLoad(By.linkText(String.valueOf(this.accountNumber))).click();

		final String xpath = "//*[@id=\"hor-minimalist-b\"]/tbody/tr[" + (this.accountIndex + 1)
		        + "]/td[5]";

		final String value = this.waitForPageToLoad(By.xpath(xpath)).getAttribute("textContent")
		        .trim();

		this.waitForPageToLoad(By.linkText("Home")).click();

		return StringConversionUtils.convertMoneyString(value);
	}

	@Override
	public String getDescription() {
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return "From AT Website " + format.format(new Date());
	}

	/**
	 * Gets the value.
	 *
	 * @param accountIndex
	 *            the account index
	 * @return the value
	 */
	public final String getValue(final int accountIndex) {
		final String account = this.config.getAccountNumber(accountIndex);
		// TODO get from config
		final int row = 1;

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
		this.getWebDriver().findElement(By.id("pid")).clear();
		this.getWebDriver().findElement(By.id("pid")).sendKeys(this.config.getAccountNumber());
		this.getWebDriver().findElement(By.cssSelector("input[type=\"image\"]")).click();
		this.enterPassword(this.config.getPassword());
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
