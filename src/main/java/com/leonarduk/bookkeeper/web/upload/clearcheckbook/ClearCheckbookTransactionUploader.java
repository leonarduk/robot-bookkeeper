/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
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
	static final By IMPORT_TO_ACCOUNT_LINK = By.name("import_to_account");
	static final By UPLOAD_BUTTON = By.xpath("//*[@id=\"uploadForm\"]/button");
	static final By IMPORT_LINK = By.id("import");
	static final By IMPORT_PAGE = By.linkText("Import Transactions");
	static final By TOOLS_PAGE = By.linkText("Tools");
	/** The Constant LOGGER. */
	private final static Logger			LOGGER	= Logger
	        .getLogger(ClearCheckbookTransactionUploader.class);
	private final ClearCheckbookConfig	config;
	private String						account;
	private final ClearCheckBookLogin	login;

	public ClearCheckbookTransactionUploader(final ClearCheckbookConfig config) {
		this.config = config;
		this.login = new ClearCheckBookLogin(config);
	}

	/**
	 * Choose file to upload.
	 *
	 * @param fileToUpload
	 *            the file to upload
	 * @throws IOException
	 */
	private void chooseFileToUpload(final String fileToUpload) throws IOException {
		final WebDriver driver = this.config.getWebDriver();
		driver.findElement(TOOLS_PAGE).click();
		driver.findElement(IMPORT_PAGE).click();
		final String replaceAll = fileToUpload.replaceAll("/", "\\\\");
		driver.findElement(IMPORT_LINK).sendKeys(replaceAll);
		// *[@id="import"]
		driver.findElement(UPLOAD_BUTTON).click();
	}

	@Override
	public void close() throws Exception {
		this.config.getWebDriver().close();
	}

	private void generalSettings() throws IOException {
		try {
			new Select(this.config.getWebDriver().findElement(IMPORT_TO_ACCOUNT_LINK))
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
	 * @throws IOException
	 */
	private String importTransactions(final boolean removeDuplicates) throws IOException {
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
	 * Upload to clear checkbook.
	 *
	 * @param fileToUpload
	 *            the file to upload
	 * @return the string
	 * @throws IOException
	 */
	public String uploadToClearCheckbook(final String fileToUpload) throws IOException {
		this.login.login();
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
