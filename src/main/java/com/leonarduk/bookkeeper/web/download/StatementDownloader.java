/**
 * StatementDownloader
 *
 * @author ${author}
 * @since 01-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download;

import java.io.IOException;

public interface StatementDownloader {

	void downloadLatestStatement() throws IOException;

}
