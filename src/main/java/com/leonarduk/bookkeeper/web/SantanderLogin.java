/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;
import com.leonarduk.webscraper.core.config.Config;
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

	/**
	 * The Constant IN_WHAT_CITY_OR_TOWN_WERE_YOU_MARRIED_ENTER_FULL_NAME_OF_CITY_OR_TOWN.
	 */
	private static final String IN_WHAT_CITY_OR_TOWN_WERE_YOU_MARRIED = "In what city or town were you married? (Enter full name of city or town)";

	/**
	 * The Constant IN_WHAT_CITY_OR_TOWN_WERE_YOU_BORN_ENTER_FULL_NAME_OF_CITY_OR_TOWN_ONLY.
	 */
	public static final String IN_WHAT_CITY_OR_TOWN_WERE_YOU_BORN = "In what city or town were you born? (Enter full name of city or town only)";

	/**
	 * The Constant IN_WHAT_CITY_OR_TOWN_WAS_YOUR_FATHER_BORN_ENTER_FULL_NAME_OF_CITY_OR_TOWN_ONLY .
	 */
	private static final String IN_WHAT_CITY_OR_TOWN_WAS_YOUR_FATHER_BORN = "In what city or town was your father born? (Enter full name of city or town only)";

	/** The Constant WHAT_IS_YOUR_FATHER_S_MIDDLE_NAME. */
	public static final String WHAT_IS_YOUR_FATHER_S_MIDDLE_NAME = "What is your father´s middle name?";

	/** The Constant WHAT_WAS_THE_NAME_OF_YOUR_FIRST_PET. */
	public static final String WHAT_WAS_THE_NAME_OF_YOUR_FIRST_PET = "What was the name of your first pet?";

	/**
	 * The Constant
	 * WHAT_WAS_THE_NAME_OF_YOUR_LAST_SCHOOL_ENTER_ONLY_THINGWALL_FOR_THINGWALL_INFANT_SCHOOL .
	 */
	public static final String NAME_OF_YOUR_LAST_SCHOOL = "What was the name of your last school? "
	        + "(Enter only \"Thingwall\" for Thingwall infant School)";

	/** The Constant WHAT_IS_YOUR_FAVOURITE_FOOTBALL_TEAM. */
	public static final String WHAT_IS_YOUR_FAVOURITE_FOOTBALL_TEAM = "What is your favourite football team?";

	/**
	 * The Constant
	 * WHERE_DID_YOU_MEET_YOUR_SPOUSE_FOR_THE_FIRST_TIME_ENTER_FULL_NAME_OF_CITY_OR_TOWN_ONLY .
	 */
	public static final String WHERE_DID_YOU_MEET_YOUR_SPOUSE = "Where did you meet your spouse for the first time?"
	        + " (Enter full name of city or town only)";

	/** The Constant NAME_OF_CHIEF_BRIDESMAID. */
	public static final String NAME_OF_CHIEF_BRIDESMAID = "What is the first name of the maid of honour "
	        + "or chief bridesmaid at your wedding?";

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

	/** The filter questions. */
	private final Question[] filterQuestions = new Question[] {
	        new Question("bridesmaid", SantanderLogin.NAME_OF_CHIEF_BRIDESMAID),
	        new Question("football", SantanderLogin.WHAT_IS_YOUR_FAVOURITE_FOOTBALL_TEAM),
	        new Question("spouse", SantanderLogin.WHERE_DID_YOU_MEET_YOUR_SPOUSE),
	        new Question("school", SantanderLogin.NAME_OF_YOUR_LAST_SCHOOL),
	        new Question("pet", SantanderLogin.WHAT_WAS_THE_NAME_OF_YOUR_FIRST_PET),
	        new Question("fathermiddle", SantanderLogin.WHAT_IS_YOUR_FATHER_S_MIDDLE_NAME),
	        new Question("fatherborn", SantanderLogin.IN_WHAT_CITY_OR_TOWN_WAS_YOUR_FATHER_BORN),
	        new Question("born", SantanderLogin.IN_WHAT_CITY_OR_TOWN_WERE_YOU_BORN),
	        new Question("citymarried", SantanderLogin.IN_WHAT_CITY_OR_TOWN_WERE_YOU_MARRIED) };

	/** The accounts url. */
	private final String loginUrl;

	/** The password. */
	private final String password;

	/** The questions. */
	private final Map<String, String> questions;

	/** The security number. */
	private final String securityNumber;

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

		final SantanderDownloadTransactions santanderLogin = new SantanderDownloadTransactions(
		        new SantanderLogin(webDriver, config));
		santanderLogin.get();

	}

	/**
	 * Instantiates a new santander login.
	 *
	 * @param webDriver
	 *            the web driver
	 * @param config
	 *            the config
	 */
	public SantanderLogin(final WebDriver webDriver, final Config config) {
		super(webDriver, config.getProperty("bookkeeper.web.santander.url.accounts"));
		this.loginUrl = config.getProperty("bookkeeper.web.santander.url.start");
		this.customerId = config.getProperty("bookkeeper.web.santander.id");
		this.password = config.getProperty("bookkeeper.web.santander.password");
		this.securityNumber = config.getProperty("bookkeeper.web.santander.securitynumber");
		this.questions = new HashMap<>();
		final String questionPrefix = "bookkeeper.web.santander.question.";
		for (final Question question : this.filterQuestions) {
			this.questions.put(question.configKeyString,
			        config.getProperty(questionPrefix + question.configKeyString));
			SantanderLogin.LOGGER.info("Q: " + question.configKeyString + " - "
			        + this.questions.get(question.configKeyString));

		}
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

		for (final Question question : this.filterQuestions) {
			if (questionText.equals(question.questionTextString)) {
				answer = this.questions.get(question.configKeyString);
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

	/**
	 * The Class Question.
	 */
	class Question {

		/** The config key. */
		final String configKeyString;

		/** The question text. */
		final String questionTextString;

		/**
		 * Instantiates a new question.
		 *
		 * @param configKey
		 *            the config key
		 * @param questionText
		 *            the question text
		 */
		public Question(final String configKey, final String questionText) {
			super();
			this.configKeyString = configKey;
			this.questionTextString = questionText;
		}

	}
}
