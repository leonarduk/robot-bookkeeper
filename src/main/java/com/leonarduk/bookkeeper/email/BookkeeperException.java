/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.email;

/**
 * The Class BookkeeperException.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 3 Feb 2015
 */
public class BookkeeperException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new bookkeeper exception.
	 *
	 * @param message
	 *            the string
	 */
	public BookkeeperException(final String message) {
		super(message);
	}

}
