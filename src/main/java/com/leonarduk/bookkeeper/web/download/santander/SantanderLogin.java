/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.santander;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.leonarduk.web.BasePage;
import com.leonarduk.web.ClickableElement;
import com.leonarduk.web.SeleniumBrowserController;
import com.leonarduk.web.SeleniumException;

/**
 * The Class SantanderLogin.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 3 Feb 2015
 */
public class SantanderLogin extends BasePage<SantanderLogin> {

	/** The Constant PASSWORD_SUBMIT_XPATH. */
	public static final String PASSWORD_SUBMIT_XPATH = "//*[@id=\"formAuthenticationAbbey\"]/div[2]/div[3]/span/input";
	/** The Constant PASSWORD_VALUE_XPATH. */
	public static final String PASSWORD_VALUE_XPATH = "//*[@id=\"passwordPosition";

	/** The Constant SIGN_POSITION_PREFIX. */
	public static final String SIGN_POSITION_PREFIX = "signPosition";
	// *[@id="signPosition1"]
	/** The Constant ANSWER_SUBMIT_XPATH. */
	public static final String ANSWER_SUBMIT_XPATH = "//*[@id=\"formCustomerID\"]/div/span[1]/input";

	/** The Constant CUSTOMER_ID_SUBMIT_XPATH. */
	public static final String CUSTOMER_ID_SUBMIT_XPATH = "/html/body/div[2]/div[3]/div/div/div[2]/div[1]/form/fieldset/div/span/span[1]/span[2]/input";

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
	private static final String SECURITY_ID_INDEX_PREFIX = "//*[@id=\"formAuthenticationAbbey\"]/div[2]/div[2]/div/span[2]/span/span[1]/label[";
	public static final String PASSWORD_INDEX_XPATH = "//*[@id=\"formAuthenticationAbbey\"]/div[2]/div[1]/div/span[2]/span/span[1]/label[";
//	"//*[@id=\"formAuthenticationAbbey\"]/div[1]/div[1]/span[2]/span/span[1]/label[";

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

	/** The config. */
	private final SantanderConfig config;

	/**
	 * Instantiates a new santander login.
	 *
	 * @param config the config
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SantanderLogin(final SantanderConfig config) throws IOException {
		super(new SeleniumBrowserController(config.getWebDriver()), config.getSantanderAccountsUrl());
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
	 * @param signPositionPrefix the sign position prefix
	 * @param prefix             the prefix
	 * @param keyword            the keyword
	 * @throws IOException 
	 */
	private void enterCharacters(final String signPositionPrefix, final String prefix, final String keyword) throws IOException {
		final int lastDigit = 3;
		final ClickableElement[] nodes = new ClickableElement[lastDigit];
		for (int i = 1; i < (lastDigit + 1); i++) {
			final ClickableElement node = this // *[@id="signPosition1"]
					.findElementById(signPositionPrefix + i);// *[@id="formAuthenticationAbbey"]/div[2]/div[1]/div/span[2]/span/span[1]/label[1]/strong/b
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
	 * @param index      the index
	 * @param webElement the web element
	 * @param prefix     the prefix
	 * @param keyphrase  the keyphrase
	 * @throws IOException 
	 */

	private void enterCode(final int index, final ClickableElement webElement, final String prefix,
			final String keyphrase) throws IOException {
		final String xpath = prefix + (index) + "]";
		final String text = this.findElementByXpath(xpath).getText();
		final int value = this.keepNumberOnly(text);
		final String character = keyphrase.substring(value - 1, value);
		SantanderLogin.LOGGER.info(text + " " + value + " " + character);

		webElement.sendKeys(character);
	}

	/**
	 * Enter customer id.
	 * @throws IOException 
	 */
	private void enterCustomerId() throws IOException {
		this.enterValueIntoField(this.customerId, SantanderLogin.CUSTOMER_ID_XPATH);
		this.clickField(SantanderLogin.CUSTOMER_ID_SUBMIT_XPATH);
	}

	/**
	 * Enter password.
	 * @throws IOException 
	 */
	private void enterPassword() throws IOException {
		final String signPositionPrefix = SantanderLogin.SIGN_POSITION_PREFIX;
		final String prefix = SantanderLogin.PASSWORD_INDEX_XPATH;
		this.enterCharacters(signPositionPrefix, prefix, this.password);
	}

	/**
	 * Enter security number.
	 * @throws IOException 
	 */
	private void enterSecurityNumber() throws IOException {
		final String signPositionPrefix = SantanderLogin.SECURITY_ID_POSITION_PREFIX;
		final String prefix = SantanderLogin.SECURITY_ID_INDEX_PREFIX;
		this.enterCharacters(signPositionPrefix, prefix, this.securityNumber);
	}

	/**
	 * Filter question.
	 * @throws IOException 
	 */
	private void filterQuestion() throws IOException {
		final List<ClickableElement> questopnList = this.findElementsByXpath(SantanderLogin.QUESTION_XPATH);
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

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public SantanderConfig getConfig() {
		return this.config;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() throws IOException {
		this.getBrowserController().get(this.loginUrl);
		this.waitForPageToLoad();
		this.enterCustomerId();
		this.waitForPageToLoad();
		try {
			this.filterQuestion();
			this.waitForPageToLoad();
		} catch (final NoSuchElementException e) {
			SantanderLogin.LOGGER.info("No filter screen. Ignore and try next page", e);
		}
		this.passwordPage();

		try {
			final ClickableElement popup = this.findElementByXpath("//*[@id=\"PopUp\"]/div/form/fieldset/div/span/a");

			if (null != popup) {
				popup.click();
			}
		} catch (final NoSuchElementException e) {
			SantanderLogin.LOGGER.info("No pop up screen. Ignore and try next page", e);
		}
		this.waitForPageToLoad();
	}

	/**
	 * Password page.
	 * @throws IOException 
	 */
	private void passwordPage() throws IOException {
		this.enterPassword();
		this.enterSecurityNumber();
		this.clickField(SantanderLogin.PASSWORD_SUBMIT_XPATH);
	}

}
