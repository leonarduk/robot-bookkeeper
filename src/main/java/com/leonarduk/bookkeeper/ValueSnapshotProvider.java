/**
 * ValueSnapshotProvider
 *
 * @author ${author}
 * @since 04-Jul-2016
 */
package com.leonarduk.bookkeeper;

import java.io.IOException;

public interface ValueSnapshotProvider {

	double getCurrentValue() throws IOException;

	String getDescription();

}
