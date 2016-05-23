/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import com.leonarduk.core.config.Config;

/**
 * The Class FreeagentLoginTest.
 *
 * @author stephen
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 4 Mar 2015
 */
public class FreeagentLoginTest {

    /** The login. */
    private FreeAgentUploadTransactions login;

    /** The web driver. */
    private WebDriver webDriver;

    /** The config. */
    private Config config;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public final void setUp() throws Exception {
        this.webDriver = Mockito.mock(WebDriver.class);

        this.config = new Config("bookkeeper.properties");

        this.login =
                new FreeAgentUploadTransactions(new FreeAgentLogin(
                        this.webDriver, this.config));
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {
        // this.login.getWebDriver().close();
    }

    /**
     * Test is loaded.
     */
    @Test
    @Ignore
    // get Jenkins working for now
            public final
            void
            testIsLoaded() {
        this.login.get();
        this.login
                .uploadTransactions("/tmp/heaDd2/Statements09015613132580.qif");
    }

    // @Test
    /**
     * Test santander login.
     */
    public final void testSantanderLogin() {
        Assert.assertNotNull(this.login);
    }

}
