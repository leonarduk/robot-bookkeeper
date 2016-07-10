/**
 * ClearCheckBookApiClient
 *
 * @author ${author}
 * @since 10-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.clearcheckbook.ClearCheckBookConnection;
import com.leonarduk.clearcheckbook.ClearcheckbookException;
import com.leonarduk.clearcheckbook.calls.AccountCall;

public class ClearCheckBookApiClient {
	private final AccountCall accountCall;

	public ClearCheckBookApiClient(final ClearCheckbookConfig config) {
		this(config.getUserName(), config.getPassword());
	}

	public ClearCheckBookApiClient(final String user, final String password) {
		final ClearCheckBookConnection connection = new ClearCheckBookConnection(user, password);
		this.accountCall = new AccountCall(connection);
	}

	public double getBalanceForAccount(final String accountName) throws ClearcheckbookException {
		return this.accountCall.get(accountName).getCurrentBalance().doubleValue();
	}
}
