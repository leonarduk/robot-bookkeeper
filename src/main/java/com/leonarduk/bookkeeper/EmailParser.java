/**
 * All rights reserved. @Leonard UK Ltd.
 */
package com.leonarduk.bookkeeper;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.leonarduk.bookkeeper.email.BFCAInvoiceProcessor;
import com.leonarduk.bookkeeper.email.VodafoneProcessor;
import com.leonarduk.core.config.Config;
import com.leonarduk.core.email.EmailProcessorChain;
import com.leonarduk.core.email.EmailReader;
import com.leonarduk.core.email.EmailReader.ServerType;
import com.leonarduk.core.email.SimplePrintEmailProcessor;

/**
 * The Class EmailParser.
 *
 * @author Stephen Leonard
 * @version $Author: $: Author of last commit
 * @version $Rev: $: Revision of last commit
 * @version $Date: $: Date of last commit
 * @since 3 Feb 2015
 */
public final class EmailParser {
    /** The Constant LOGGER. */
    static final Logger LOGGER = Logger.getLogger(EmailParser.class);

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static void main(final String[] args) throws IOException {
        final Config config = new Config("bookkeeper.properties");
        EmailParser.processEmails(config);
    }

    /**
     * Process emails.
     *
     * @param config
     *            the config
     */
    public static void processEmails(final Config config) {
        // final String[] toEmail =
        // config.getArrayProperty("bookkeeper.email.to");
        final String userName = config.getProperty("bookkeeper.email.user");
        final String server = config.getProperty("bookkeeper.email.server");
        final String serverTypeName =
                config.getProperty("bookkeeper.email.server.type");
        final String password = config.getProperty("bookkeeper.email.password");
        // final String port = config.getProperty("bookkeeper.email.port");

        final ServerType serverType =
                EmailReader.ServerType.valueOf(serverTypeName);
        final EmailReader reader = new EmailReader();
        final String attachmentsFolder =
                config.getProperty("bookkeeper.email.attachments.savedir");
        final EmailProcessorChain processor = new EmailProcessorChain();
        processor.addProcessor(new SimplePrintEmailProcessor());
        processor.addProcessor(new BFCAInvoiceProcessor());
        processor.addProcessor(new VodafoneProcessor());

        reader.processMail(server, userName, password, serverType,
                attachmentsFolder, processor);
        // final EmailSender emailSender = new EmailSender();

        // final EmailSession session = new EmailSession(userName, password,
        // server, port);
        // emailSender.sendMessage(config.getProperty("bookkeeper.email.from.email"),
        // config.getProperty("bookkeeper.email.from.name"),
        // "Matching bookkeeper items found", emailBody.toString(), true,
        // session, toEmail);
    }

    /**
     * Instantiates a new email parser.
     */
    private EmailParser() {
    }
}
