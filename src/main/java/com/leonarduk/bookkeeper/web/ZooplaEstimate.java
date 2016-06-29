/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.bookkeeper.web.clearcheckbook.UploadToClearCheckbook;
import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.web.SeleniumUtils;
import com.leonarduk.webscraper.core.FileUtils;

/**
 * The Class NationwideLogin.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 28 Mar 2015
 */
public class ZooplaEstimate extends BaseSeleniumPage {

	/** The Constant _logger. */
	private static final Logger _logger = Logger.getLogger(ZooplaEstimate.class);

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(final String[] args) throws Exception {
		final ZooplaEstimate zooplaEstimate = new ZooplaEstimate(new FirefoxDriver());
		zooplaEstimate.get();
		final String estimate = zooplaEstimate.getEstimate();
		final String account = "RE - 60 Willoughby Road (Illiquid)";

		final String ccbuserName = "stevel56";
		final String ccbpassword = "N0bigm0mas!";

		final File tempDir = FileUtils.createTempDir();

		final WebDriver webDriver = SeleniumUtils.getDownloadCapableBrowser(tempDir);

		final String valueXpath = "//*[@id=\"account-overviews\"]/div[29]/div[3]";
		final String memo = "Updated from Zoopla estimate";

		UploadToClearCheckbook.updateEstimate(account, estimate, ccbuserName, ccbpassword,
		        webDriver, valueXpath, memo);

	}

	/**
	 * Instantiates a new nationwide login.
	 *
	 * @param webDriver
	 *            the web driver
	 */
	public ZooplaEstimate(final WebDriver webDriver) {
		super(webDriver,
		        "http://www.zoopla.co.uk/property/60-willoughby-road/kingston-upon-thames/kt2-6lj/11760869");
	}

	/**
	 * Gets the estimate.
	 *
	 * @return the estimate
	 */
	public String getEstimate() {
		final WebElement estimateNode = this
		        .findElementByXpath("//*[@id=\"estimate-property\"]/ul[1]/li[1]/p/span/strong");

		final String value = estimateNode.getText();
		ZooplaEstimate._logger.info("Estimate: " + value);
		return value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() {
		final int fewSeconds = 3;
		this.getWebDriver().manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
		ZooplaEstimate._logger.info("Load: " + this.getExpectedUrl());
		this.getWebDriver().get(this.getExpectedUrl());
		this.getEstimate();
	}

}
