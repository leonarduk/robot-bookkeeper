/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.clearcheckbook;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class UploadToClearCheckbook.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 28 Mar 2015
 */
public class ClearCheckbook {
	/** The Constant LOGGER. */
	private final static Logger			LOGGER	= Logger.getLogger(ClearCheckbook.class);
	private final ClearCheckbookConfig	config;

	public ClearCheckbook(final ClearCheckbookConfig config) {
		this.config = config;
	}

	/**
	 * Amex settings.
	 *
	 * @param account
	 *            the account
	 * @param driver
	 *            the driver
	 */
	private void amexSettings(final String account, final WebDriver driver) {
		try {
			new Select(driver.findElement(By.id("type_4"))).selectByVisibleText("Payee");
			new Select(driver.findElement(By.name("dateFormat"))).selectByVisibleText("dd/mm/yyyy");
			new Select(driver.findElement(By.name("import_to_account")))
			        .selectByVisibleText(account);
		}
		catch (final NoSuchElementException e) {
			ClearCheckbook.LOGGER.error("Failed to find element in amexSettings", e);
			throw e;
		}
	}

	/**
	 * Choose file to upload.
	 *
	 * @param fileToUpload
	 *            the file to upload
	 * @param driver
	 *            the driver
	 */
	private void chooseFileToUpload(final String fileToUpload, final WebDriver driver) {
		driver.findElement(By.linkText("Tools")).click();
		driver.findElement(By.linkText("Import Transactions")).click();
		final String replaceAll = fileToUpload.replaceAll("/", "\\\\");
		driver.findElement(By.id("import")).sendKeys(replaceAll);
		// *[@id="import"]
		driver.findElement(By.xpath("//*[@id=\"uploadForm\"]/button")).click();
	}

	/**
	 * Convert money string.
	 *
	 * @param amount
	 *            the amount
	 * @return the double
	 */
	public double convertMoneyString(final String amount) {
		return Double.valueOf(amount.replaceAll("£", "").replaceAll(",", ""));
	}

	private void generalSettings(final String account, final WebDriver driver) {
		try {
			new Select(driver.findElement(By.name("import_to_account")))
			        .selectByVisibleText(account);
		}
		catch (final NoSuchElementException e) {
			ClearCheckbook.LOGGER.error("Failed to find element in amexSettings", e);
			throw e;
		}
	}

	/**
	 * Import transactions.
	 *
	 * @param driver
	 *            the driver
	 * @param removeDuplicates
	 * @return the string
	 */
	private String importTransactions(final WebDriver driver, final boolean removeDuplicates) {
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
	 *
	 * @param driver
	 *            the driver
	 */
	private void login(final WebDriver driver) {
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
	 * Nationwide settings.
	 *
	 * @param account
	 *            the account
	 * @param driver
	 *            the driver
	 */
	private void nationwideSettings(final String account, final WebDriver driver) {
		try {
			new Select(driver.findElement(By.id("type_5"))).selectByVisibleText("Payee");
			new Select(driver.findElement(By.id("type_4"))).selectByVisibleText("Description");
			new Select(driver.findElement(By.name("dateFormat"))).selectByVisibleText("yyyy-mm-dd");
			new Select(driver.findElement(By.name("import_to_account")))
			        .selectByVisibleText(account);
		}
		catch (final NoSuchElementException e) {
			ClearCheckbook.LOGGER.error("Failed to find element in nationwideSettings", e);
			throw e;
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

		while (duplicates > 0) {
			driver.findElement(By.id("selectAllCheckbox")).click();
			driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
			final String updateFilterXpath = "/html/body/div[2]/div[6]/form/input";
			driver.findElement(By.xpath(updateFilterXpath)).click();

			duplicates = Integer.valueOf(driver.findElement(By.xpath(numberOfDupsXpath)).getText()
			        .replace(" Duplicates Found", "").replace(" Duplicate Found", ""));
		}
	}

	/**
	 * Update estimate.
	 *
	 * @param account
	 *            the account
	 * @param currentValue
	 *            the current value
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @param driver
	 *            the driver
	 * @param valueXpath
	 *            the value xpath
	 * @param memo
	 *            the memo
	 * @return the string
	 */
	public String updateEstimate(final String account, final String currentValue,
	        final WebDriver driver, final String valueXpath, final CharSequence memo) {
		this.login(driver);

		final WebElement valueElement = driver.findElement(By.xpath(valueXpath));
		final String ccbValueString = valueElement.getText();

		// driver.findElement(By.linkText("Enter a Transaction")).click();
		driver.findElement(By.id("amount")).clear();
		final double ccbAmount = this.convertMoneyString(ccbValueString);
		double amount = this.convertMoneyString(currentValue) - ccbAmount;
		if (Math.abs(amount) < 1) {
			ClearCheckbook.LOGGER.info("No change");
			return "No change";
		}
		final int veryLargeChange = 100000;
		if (Math.abs(amount) > veryLargeChange) {
			ClearCheckbook.LOGGER
			        .warn("Suspected error, ignoring move from " + ccbAmount + " to " + amount);
			return "No change";
		}
		ClearCheckbook.LOGGER.info("Updating value from " + ccbAmount + " to " + amount);

		driver.findElement(By.id("amount")).sendKeys(String.valueOf(amount));
		driver.findElement(By.id("memo")).sendKeys(memo);

		String transactionType = "Deposit";
		if (amount < 0) {
			transactionType = "Withdrawal";
			amount *= -1;
		}

		new Select(driver.findElement(By.id("transaction_type")))
		        .selectByVisibleText(transactionType);
		new Select(driver.findElement(By.id("account_id"))).selectByVisibleText(account);
		new Select(driver.findElement(By.id("category_id"))).selectByVisibleText("Miscellaneous");
		driver.findElement(By.id("at_jive")).click();
		driver.findElement(By.id("addEntryButton")).click();

		return transactionType + ":" + amount;

	}

	/**
	 * Upload to clear checkbook.
	 *
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 * @param account
	 *            the account
	 * @param fileToUpload
	 *            the file to upload
	 * @param driver
	 *            the driver
	 * @param setting
	 *            the setting
	 * @param removeDuplicates
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public String uploadToClearCheckbook(final String account, final String fileToUpload,
	        final WebDriver driver, final Setting setting, final boolean removeDuplicates)
	                throws Exception {
		this.login(driver);
		this.chooseFileToUpload(fileToUpload, driver);
		switch (setting) {
			case AMEX:
				this.amexSettings(account, driver);
				break;
			case NATIONWIDE:
				this.nationwideSettings(account, driver);
				break;
			default:
				this.generalSettings(account, driver);
		}

		return this.importTransactions(driver, removeDuplicates);

	}

	/**
	 * The Enum Setting.
	 */
	public enum Setting {

		/** The amex. */
		AMEX, /** The nationwide. */
		NATIONWIDE, GENERAL;
	}
}
