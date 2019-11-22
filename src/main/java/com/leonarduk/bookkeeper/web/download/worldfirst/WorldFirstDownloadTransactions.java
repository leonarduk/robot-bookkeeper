/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web.download.worldfirst;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.leonarduk.bookkeeper.ValueSnapshotProvider;
import com.leonarduk.bookkeeper.file.QifFileParser;
import com.leonarduk.bookkeeper.file.StringConversionUtils;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.file.TransactionRecordFilter;
import com.leonarduk.bookkeeper.web.download.StatementDownloader;
import com.leonarduk.bookkeeper.web.download.TransactionDownloader;
import com.leonarduk.web.BasePage;
import com.leonarduk.web.BaseSeleniumPage;
import com.leonarduk.web.SeleniumBrowserController;

/**
 * The Class AmexDownloadTransactions.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 24 Mar 2015
 */
public class WorldFirstDownloadTransactions extends BasePage<WorldFirstDownloadTransactions>
		implements TransactionDownloader, StatementDownloader, AutoCloseable, ValueSnapshotProvider {
	private static final Logger LOGGER = Logger.getLogger(WorldFirstDownloadTransactions.class);

	private final WorldFirstConfig config;

	/**
	 * Instantiates a new amex download transactions.
	 *
	 * @param config the config
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public WorldFirstDownloadTransactions(final WorldFirstConfig config) throws IOException {
		super(new SeleniumBrowserController(config.getWebDriver(config.getDownloadDir())), config.getBaseUrl());
		final int fewSeconds = 3;
		this.config = config;
		this.config.getWebDriver().manage().timeouts().implicitlyWait(fewSeconds, TimeUnit.SECONDS);
	}

	@Override
	public void close() throws Exception {
		this.config.getWebDriver().close();
	}

	@Override
	public void downloadLatestStatement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<TransactionRecord> saveTransactions(TransactionRecordFilter filter) throws IOException {
		final QifFileParser parser = new QifFileParser();
		final String fileName = this.downloadTransactionsFile();
		BaseSeleniumPage.waitForPageToLoad(this.config.getWebDriver());
		LOGGER.info(fileName);
		return parser.parse(fileName, filter);
	}

	/**
	 * Download transactions.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public String downloadTransactionsFile() throws IOException {
		this.load();
		String accountId2 = this.config.getAccountId();
		String url = this.config.getBaseUrl() + "/ecommerce/history?id=" + accountId2 + "&submit=1";
		this.config.getWebDriver().get(url);

		BaseSeleniumPage.waitForPageToLoad(this.config.getWebDriver());

		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
		String url2 = this.config.getBaseUrl() + "/ecommerce/history?id=" + accountId2 + "&start_date="
				+ LocalDate.now().minusMonths(5).format(formatter) + "&end_date=" + LocalDate.now().format(formatter)
				+ "&export=1&data-filename=statement-" + accountId2 + ".csv";
		LOGGER.info(url2);
		this.config.getWebDriver().get(url2);
		return this.config.getDownloadDir() + File.separator + this.config.getDownloadDir().list()[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.bookkeeper.ValueSnapshotProvider#getCurrentValue()
	 */
	@Override
	public double getCurrentValue() throws IOException {
		final String xpathToBalance = "//*[@id=\"card-balance\"]/h3/span[2]";
		final String overviewUrl = this.config.getAccountSummaryUrl();
		WorldFirstDownloadTransactions.LOGGER.info("Get current balance from " + overviewUrl);
		final WebDriver webDriver = this.config.getWebDriver();
		webDriver.get(overviewUrl);
		final WebElement amountNode = webDriver.findElement(By.xpath(xpathToBalance));
		final String amountString = amountNode.getText();
		return StringConversionUtils.convertMoneyString(amountString);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.leonarduk.bookkeeper.ValueSnapshotProvider#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Account adjustment to reconcile with Amex site";
	}

	@Override
	protected void load() throws IOException {
		String url = this.config.getBaseUrl() + "/auth/login?lang=en_GB";
		this.config.getWebDriver().get(url);
		this.config.getWebDriver().findElement(By.id("login_username")).clear();
		this.config.getWebDriver().findElement(By.id("login_username")).sendKeys(this.config.getUserName());
		this.config.getWebDriver().findElement(By.id("login_password")).clear();
		this.config.getWebDriver().findElement(By.id("login_password")).sendKeys(this.config.getPassword());
		this.config.getWebDriver().findElement(By.id("submit")).click();

		this.waitForPageToLoad();
		String question = this.config.getWebDriver()
				.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div[1]/div/div/div/h2/span")).getText();

		String answer = this.config.getAnswer(question);
		WorldFirstDownloadTransactions.LOGGER.info(question + "=" + answer);

		String questionDetail = this.config.getWebDriver()
				.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div[1]/div/div/form/fieldset/div[1]/h2"))
				.getText();

		List<Integer> fields = questionDetail.lines().filter(line -> line.startsWith("Please select")).map(
				line -> this.keepNumberOnly(line.replaceAll("Please select the ", "").replaceAll(" character", "")))
				.collect(Collectors.toList());

		for (int index = 0; index < fields.size(); index++) {
			String character = Character.toString(answer.charAt(fields.get(index) - 1));
			this.findElementByXpath("//*[@id=\"" + index + "\"]").sendKeys(character);

		}
		this.findElementByXpath("//*[@id=\"submit\"]").click();
		this.waitForPageToLoad();
	}

}
