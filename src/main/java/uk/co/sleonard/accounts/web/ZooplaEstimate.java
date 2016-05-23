/**
 * All rights reserved. @Leonard UK Ltd.
 */
package uk.co.sleonard.accounts.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.core.FileUtils;
import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.web.SeleniumUtils;

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
    private static final Logger _logger = Logger
            .getLogger(ZooplaEstimate.class);

    /**
     * Instantiates a new nationwide login.
     *
     * @param webDriver
     *            the web driver
     * @param customerNumber
     *            the customer number
     * @param memorableword
     *            the memorableword
     * @param password
     *            the password
     */
    public ZooplaEstimate(final WebDriver webDriver) {
        super(
                webDriver,
                "http://www.zoopla.co.uk/property/60-willoughby-road/kingston-upon-thames/kt2-6lj/11760869");
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        ZooplaEstimate zooplaEstimate = new ZooplaEstimate(new FirefoxDriver());
        zooplaEstimate.get();
        String estimate = zooplaEstimate.getEstimate();
        String account = "RE - 60 Willoughby Road (Illiquid)";

        String ccbuserName = "stevel56";
        String ccbpassword = "N0bigm0mas!";

        final File tempDir = FileUtils.createTempDir();

        final WebDriver webDriver =
                SeleniumUtils.getDownloadCapableBrowser(tempDir);

        String valueXpath = "//*[@id=\"account-overviews\"]/div[29]/div[3]";
        String memo = "Updated from Zoopla estimate";

        UploadToClearCheckbook.updateEstimate(account, estimate, ccbuserName,
                ccbpassword, webDriver, valueXpath, memo);

    }

    /*
     * (non-Javadoc)
     * @see org.openqa.selenium.support.ui.LoadableComponent#load()
     */
    @Override
    protected final void load() {
        final int fewSeconds = 3;
        this.getWebDriver().manage().timeouts()
                .implicitlyWait(fewSeconds, TimeUnit.SECONDS);
        _logger.info("Load: " + getExpectedUrl());
        this.getWebDriver().get(getExpectedUrl());
        getEstimate();
    }

    public String getEstimate() {
        WebElement estimateNode =
                this.findElementByXpath("//*[@id=\"estimate-property\"]/ul[1]/li[1]/p/span/strong");
      
        String value = estimateNode.getText();
        _logger.info("Estimate: " + value);
        return value;
    }

}
