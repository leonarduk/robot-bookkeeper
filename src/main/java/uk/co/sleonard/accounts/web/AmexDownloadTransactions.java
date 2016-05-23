/**
 * All rights reserved. @Leonard UK Ltd.
 */
package uk.co.sleonard.accounts.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import uk.co.sleonard.accounts.web.UploadToClearCheckbook.Setting;

import com.leonarduk.core.FileUtils;
import com.leonarduk.web.SeleniumUtils;

/**
 * The Class AmexDownloadTransactions.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 24 Mar 2015
 */
public class AmexDownloadTransactions {

    /** The driver. */
    private WebDriver driver;

    /** The base url. */
    private String baseUrl;

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws Throwable
     *             the throwable
     */
    public static void main(final String[] args) throws Throwable {
        String userName = "stevel56";
        String password = "N0bigm0m";
        File downloadDir = FileUtils.createTempDir();
        WebDriver webDriver =
                SeleniumUtils.getDownloadCapableBrowser(downloadDir);

        AmexDownloadTransactions transactions =
                new AmexDownloadTransactions(webDriver);
        transactions.downloadTransactions(userName, password);

        String ccbuserName = "stevel56";
        String ccbpassword = "N0bigm0mas!";
        String account = "CC - AMEX";
        String fileToUpload = downloadDir.getAbsolutePath() + "/ofx.qif";

        String results =
                UploadToClearCheckbook.uploadToClearCheckbook(ccbuserName,
                        ccbpassword, account, fileToUpload, webDriver,
                        Setting.AMEX);
        System.out.println(results);
        // transactions.finalize();

    }

    /**
     * Instantiates a new amex download transactions.
     *
     * @param webDriver
     *            the web driver
     * @throws Exception
     *             the exception
     */
    public AmexDownloadTransactions(final WebDriver webDriver) throws Exception {
        this.driver = webDriver;
        baseUrl = "https://www.americanexpress.com/";
        final int fewSeconds = 3;
        driver.manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
    }

    /**
     * Download transactions.
     *
     * @param userName
     *            the user name
     * @param password
     *            the password
     * @throws Exception
     *             the exception
     */
    public final void downloadTransactions(
            final String userName,
            final String password) throws Exception {
        driver.get(baseUrl + "/uk/");
        driver.findElement(By.id("LabelUserID")).click();
        driver.findElement(By.id("UserID")).clear();
        driver.findElement(By.id("UserID")).sendKeys(userName);
        driver.findElement(By.id("Password")).clear();
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
        driver.findElement(By.id("estatement-link")).click();
        driver.findElement(By.cssSelector("#downloads-link > span.copy"))
                .click();
        driver.findElement(
                By.cssSelector("a[title=\"Export your statement data into a variety of file formats\"]"))
                .click();
        driver.findElement(By.id("QIF")).click();
        driver.findElement(By.id("selectCard10")).click();
        driver.findElement(By.id("radioid00")).click();
        driver.findElement(By.id("radioid01")).click();
        driver.findElement(By.id("radioid02")).click();
        driver.findElement(By.id("radioid03")).click();
        driver.findElement(By.linkText("Download Now")).click();
    }

}
