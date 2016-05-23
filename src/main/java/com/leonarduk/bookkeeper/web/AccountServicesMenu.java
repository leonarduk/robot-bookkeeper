/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

/**
 * The Enum AccountServicesMenu.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 18 Feb 2015
 */
enum AccountServicesMenu {

	/** The Overview. */
	Overview(1),
	/** The Alerts. */
	Alerts(2),
	/** The e documents. */
	eDocuments(3),
	/** The Requests. */
	Requests(4),
	/** The Mobile payments. */
	MobilePayments(5);

	/** The index. */
	private int itemIndex;

	/**
	 * Instantiates a new account services menu.
	 *
	 * @param index
	 *            the index
	 */
	private AccountServicesMenu(final int index) {
		this.itemIndex = index;
	}

	/**
	 * Url.
	 *
	 * @return the string
	 */
	public String url() {
		return "//*[@id=\"submenu\"]/ul/li[" + this.itemIndex + "]/a";
	}
}
