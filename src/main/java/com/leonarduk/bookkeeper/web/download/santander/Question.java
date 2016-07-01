/**
 * Question
 * 
 * @author ${author}
 * @since 27-Jun-2016
 */
package com.leonarduk.bookkeeper.web.download.santander;

/**
 * The Class Question.
 */
class Question {

	/** The config key. */
	private final String configKeyString;

	/** The question text. */
	private final String questionTextString;

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

	public String getConfigKeyString() {
		return this.configKeyString;
	}

	public String getQuestionTextString() {
		return this.questionTextString;
	}

}
