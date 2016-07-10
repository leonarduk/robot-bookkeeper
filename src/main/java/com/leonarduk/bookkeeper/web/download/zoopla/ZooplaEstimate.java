/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.zoopla;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.StringUtils;
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
public class ZooplaEstimate extends BaseSeleniumPage implements ValueSnapshotProvider {

	/** The Constant _logger. */
	private static final Logger _logger = Logger.getLogger(ZooplaEstimate.class);

	public ZooplaEstimate(final ZooplaConfig config) throws IOException {
		super(config.getWebDriver(), config.getUrl());
	}

	@Override
	public double getCurrentValue() {
		return StringUtils.convertMoneyString(this.getEstimate());
	}

	@Override
	public String getDescription() {
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return "From Zoopla Website " + format.format(new Date());
	}

	/**
	 * Gets the estimate.
	 *
	 * @return the estimate
	 */
	public String getEstimate() {
		this.get();
		final WebElement estimateNode = this
		        .findElementByXpath("//*[@id=\"estimate-property\"]/ul[1]/li[1]/p/span/strong");

		final String value = estimateNode.getText();
		ZooplaEstimate._logger.info("Estimate: " + value);
		return value;
	}

	@Override
	protected final void load() {
		final int fewSeconds = 3;
		this.getWebDriver().manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
		ZooplaEstimate._logger.info("Load: " + this.getExpectedUrl());
		this.getWebDriver().get(this.getExpectedUrl());
		this.getEstimate();
	}

}
