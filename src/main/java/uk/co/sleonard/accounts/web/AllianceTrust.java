/**
 * All rights reserved. @Leonard UK Ltd.
 */
package uk.co.sleonard.accounts.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class AllianceTrust.
 */
public class AllianceTrust extends BaseSeleniumPage {

    /**
     * Instantiates a new alliance trust.
     *
     * @param webDriver
     *            the web driver
     */
    public AllianceTrust(final WebDriver webDriver) {
        super(webDriver,
        // "https://atonline.alliancetrust.co.uk/atonline/index.jsp");
                "https://atonline.alliancetrust.co.uk/atonline/secure/CustomerListView.action");
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        final WebDriver webDriver = new FirefoxDriver();
        WebDriver freeAgentWebdriver = new FirefoxDriver();
        String ccbuserName = "stevel56";
        String ccbpassword = "N0bigm0mas!";
        String steveaccount = "155266";
        String lucyAccount = "155385";

        AllianceTrust trust = new AllianceTrust(webDriver);
        trust.get();

        String pensionValue = trust.getValue(1, steveaccount);
        System.out.println("Pension:" + pensionValue);

        String steveLisaValue = trust.getValue(2, steveaccount);
        System.out.println("Steve ISA:" + steveLisaValue);

        String lucyIsaValue = trust.getValue(1, lucyAccount);
        System.out.println("Lucy ISA:" + lucyIsaValue);
        UploadToClearCheckbook.updateEstimate("AT SIPP (StockMarket)",
                pensionValue, ccbuserName, ccbpassword, freeAgentWebdriver,
                "//*[@id=\"account-overviews\"]/div[3]/div[3]",
                "Updated from Alliance Trust");
        UploadToClearCheckbook.updateEstimate("AT ISA Steve (StockMarket)",
                steveLisaValue, ccbuserName, ccbpassword, freeAgentWebdriver,
                "//*[@id=\"account-overviews\"]/div[2]/div[3]",
                "Updated from Alliance Trust");
        UploadToClearCheckbook.updateEstimate("AT ISA Lucy (StockMarket)",
                lucyIsaValue, ccbuserName, ccbpassword, freeAgentWebdriver,
                "//*[@id=\"account-overviews\"]/div[1]/div[3]",
                "Updated from Alliance Trust");

    }

    /*
     * (non-Javadoc)
     * @see org.openqa.selenium.support.ui.LoadableComponent#load()
     */
    public final void load() {
        this.getWebDriver().get(
                "https://atonline.alliancetrust.co.uk/atonline/login.jsp");
        String accountNumber = "84871967";
        String password = "N0bigm0mas";

        this.getWebDriver().findElement(By.id("pid")).clear();
        this.getWebDriver().findElement(By.id("pid")).sendKeys(accountNumber);
        this.getWebDriver()
                .findElement(By.cssSelector("input[type=\"image\"]")).click();
        enterPassword(password);
        waitForPageToLoad();

    }

    /**
     * Gets the value.
     *
     * @param row
     *            the row
     * @param account
     *            the account
     * @return the value
     */
    public final String getValue(final int row, final String account) {
        waitForPageToLoad(By.linkText(account)).click();

        String xpath =
                "//*[@id=\"hor-minimalist-b\"]/tbody/tr[" + (row + 1)
                        + "]/td[5]";

        String value =
                waitForPageToLoad(By.xpath(xpath)).getAttribute("textContent")
                        .trim();

        waitForPageToLoad(By.linkText("Home")).click();

        return value;

    }

    /**
     * Wait for page to load.
     *
     * @param by
     *            the by
     * @return the web element
     */
    private WebElement waitForPageToLoad(final By by) {
        while (!this.isElementPresent(by)) {
            this.waitForPageToLoad();
        }
        return this.getWebDriver().findElement(by);
    }

    /**
     * Enter password.
     *
     * @param password
     *            the password
     */
    public final void enterPassword(final String password) {
        final int numberOfDigitsToEnter = 3;
        for (int i = 0; i < numberOfDigitsToEnter; i++) {
            String xpathExpression =
                    "//*[@id=\"login-r\"]/form/table/tbody/tr[1]/td[" + (i + 1)
                            + "]";

            WebElement indexElement =
                    this.getWebDriver().findElement(By.xpath(xpathExpression));
            int index =
                    Integer.valueOf(indexElement.getText().replaceAll("\\D+",
                            ""));

            this.getWebDriver().findElement(By.id("ppd" + (i))).clear();
            String character = password.substring(index - 1, index);
            this.getWebDriver().findElement(By.id("ppd" + (i)))
                    .sendKeys(character);
        }
        this.getWebDriver()
                .findElement(By.xpath("//input[@value='Continue >']")).click();
    }

}
