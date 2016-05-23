/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

/**
 * The Enum MyAccountsMenu.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 18 Feb 2015
 */
public enum MyAccountsMenu {

	/** The Accounts. */
	Accounts(1),
	/** The Transactions. */
	Transactions(2),
	/** The Overdraft. */
	Overdraft(3),
	/** The Charges. */
	Charges(4),
	/** The Savings goals. */
	SavingsGoals(5),
	/** The E documents. */
	EDocuments(6),
	/** The Mobile payments. */
	MobilePayments(7);

	/** The index. */
	private int itemIndex;

	/**
	 * Instantiates a new my accounts menu.
	 *
	 * @param index
	 *            the index
	 */
	private MyAccountsMenu(final int index) {
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
