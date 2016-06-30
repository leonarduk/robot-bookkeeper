/**
 * SantanderConfig
 *
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web.santander;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.leonarduk.webscraper.core.config.Config;

public class SantanderConfig {
	private static final String BOOKKEEPER_WEB_SANTANDER_ID = "bookkeeper.web.santander.id";

	private static final String BOOKKEEPER_WEB_SANTANDER_URL_START = "bookkeeper.web.santander.url.start";

	private static final String BOOKKEEPER_WEB_SANTANDER_URL_ACCOUNTS = "bookkeeper.web.santander.url.accounts";

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
	public static final String	NAME_OF_CHIEF_BRIDESMAID	= "What is the first name of the maid of honour "
	        + "or chief bridesmaid at your wedding?";
	private static final Logger	LOGGER						= Logger.getLogger(SantanderConfig.class);
	/** The filter questions. */
	private final Question[]	filterQuestions				= new Question[] {
	        new Question("bridesmaid", SantanderConfig.NAME_OF_CHIEF_BRIDESMAID),
	        new Question("football", SantanderConfig.WHAT_IS_YOUR_FAVOURITE_FOOTBALL_TEAM),
	        new Question("spouse", SantanderConfig.WHERE_DID_YOU_MEET_YOUR_SPOUSE),
	        new Question("school", SantanderConfig.NAME_OF_YOUR_LAST_SCHOOL),
	        new Question("pet", SantanderConfig.WHAT_WAS_THE_NAME_OF_YOUR_FIRST_PET),
	        new Question("fathermiddle", SantanderConfig.WHAT_IS_YOUR_FATHER_S_MIDDLE_NAME),
	        new Question("fatherborn", SantanderConfig.IN_WHAT_CITY_OR_TOWN_WAS_YOUR_FATHER_BORN),
	        new Question("born", SantanderConfig.IN_WHAT_CITY_OR_TOWN_WERE_YOU_BORN),
	        new Question("citymarried", SantanderConfig.IN_WHAT_CITY_OR_TOWN_WERE_YOU_MARRIED) };

	private final Config config;

	public SantanderConfig(final Config config2) {
		this.config = config2;
	}

	public SantanderConfig(final String string) throws IOException {
		this.config = new Config(string);
	}

	public Question[] getFilterQuestions() {
		return this.filterQuestions;
	}

	public String getPassword() {
		return this.config.getProperty("bookkeeper.web.santander.password");
	}

	public Map<String, String> getQuestions() {
		final Map<String, String> questions2 = new HashMap<>();
		final String questionPrefix = "bookkeeper.web.santander.question.";
		for (final Question question : this.getFilterQuestions()) {
			questions2.put(question.getConfigKeyString(),
			        this.config.getProperty(questionPrefix + question.getConfigKeyString()));
			SantanderConfig.LOGGER.info("Q: " + question.getConfigKeyString() + " - "
			        + questions2.get(question.getConfigKeyString()));

		}
		return questions2;
	}

	public String getSantanderAccountsUrl() {
		return this.config.getProperty(SantanderConfig.BOOKKEEPER_WEB_SANTANDER_URL_ACCOUNTS);
	}

	public String getSantanderId() {
		return this.config.getProperty(SantanderConfig.BOOKKEEPER_WEB_SANTANDER_ID);
	}

	public String getSantanderStartUrl() {
		return this.config.getProperty(SantanderConfig.BOOKKEEPER_WEB_SANTANDER_URL_START);
	}

	public String getSecurityNumber() {
		return this.config.getProperty("bookkeeper.web.santander.securitynumber");
	}

	public SantanderConfig setSantanderStartUrl(final String url) {
		this.config.setProperty(SantanderConfig.BOOKKEEPER_WEB_SANTANDER_URL_START, url);
		return this;
	}

}
