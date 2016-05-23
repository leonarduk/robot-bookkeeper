/**
 * All rights reserved. @Leonard UK Ltd.
 */
package uk.co.sleonard.accounts.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.leonarduk.core.FileUtils;
import com.leonarduk.web.BaseSeleniumPage;

/**
 * The Class NationwideAccount.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 28 Mar 2015
 */
public class NationwideAccount extends BaseSeleniumPage {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(final String[] args) throws IOException {
		String downloadDir = "/home/stephen/Downloads/";
		File tempDir = FileUtils.createTempDir();
		WebDriver downloadCapableBrowser = new ChromeDriver();
		// SeleniumUtils.getDownloadCapableBrowser(tempDir);
		FileType type = FileType.OFX;
		NationwideAccount nationwideAccount = (NationwideAccount) new NationwideAccount(
				new NationwideLogin(downloadCapableBrowser, "2901722608", "olympia", "971659"), 1, type).get();
		File[] files = tempDir.listFiles();
		if (files.length > 0) {
			files[0].renameTo(
					new File(downloadDir + nationwideAccount.createFileName() + "." + type.name().toLowerCase()));
		}

	}

	/** The Constant _logger. */
	private static final Logger _logger = Logger.getLogger(NationwideAccount.class);

	/** The login. */
	private NationwideLogin login;

	/** The file type. */
	private FileType fileType;

	/**
	 * Instantiates a new nationwide account.
	 *
	 * @param aLogin
	 *            the login
	 * @param aAccountId
	 *            the account id
	 * @param aFileType
	 *            the file type
	 */
	public NationwideAccount(final NationwideLogin aLogin, final int aAccountId, final FileType aFileType) {
		super(aLogin.getWebDriver(),
				"https://onlinebanking.nationwide.co.uk/Transactions/FullStatement/FullStatement/" + aAccountId);
		this.login = aLogin;
		this.fileType = aFileType;
	}

	/**
	 * The Enum FileType.
	 */
	public enum FileType {

		/** The csv. */
		CSV, /** The ofx. */
		OFX;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openqa.selenium.support.ui.LoadableComponent#load()
	 */
	@Override
	protected final void load() {
		if (this.getWebDriver().findElements(By.id("logoutForm")).size() == 0) {
			this.login.get();
		}
		downloadForAccount();
	}

	/**
	 * Download for account.
	 */
	private void downloadForAccount() {
		this.getWebDriver().get(this.getExpectedUrl());
		try {
			this.getWebDriver().switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			_logger.info("no alert to close");
		}
		List<WebElement> downloadLinks = this.getWebDriver()
				.findElements(By.cssSelector("a.downloadFileLink.custom-tooltip-link"));
		if (downloadLinks.size() < 1 || !downloadLinks.get(0).isDisplayed()) {
			return;
		}

		downloadLinks.get(0).click();
		this.getWebDriver().findElement(By.xpath("(//form[@action='/Transactions/FullStatement/DownloadFS'])[2]"))
				.click();
		this.getWebDriver().findElement(By.cssSelector("b.reveal-info-down")).click();
		try {
			this.getWebDriver().findElement(By.linkText("Download " + this.fileType + " file")).click();
		} catch (NoSuchElementException e) {
			_logger.info("no data");
			// Ignore as this means there is no data
		}
	}

	/**
	 * Account name.
	 *
	 * @return the string
	 */
	public final String accountName() {
		get();
		return this.getWebDriver().findElement(By.xpath("//*[@id=\"stageInner\"]/div[3]/h2")).getText();
	}

	/**
	 * Gets the dates.
	 *
	 * @return the dates
	 */
	public final String getDates() {
		get();
		return this.getWebDriver().findElement(By.id("date-display-dates")).getText();
	}

	/**
	 * Creates the file name.
	 *
	 * @return the string
	 */
	public final String createFileName() {
		StringBuilder buf = new StringBuilder();
		buf.append(accountName().replaceAll(" ", "_"));
		buf.append("_");
		buf.append(getDates().replaceAll(" ", "_"));
		return buf.toString();
	}
}
