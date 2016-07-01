/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.santander;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.leonarduk.web.BaseSeleniumPage;
import com.thoughtworks.selenium.SeleniumException;

/**
 * The Class SantanderLogin.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 3 Feb 2015
 */
public class SantanderLogin extends BaseSeleniumPage {

	/** The Constant PASSWORD_SUBMIT_XPATH. */
	public static final String PASSWORD_SUBMIT_XPATH = "//*[@id=\"formAuthenticationAbbey\"]/div[2]/span[1]/input";

	/** The Constant PASSWORD_VALUE_XPATH. */
	public static final String PASSWORD_VALUE_XPATH = "//*[@id=\"passwordPosition";

	/** The Constant SIGN_POSITION_PREFIX. */
	public static final String SIGN_POSITION_PREFIX = "signPosition";

	/** The Constant ANSWER_SUBMIT_XPATH. */
	public static final String ANSWER_SUBMIT_XPATH = "//*[@id=\"formCustomerID\"]/div/span[1]/input";

	/** The Constant CUSTOMER_ID_SUBMIT_XPATH. */
	public static final String CUSTOMER_ID_SUBMIT_XPATH = "//*[@id=\"formCustomerID_1\"]/fieldset/div/div/span/input";

	/** The Constant CUSTOMER_ID_XPATH. */
	public static final String CUSTOMER_ID_XPATH = "//*[@id=\"infoLDAP_E.customerID\"]";

	/** The Constant answerxpath. */
	public static final String ANSWER_XPATH = "//*[@id=\"cbQuestionChallenge.responseUser\"]";

	/** The Constant questionXpath. */
	public static final String QUESTION_XPATH = "//*[@id=\"formCustomerID\"]/fieldset/div/div[1]/span[2]";

	/** The Constant _logger. */

	private static final Logger LOGGER = Logger.getLogger(SantanderLogin.class);

	/** The Constant SECURITY_ID_POSITION_PREFIX. */
	private static final String SECURITY_ID_POSITION_PREFIX = "passwordPosition";

	/** The Constant SECURITY_ID_INDEX_PREFIX. */
	private static final String SECURITY_ID_INDEX_PREFIX = "//*[@id=\"formAuthenticationAbbey\"]/div[1]/div[2]/span[2]/span/span[1]/label[";

	/** The Constant passwordXpathPrefix. */
	public static final String PASSWORD_INDEX_XPATH = "//*[@id=\"formAuthenticationAbbey\"]/div[1]/div[1]/span[2]/span/span[1]/label[";

	/** The customer number. */
	private final String customerId;

	/** The accounts url. */
	private final String loginUrl;

	/** The password. */
	private final String password;

	/** The questions. */
	private final Map<String, String> questions;

	/** The security number. */
	private final String securityNumber;

	private final SantanderConfig config;

	/**
	 * Instantiates a new santander login.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param config
	 *            the config
	 */
	public SantanderLogin(final SantanderConfig config) {
		super(config.getWebDriver(), config.getSantanderAccountsUrl());
		this.config = config;
		this.loginUrl = this.config.getSantanderStartUrl();
		this.customerId = this.config.getSantanderId();
		this.password = this.config.getPassword();
		this.securityNumber = this.config.getSecurityNumber();
		this.questions = this.config.getQuestions();
		SantanderLogin.LOGGER.info("URL: " + this.loginUrl);
		SantanderLogin.LOGGER.info("ID: " + this.customerId);

		SantanderLogin.LOGGER.info("Password: " + this.password);

		SantanderLogin.LOGGER.info("SecurityNumber: " + this.securityNumber);
	}

