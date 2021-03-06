/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.leonarduk.bookkeeper.file.QifFileFormatter;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.download.clearcheckbook.ClearCheckBookApiClient;
import com.leonarduk.bookkeeper.web.upload.TransactionUploader;
import com.leonarduk.clearcheckbook.ClearcheckbookException;
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
public class ClearCheckbookTransactionUploader implements AutoCloseable, TransactionUploader {

	/** The Constant IMPORT_TO_ACCOUNT_LINK. */
	static final By IMPORT_TO_ACCOUNT_LINK = By.name("import_to_account");

	/** The Constant UPLOAD_BUTTON. */
	static final By UPLOAD_BUTTON = By.xpath("//*[@id=\"uploadForm\"]/button");

	/** The Constant IMPORT_LINK. */
	static final By IMPORT_LINK = By.id("import");

	/** The Constant IMPORT_PAGE. */
	static final By IMPORT_PAGE = By.linkText("Import Transactions");

	/** The Constant TOOLS_PAGE. */
	static final By TOOLS_PAGE = By.linkText("Tools");
	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(ClearCheckbookTransactionUploader.class);

	/** The config. */
	private final ClearCheckbookConfig config;

	/** The account. */
	private final String account;

	/** The login. */
	private final ClearCheckBookLogin login;

	public ClearCheckbookTransactionUploader(final ClearCheckbookConfig config, final String account) {
		this.config = config;
		this.login = new ClearCheckBookLogin(config);
		this.account = account;
	}

	/**
	 * Choose file to upload.
	 *
	 * @param fileToUpload the file to upload
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void chooseFileToUpload(final String fileToUpload) throws IOException {
		final WebDriver driver = this.config.getWebDriver();
		driver.findElement(ClearCheckbookTransactionUploader.TOOLS_PAGE).click();
		driver.findElement(ClearCheckbookTransactionUploader.IMPORT_PAGE).click();
		final String replaceAll = fileToUpload.replaceAll("/", "\\\\");
		driver.findElement(ClearCheckbookTransactionUploader.IMPORT_LINK).sendKeys(replaceAll);
		// *[@id="import"]
		driver.findElement(ClearCheckbookTransactionUploader.UPLOAD_BUTTON).click();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		try {
			this.config.getWebDriver().close();
		} catch (final Throwable r) {
		}
	}

	/**
	 * General settings.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void generalSettings() throws IOException {
		try {
			new Select(this.config.getWebDriver().findElement(ClearCheckbookTransactionUploader.IMPORT_TO_ACCOUNT_LINK))
					.selectByVisibleText(this.account);
		} catch (final NoSuchElementException e) {
			ClearCheckbookTransactionUploader.LOGGER.error("Failed to find element in amexSettings", e);
			throw e;
		}
	}

	/**
	 * Gets the qif file formatter.
	 *
	 * @return the qif file formatter
	 */
	public QifFileFormatter getQifFileFormatter() {
		return new QifFileFormatter(QifFileFormatter.CCB_FORMAT);
	}

	/**
	 * Import transactions.
	 *
	 * @param removeDuplicates the remove duplicates
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
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
			final String results = driver.findElement(By.xpath("//*[@id=\"importTransactions\"]")).getText();
			driver.findElement(By.name("submit[all]")).click();
			return results;
		}
		return "No transactions added";
	}

	/**
	 * Removes the duplicates by page.
	 *
	 * @param driver the driver
	 */
	private void removeDuplicatesByPage(final WebDriver driver) {
		final String numberOfDupsXpath = "/html/body/div[2]/h3";
		Integer duplicates = Integer.valueOf(driver.findElement(By.xpath(numberOfDupsXpath)).getText()
				.replace(" Duplicates Found", "").replace(" Duplicate Found", ""));

		while (duplicates.intValue() > 0) {
			driver.findElement(By.id("selectAllCheckbox")).click();
			driver.findElement(By.cssSelector("input.btn.btn-primary")).click();
			final String updateFilterXpath = "/html/body/div[2]/div[6]/form/input";
			driver.findElement(By.xpath(updateFilterXpath)).click();

			duplicates = Integer.valueOf(driver.findElement(By.xpath(numberOfDupsXpath)).getText()
					.replace(" Duplicates Found", "").replace(" Duplicate Found", ""));
		}
	}

	/**
	 * Upload to clear checkbook.
	 *
	 * @param fileToUpload the file to upload
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String uploadToClearCheckbook(final String fileToUpload) throws IOException {
		this.login.login();
		this.chooseFileToUpload(fileToUpload);
		this.generalSettings();
		return this.importTransactions(this.config.isRemoveDuplicatesEnabled());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.leonarduk.bookkeeper.web.upload.TransactionUploader#uploadTransactions(
	 * java.util.List)
	 */
	@Override
	public List<TransactionRecord> writeTransactions(final List<TransactionRecord> transactions, final String fileName,
			TransactionRecordFilter filter) throws IOException {
		try {
			return this.uploadViaApi(transactions, filter);
		} catch (final ParseException e) {
			throw new IOException(e);
		}
	}

	public List<TransactionRecord> uploadViaApi(final List<TransactionRecord> transactions,
			TransactionRecordFilter filter) throws IOException, ParseException {
		final ClearCheckBookApiClient apiClient = new ClearCheckBookApiClient(this.config);
		try {
			return apiClient.insertRecords(transactions, this.account, 500, filter);
		} catch (final ClearcheckbookException e) {
			throw new IOException("Failed to save transactions", e);
		}
	}

}
