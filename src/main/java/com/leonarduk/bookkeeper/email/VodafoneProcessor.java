/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.email;

import org.apache.log4j.Logger;

import com.leonarduk.webscraper.core.email.EmailMessage;
import com.leonarduk.webscraper.core.email.EmailProcessor;

/**
 * The Class VodafoneProcessor.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 18 Feb 2015
 */
public class VodafoneProcessor implements EmailProcessor {

	/** The Constant VODAFONE. */
	public static final String VODAFONE = "Vodafone";

	/** The Constant LOGGER. */
	static final Logger LOGGER = Logger.getLogger(VodafoneProcessor.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.core.email.EmailProcessor#process(com.leonarduk.core.email.EmailMessage)
	 */
	@Override
	public final boolean process(final EmailMessage emailMessage) throws BookkeeperException {
		final String emailSender = VodafoneProcessor.VODAFONE;
		final String subjectPrefix = "Your Bill is Ready ";
		if (!emailMessage.getSender().equals(emailSender)
		        || !emailMessage.getSubject().startsWith(subjectPrefix)) {
			return false;
		}
		VodafoneProcessor.LOGGER
		        .info("Processing " + emailSender + " " + emailMessage.getSubject());
		return true;

	}

}
