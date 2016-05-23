/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.email;

import org.apache.log4j.Logger;

import com.leonarduk.core.email.EmailMessage;
import com.leonarduk.core.email.EmailProcessor;

/**
 * The Class BFCAInvoiceProcessor.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 18 Feb 2015
 */
public class BFCAInvoiceProcessor implements EmailProcessor {

	/** The Constant BFCA_INVOICE. */
	public static final String BFCA_INVOICE = "BFCA Invoice";

	/** The Constant LOGGER. */
	static final Logger LOGGER = Logger.getLogger(BFCAInvoiceProcessor.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.core.email.EmailProcessor#process(com.leonarduk.core.email.EmailMessage)
	 */
	@Override
	public final boolean process(final EmailMessage emailMessage) throws BookkeeperException {
		final String emailSender = BFCAInvoiceProcessor.BFCA_INVOICE;
		if (!emailMessage.getSender().equals(emailSender)) {
			return false;
		}
		BFCAInvoiceProcessor.LOGGER.info("Processing " + emailSender + " message");
		return true;
	}

}
