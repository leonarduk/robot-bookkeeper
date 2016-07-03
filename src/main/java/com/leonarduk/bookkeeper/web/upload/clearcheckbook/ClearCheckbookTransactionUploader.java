/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.upload.TransactionUploader;
import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.webscraper.core.FileUtils;

/**
 * The Class UploadToClearCheckbook.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 28 Mar 2015
 */
public class ClearCheckbookTransactionUploader implements AutoCloseable, TransactionUploader {
	/** The Constant LOGGER. */
	private final static Logger			LOGGER	= Logger
	        .getLogger(ClearCheckbookTransactionUploader.class);
	private final ClearCheckbookConfig	config;
	private String						account;

	public ClearCheckbookTransactionUploader(final ClearCheckbookConfig config) {
		this.config = config;
	}

	/**
	 * Choose file to upload.
	 *
	 * @param fileToUpload
	 *            the file to upload
	 */
	private void chooseFileToUpload(final String fileToUpload) {
		final WebDriver driver = this.config.getWebDriver();
		driver.findElement(By.linkText("Tools")).click();
		driver.findElement(By.linkText("Import Transactions")).click();
		final String replaceAll = fileToUpload.replaceAll("/", "\\\\");
		driver.findElement(By.id("import")).sendKeys(replaceAll);
		// *[@id="import"]
		driver.findElement(By.xpath("//*[@id=\"uploadForm\"]/button")).click();
	}

	@Override
	public void close() throws Exception {
		this.config.getWebDriver().close();
	}

	/**
	 * Convert money string.
	 *
	 * @param amount
	 *            the amount
	 * @return the double
	 */
	public double convertMoneyString(final String amount) {
		return Double.valueOf(amount.replaceAll("£", "").replaceAll(",", "")).doubleValue();
	}

	private void generalSettings() {
		try {
			new Select(this.config.getWebDriver().findElement(By.name("import_to_account")))
			        .selectByVisibleText(this.account);
		}
		catch (final NoSuchElementException e) {
			ClearCheckbookTransactionUploader.LOGGER.error("Failed to find element in amexSettings",
			        e);
			throw e;
		}
	}

	public QifFileFormatter getQifFileFormatter() {
		return new QifFileFormatter(QifFileFormatter.CCB_FORMAT);
	}

	/**
	 * Import transactions.
	 *
	 * @param removeDuplicates
	 * @return the string
	 */
	private String importTransactions(final boolean removeDuplicates) {
		final WebDriver driver = this.config.getWebDriver();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		BaseSeleniumPage.waitForPageToLoad(driver);
		driver.findElement(By.cssSelector("input.btn.btn-default")).click();
		BaseSeleniumPage.waitForPageToLoad(driver);

		if (removeDuplicates) {
			this.removeDuplicatesByPage(driver);
		}
		driver.findElement(By.linkText("« Return to Imported Transactions")).click();

		if (driver.findElements(By.id("jive")).size() > 0) {
			driver.findElement(By.id("jive")).click();
			final String results = driver.findElement(By.xpath("//*[@id=\"importTransactions\"]"))
			        .getText();
			driver.findElement(By.name("submit[all]")).click();
			return results;
		}
		return "No transactions added";
	}

	/**
	 * Login.
	 */
	private void login() {
		final WebDriver driver = this.config.getWebDriver();
		final String baseUrl = "https://www.clearcheckbook.com/";
		final int severalSeconds = 5;
		driver.manage().timeouts().implicitlyWait(severalSeconds, TimeUnit.SECONDS);
		driver.get(baseUrl + "/login");

		final List<WebElement> userNameElement = driver.findElements(By.id("ccb-l-username"));
		if (userNameElement.size() > 0) {
			userNameElement.get(0).sendKeys(this.config.getUserName());
			driver.findElement(By.id("ccb-l-password")).sendKeys(this.config.getPassword());
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
	}

	/**
	 * Removes the duplicates by page.
	 *
	 * @param driver
	 *            the driver
	 */
	private void removeDuplicatesByPage(final WebDriver driver) {
		final String numberOfDupsXpath = "/html/body/div[2]/h3";
		Integer duplicates = Integer.valueOf(driver.findElement(By.xpath(numberOfDupsXpath))
		        .getText().replace(" Duplicates Found", "").replace(" Duplicate Found", ""));

		while (duplicates.intValue() > 0) {
			driver.findElement(By.id("selectAllCheckbox")).click();
			driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
			final String updateFilterXpath = "/html/body/div[2]/div[6]/form/input";
			driver.findElement(By.xpath(updateFilterXpath)).click();

			duplicates = Integer.valueOf(driver.findElement(By.xpath(numberOfDupsXpath)).getText()
			        .replace(" Duplicates Found", "").replace(" Duplicate Found", ""));
		}
	}

	public void setAccount(final String account2) {
		this.account = account2;
	}

	/**
	 * Update estimate.
	 *
	 * @param currentValue
	 *            the current value
	 * @param valueXpath
	 *            the value xpath
	 * @param memo
	 *            the memo
	 * @return the string
	 */
	public String updateEstimate(final String currentValue, final String valueXpath,
	        final CharSequence memo) {
		this.login();

		final WebDriver driver = this.config.getWebDriver();
		final WebElement valueElement = driver.findElement(By.xpath(valueXpath));
		final String ccbValueString = valueElement.getText();

		// driver.findElement(By.linkText("Enter a Transaction")).click();
		driver.findElement(By.id("amount")).clear();
		final double ccbAmount = this.convertMoneyString(ccbValueString);
		double amount = this.convertMoneyString(currentValue) - ccbAmount;
		if (Math.abs(amount) < 1) {
			ClearCheckbookTransactionUploader.LOGGER.info("No change");
			return "No change";
		}
		final int veryLargeChange = 100000;
		if (Math.abs(amount) > veryLargeChange) {
			ClearCheckbookTransactionUploader.LOGGER
			        .warn("Suspected error, ignoring move from " + ccbAmount + " to " + amount);
			return "No change";
		}
		ClearCheckbookTransactionUploader.LOGGER
		        .info("Updating value from " + ccbAmount + " to " + amount);

		driver.findElement(By.id("amount")).sendKeys(String.valueOf(amount));
		driver.findElement(By.id("memo")).sendKeys(memo);

		String transactionType = "Deposit";
		if (amount < 0) {
			transactionType = "Withdrawal";
			amount *= -1;
		}

		new Select(driver.findElement(By.id("transaction_type")))
		        .selectByVisibleText(transactionType);
		new Select(driver.findElement(By.id("account_id"))).selectByVisibleText(this.account);
		new Select(driver.findElement(By.id("category_id"))).selectByVisibleText("Miscellaneous");
		driver.findElement(By.id("at_jive")).click();
		driver.findElement(By.id("addEntryButton")).click();

		return transactionType + ":" + amount;

	}

	/**
	 * Upload to clear checkbook.
	 *
	 * @param fileToUpload
	 *            the file to upload
	 * @return the string
	 */
	public String uploadToClearCheckbook(final String fileToUpload) {
		this.login();
		this.chooseFileToUpload(fileToUpload);
		this.generalSettings();
		return this.importTransactions(this.config.isRemoveDuplicatesEnabled());

	}

	@Override
	public void uploadTransactions(final List<TransactionRecord> transactions) throws IOException {
		final File folder = FileUtils.createTempDir();
		folder.deleteOnExit();
		final String outputFileName = folder.getAbsolutePath() + File.separator + "freeagent.csv";

		this.getQifFileFormatter().format(transactions, outputFileName);

		this.uploadToClearCheckbook(outputFileName);
	}

}
