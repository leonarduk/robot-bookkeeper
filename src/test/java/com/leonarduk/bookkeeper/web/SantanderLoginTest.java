/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.leonarduk.webscraper.core.config.Config;

/**
 * The Class SantanderLoginTest.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 6 Feb 2015
 */
public class SantanderLoginTest {

	/** The login. */
	private SantanderLogin login;

	/** The web driver. */
	private WebDriver webDriver;

	/** The config. */
	private Config config;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public final void setUp() throws Exception {
		this.webDriver = Mockito.mock(WebDriver.class);
		this.config = new Config("bookkeeper.properties");

		this.login = new SantanderLogin(this.webDriver, this.config);
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
		// this.login.getWebDriver().close();
	}

	/**
	 * Test is loaded.
	 */
	@Test
	public final void testIsLoaded() {
		final String url = this.config.getProperty("bookkeeper.web.santander.url.accounts");
		Mockito.when(this.webDriver.getCurrentUrl()).thenReturn(url);
		final WebElement idSubmit = Mockito.mock(WebElement.class);
		final WebElement questionNode = idSubmit;
		final String questionText = SantanderLogin.IN_WHAT_CITY_OR_TOWN_WERE_YOU_BORN;
		Mockito.when(questionNode.getText()).thenReturn(questionText);
		Mockito.when(questionNode.getAttribute(Matchers.anyString())).thenReturn("1");
		Mockito.when(this.webDriver.findElement(By.xpath(SantanderLogin.QUESTION_XPATH)))
		        .thenReturn(questionNode);
		final WebElement numberNode = idSubmit;
		Mockito.when(this.webDriver.findElement(By.xpath(SantanderLogin.CUSTOMER_ID_XPATH)))
		        .thenReturn(numberNode);

		Mockito.when(this.webDriver.findElement(By.xpath(SantanderLogin.CUSTOMER_ID_SUBMIT_XPATH)))
		        .thenReturn(idSubmit);

		final WebElement answerField = Mockito.mock(WebElement.class);
		Mockito.when(this.webDriver.findElement(By.xpath(SantanderLogin.ANSWER_XPATH)))
		        .thenReturn(answerField);

		final WebElement answerSubmitField = Mockito.mock(WebElement.class);
		Mockito.when(this.webDriver.findElement(By.xpath(SantanderLogin.ANSWER_SUBMIT_XPATH)))
		        .thenReturn(answerSubmitField);
		final int numberOfDigits = 3;
		for (int i = 1; i <= numberOfDigits; i++) {
			final WebElement signPosition = Mockito.mock(WebElement.class);
			Mockito.when(signPosition.getAttribute("tabindex")).thenReturn(String.valueOf(i));
			Mockito.when(
			        this.webDriver.findElement(By.name(SantanderLogin.SIGN_POSITION_PREFIX + i)))
			        .thenReturn(signPosition);
			final String xpath = SantanderLogin.PASSWORD_INDEX_XPATH + (i + 1) + "]";

			final WebElement passwordIDNode = Mockito.mock(WebElement.class);
			Mockito.when(passwordIDNode.getText()).thenReturn(i + "th");
			Mockito.when(this.webDriver.findElement(By.xpath(xpath))).thenReturn(passwordIDNode);

			final String passwordEntryxpath = SantanderLogin.PASSWORD_INDEX_XPATH + (i + 1) + "]";
			final WebElement passwordEntryNode = Mockito.mock(WebElement.class);
			Mockito.when(passwordEntryNode.getText()).thenReturn(i + "St");
			Mockito.when(this.webDriver.findElement(By.xpath(passwordEntryxpath)))
			        .thenReturn(passwordEntryNode);
			final String xpath2 = SantanderLogin.PASSWORD_VALUE_XPATH + i + "\"]";
			final WebElement passwordValueNode = Mockito.mock(WebElement.class);
			Mockito.when(this.webDriver.findElement(By.xpath(xpath2)))
			        .thenReturn(passwordValueNode);

		}
		final WebElement passwordSubmitNode = Mockito.mock(WebElement.class);
		Mockito.when(this.webDriver.findElement(By.xpath(SantanderLogin.PASSWORD_SUBMIT_XPATH)))
		        .thenReturn(passwordSubmitNode);

		this.login.get();
	}

	// @Test
	/**
	 * Test santander login.
	 */
	public final void testSantanderLogin() {
		Assert.assertNotNull(this.login);
	}

}