	/**
	 * Enter characters.
	 *
	 * @param signPositionPrefix
	 *            the sign position prefix
	 * @param prefix
	 *            the prefix
	 * @param keyword
	 *            the keyword
	 */
	private void enterCharacters(final String signPositionPrefix, final String prefix,
	        final String keyword) {
		final int lastDigit = 3;
		final WebElement[] nodes = new WebElement[lastDigit];
		for (int i = 1; i < (lastDigit + 1); i++) {
			final WebElement node = this.getWebDriver()
			        .findElement(By.name(signPositionPrefix + i));
			int index = (Integer.valueOf(node.getAttribute("tabindex"))).intValue() - 1;
			if (index >= lastDigit) {
				index -= lastDigit;
			}
			nodes[index] = node;
		}

		this.enterCode(1, nodes[0], prefix, keyword);
		this.enterCode(2, nodes[1], prefix, keyword);
		this.enterCode(lastDigit, nodes[2], prefix, keyword);
	}

	/**
	 * Gets the required character.
	 *
	 * @param index
	 *            the index
	 * @param webElement
	 *            the web element
	 * @param prefix
	 *            the prefix
	 * @param keyphrase
	 *            the keyphrase
	 */

	private void enterCode(final int index, final WebElement webElement, final String prefix,
	        final String keyphrase) {
		final String xpath = prefix + (index) + "]";
		final String text = this.findElementByXpath(xpath).getText();
		final int value = this.keepNumberOnly(text);
		final String character = keyphrase.substring(value - 1, value);
		SantanderLogin.LOGGER.info(text + " " + value + " " + character);

		webElement.sendKeys(character);
	}

	/**
	 * Enter customer id.
	 */
	private void enterCustomerId() {
		this.enterValueIntoField(this.customerId, SantanderLogin.CUSTOMER_ID_XPATH);
		this.clickField(SantanderLogin.CUSTOMER_ID_SUBMIT_XPATH);
	}

	/**
	 * Enter password.
	 */
	private void enterPassword() {
		final String signPositionPrefix = SantanderLogin.SIGN_POSITION_PREFIX;
		final String prefix = SantanderLogin.PASSWORD_INDEX_XPATH;
		this.enterCharacters(signPositionPrefix, prefix, this.password);
	}

	/**
	 * Enter security number.
	 */
	private void enterSecurityNumber() {
		final String signPositionPrefix = SantanderLogin.SECURITY_ID_POSITION_PREFIX;
		final String prefix = SantanderLogin.SECURITY_ID_INDEX_PREFIX;
		this.enterCharacters(signPositionPrefix, prefix, this.securityNumber);
	}

	/**
	 * Filter question.
	 */
	private void filterQuestion() {
		final List<WebElement> questopnList = this.getWebDriver()
		        .findElements(By.xpath(SantanderLogin.QUESTION_XPATH));
		if (questopnList.size() < 1) {
			return;
		}
		final String questionText = questopnList.get(0).getText();
		String answer = null;

		for (final Question question : this.config.getFilterQuestions()) {
			if (questionText.equals(question.getQuestionTextString())) {
				answer = this.questions.get(question.getConfigKeyString());
				break;
			}
		}
		if (null == answer) {
			throw new SeleniumException("Unexpected question:" + questionText);
		}
		this.enterValueIntoField(answer, SantanderLogin.ANSWER_XPATH);

		this.clickField(SantanderLogin.ANSWER_SUBMIT_XPATH);

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
		this.enterCustomerId();
		this.waitForPageToLoad();
		try {
			this.filterQuestion();
			this.waitForPageToLoad();
		}
		catch (final NoSuchElementException e) {
			SantanderLogin.LOGGER.info("No filter screen. Ignore and try next page", e);
		}
		this.passwordPage();

		try {
			final WebElement popup = this
			        .findElementByXpath("//*[@id=\"PopUp\"]/div/form/fieldset/div/span/a");
			if (null != popup) {
				popup.click();
			}
		}
		catch (final NoSuchElementException e) {
			SantanderLogin.LOGGER.info("No pop up screen. Ignore and try next page", e);
		}
		this.waitForPageToLoad();
	}

	/**
	 * Password page.
	 */
	private void passwordPage() {
		this.enterPassword();
		this.enterSecurityNumber();
		this.clickField(SantanderLogin.PASSWORD_SUBMIT_XPATH);
	}

}
