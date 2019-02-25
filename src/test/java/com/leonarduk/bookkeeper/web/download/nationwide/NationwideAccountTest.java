///**
// * NationwideAccountTest
// *
// * @author ${author}
// * @since 03-Aug-2016
// */
//package com.leonarduk.bookkeeper.web.download.nationwide;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import com.leonarduk.bookkeeper.file.TransactionRecord;
//
//public class NationwideAccountTest {
//
//	private NationwideAccount account;
//
//	@Before
//	public void setUp() throws Exception {
//		final NationwideConfig nationwideConfig = Mockito.mock(NationwideConfig.class);
//		final NationwideLogin aLogin = new NationwideLogin(nationwideConfig);
//		final File resourceFolder = new File(
//		        this.getClass().getClassLoader().getResource("nationwide").getFile());
//		Mockito.when(nationwideConfig.getDownloadDir()).thenReturn(resourceFolder);
//		final int aAccountId = 1;
//		this.account = new NationwideAccount(aLogin, aAccountId);
//	}
//
//	@Test
//	public final void testParseDownloadedFile() throws IOException {
//		final List<TransactionRecord> records = this.account.parseDownloadedFile();
//		Assert.assertEquals(2, records.size());
//	}
//
//}
