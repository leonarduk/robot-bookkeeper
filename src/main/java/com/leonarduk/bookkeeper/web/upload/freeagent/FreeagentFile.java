package com.leonarduk.bookkeeper.web.upload.freeagent;

import java.io.File;

public class FreeagentFile {

	final private File freeagentFile;

	public FreeagentFile(File freeagentFile) {
		this.freeagentFile = freeagentFile;
	}

	public FreeagentFile(final String freeagentFileName) {
		this(new File(freeagentFileName));
	}

	public File getFreeagentFile() {
		return freeagentFile;
	}
}
