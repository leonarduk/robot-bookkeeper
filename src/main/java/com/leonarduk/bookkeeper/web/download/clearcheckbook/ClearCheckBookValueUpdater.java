/**
 * ClearCheckBookValueUpdater
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.StringUtils;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckBookLogin;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.web.BaseSeleniumPage;

public class ClearCheckBookValueUpdater implements AutoCloseable, TransactionDownloader {

	private final static Logger LOGGER = Logger.getLogger(ClearCheckBookValueUpdater.class);

	private static final String CCB_ACCOUNT_OVERVIEW = "//*[@id=\"account-overviews\"]/div[";

	private final ValueSnapshotProvider valueSnapshotProvider;

	private final ClearCheckbookConfig config;

	private final String accountname;

	private final ClearCheckBookLogin login;

	public ClearCheckBookValueUpdater(final ValueSnapshotProvider valueSnapshotProvider,
	        final ClearCheckbookConfig config, final String ccbAccountName) {
		this.config = config;
		this.valueSnapshotProvider = valueSnapshotProvider;
		this.login = new ClearCheckBookLogin(config);
		this.accountname = ccbAccountName;
	}

	@Override
	public void close() throws Exception {
		this.config.getWebDriver().close();
	}

	@Override
	public List<TransactionRecord> downloadTransactions() throws IOException {
		ClearCheckBookValueUpdater.LOGGER.info("downloadTransactions");
		final List<TransactionRecord> updates = new ArrayList<>();
		this.login.login();

		final WebDriver driver = this.config.getWebDriver();

		try {
			driver.findElement(By.linkText("Account Overviews:"))
			        .findElement(By.linkText(this.accountname)).click();
		}
		catch (final NoSuchElementException e) {
			throw new IOException("Cannot find account called " + this.accountname);
		}
		BaseSeleniumPage.waitForPageToLoad(driver);

		final WebElement valueElement = driver
		        .findElement(By.xpath("/html/body/div[2]/div[5]/div[1]/div/div[2]/div[1]/div[2]"));
		final String ccbValueString = valueElement.getText();

		// driver.findElement(By.linkText("Enter a Transaction")).click();
		driver.findElement(By.id("amount")).clear();
		final double ccbAmount = StringUtils.convertMoneyString(ccbValueString);
		final double amount = this.valueSnapshotProvider.getCurrentValue() - ccbAmount;
		if (Math.abs(amount) < 1) {
			ClearCheckBookValueUpdater.LOGGER.info("No change");
			return updates;
		}
		final int veryLargeChange = 100000;
		if (Math.abs(amount) > veryLargeChange) {
			ClearCheckBookValueUpdater.LOGGER
			        .warn("Suspected error, ignoring move from " + ccbAmount + " to " + amount);
			return updates;
		}
		ClearCheckBookValueUpdater.LOGGER
		        .info("Updating value from " + ccbAmount + " to " + amount);

		final Date date2 = new Date();

		updates.add(new TransactionRecord(amount, this.valueSnapshotProvider.getDescription(),
		        date2, "", ""));
		return updates;
	}

	@Override
	public String downloadTransactionsFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
