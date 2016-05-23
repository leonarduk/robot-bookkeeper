/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import org.apache.log4j.Logger;

import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class SantanderDownloadTransactions.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 6 Feb 2015
 */
public class SantanderDownloadTransactions extends BaseSeleniumPage {

    /** The Constant LOGGER. */
    static final Logger LOGGER = Logger
            .getLogger(SantanderDownloadTransactions.class);

    /** The login. */
    private final BaseSeleniumPage loginPage;

    /**
     * Instantiates a new santander download transactions.
     *
     * @param login
     *            the login
     */
    public SantanderDownloadTransactions(final SantanderLogin login) {
        super(login.getWebDriver(), login.getExpectedUrl());
        this.loginPage = login;
    }

    /**
     * Click e documents.
     */
    private void clickEDocuments() {
        this.findElementByXpath(MyAccountsMenu.EDocuments.url()).click();
    }

    /**
     * Download latest statement.
     */
    public final void downloadLatestStatement() {
        this.isLoaded();
        this.clickEDocuments();
        this.waitForPageToLoad();
        this.downloadStatement(0);
    }

    /**
     * Download statement.
     *
     * @param index
     *            the index
     */
    private void downloadStatement(final int index) {
        this.getWebDriver().switchTo().frame(0);
        this.findElementByXpath("//*[@id=\"" + index + "\"]").click();
    }

    /**
     * Download transactions.
     */
    public final void downloadTransactions() {
        // select transactions
        final String xpath = "//*[@id=\"submenu\"]/ul/li[2]/a";
        this.findElementByXpath(xpath).click();
        this.waitForPageToLoad();
        // select download transactions
        this.findElementByXpath("//*[@id=\"content\"]/p[4]/a").click();
        this.waitForPageToLoad();
        // select all since last download
        this.findElementByXpath("//*[@id=\"AllorLastL\"]").click();
        // select QIF
        this.findElementByXpath("//*[@id=\"sel_downloadto\"]/option[2]")
                .click();
        // click Download
        this.findElementByXpath(
                "//*[@id=\"content\"]/div[2]/div/form/fieldset/div[2]/span[1]/input")
                .click();
        this.waitForPageToLoad();
    }

    /*
     * (non-Javadoc)
     * @see org.openqa.selenium.support.ui.LoadableComponent#load()
     */
    @Override
    protected final void load() {
        this.loginPage.get();
        try {
            Thread.sleep(BaseSeleniumPage.ONE_SECOND_IN_MS);
        }
        catch (final InterruptedException e) {
            LOGGER.info("Interrupted");
        }
    }

}
