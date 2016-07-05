/**
 * ClearCheckBookLogin
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper.web.upload.clearcheckbook;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ClearCheckBookLogin {
	private static final Logger			_logger	= Logger.getLogger(ClearCheckBookLogin.class);
	private final ClearCheckbookConfig	config;

	public ClearCheckBookLogin(final ClearCheckbookConfig config) {
		this.config = config;
	}

	/**
	 * Login.
	 *
	 * @throws IOException
	 */
	public void login() throws IOException {
		final WebDriver driver = this.config.getWebDriver();
		final String baseUrl = "https://www.clearcheckbook.com/";
		final int severalSeconds = 5;
		ClearCheckBookLogin._logger.info("Login to " + baseUrl);
		driver.manage().timeouts().implicitlyWait(severalSeconds, TimeUnit.SECONDS);
		driver.get(baseUrl + "/login");
		ClearCheckBookLogin._logger.info("Try to login");
		final List<WebElement> userNameElement = driver.findElements(By.id("ccb-l-username"));
		if (userNameElement.size() > 0) {
			userNameElement.get(0).sendKeys(this.config.getUserName());
			driver.findElement(By.id("ccb-l-password")).sendKeys(this.config.getPassword());
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
	}

}
