/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.zoopla;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.leonarduk.web.BaseSeleniumPage;

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

	public ZooplaEstimate(final ZooplaConfig config) {
		super(config.getWebDriver(), config.getUrl());
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
