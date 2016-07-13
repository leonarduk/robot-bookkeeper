/**
 * ClearCheckBookApiClientIT
 *
 * @author ${author}
 * @since 11-Jul-2016
 */
package com.leonarduk.bookkeeper.web.download.clearcheckbook;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonarduk.bookkeeper.email.SitConfig;
import com.leonarduk.bookkeeper.file.TransactionRecord;
import com.leonarduk.bookkeeper.web.upload.clearcheckbook.ClearCheckbookConfig;
import com.leonarduk.clearcheckbook.ClearcheckbookException;
import com.leonarduk.clearcheckbook.dto.AccountDataType;
import com.leonarduk.clearcheckbook.dto.TransactionDataType;
import com.leonarduk.webscraper.core.config.Config;

public class ClearCheckBookApiClientIT {

	private ClearCheckBookApiClient	client;
	private ClearCheckbookConfig	ccbConfig;
	private Config					config;
	private String					accountName;
	private double					initialBalance;

	public TransactionRecord createTestRecord() {
		final Date date2 = new Date();

		final TransactionRecord record = new TransactionRecord(12.34, "test payment", date2, "123",
		        "test payee");
		return record;
	}

	public String insertRecord() throws ClearcheckbookException {
		final TransactionRecord record = this.createTestRecord();
		final String id = this.client.insertRecord(record, this.accountName);
		return id;
	}

	@Before
	public void setUp() throws Exception {
		this.config = SitConfig.getSitConfig();
		this.ccbConfig = new ClearCheckbookConfig(this.config);
		this.client = new ClearCheckBookApiClient(this.ccbConfig);
		this.accountName = "TestAccount" + Math.random();
		this.initialBalance = 100.23;
		this.client.createAccount(this.accountName, AccountDataType.Type.CHECKING,
		        this.initialBalance);
	}

	@After
	public void tearDown() throws Exception {
		this.client.deleteAccount(this.accountName);
	}

	@Test
	public final void testConvertToTransactionDataType() throws ClearcheckbookException {
		final double amount = 12.45;
		final String description = "test income";
		final Date date2 = new Date();
		final String checkNumber = "123";
		final String payee = "test payee";
		final TransactionRecord record = new TransactionRecord(amount, description, date2,
		        checkNumber, payee);

		final AccountDataType account = this.client
		        .getAccount(this.config.getProperty("bookkeeper.web.clearcheckbook.amex"));
		final TransactionDataType actual = this.client.convertToTransactionDataType(record,
		        account);
		Assert.assertEquals(Double.valueOf(amount), actual.getAmount());
	}

	@Test
	public final void testGetAccount() throws ClearcheckbookException {
		final AccountDataType account = this.client.getAccount(this.accountName);
		Assert.assertEquals(this.accountName, account.getName());
	}

	@Test
	public final void testGetBalanceForAccount() throws ClearcheckbookException {
		final AccountDataType account = this.client.getAccount(this.accountName);
		System.out.println(account + " " + account.getCurrentBalance());
		Assert.assertTrue(account.getCurrentBalance().doubleValue() == this.initialBalance);
	}

	@Test
	public void testGetTransactionRecordsForAccount() throws Exception {
		final List<TransactionRecord> records = this.client
		        .getTransactionRecordsForAccount(this.client.getAccount(this.accountName), 100);
		System.out.println(records);
		Assert.assertEquals(1, records.size());

		Assert.assertTrue(records.contains(records.get(0)));
	}

	@Test
	public final void testInsertRecord() throws ClearcheckbookException {
		final String id = this.insertRecord();
		Assert.assertNotNull(id);
	}

	@Test
	public final void testInsertRecordsDuplicates() throws ClearcheckbookException, ParseException {
		final List<TransactionRecord> records = new ArrayList<>();
		final TransactionRecord record = this.createTestRecord();
		records.add(record);
		records.add(record);

		final List<TransactionRecord> ids = this.client.insertRecords(records, this.accountName,
		        100);
		Assert.assertEquals(1, ids.size());
		System.out.println(this.client
		        .getTransactionRecordsForAccount(this.client.getAccount(this.accountName), 10));
		final List<TransactionRecord> ids2 = this.client.insertRecords(records, this.accountName,
		        100);
		System.out.println(this.client
		        .getTransactionRecordsForAccount(this.client.getAccount(this.accountName), 10));
		System.out.println(record);
		Assert.assertEquals(0, ids2.size());
	}

	@Test
	public final void testInsertRecordsNoDuplicates()
	        throws ClearcheckbookException, ParseException {
		final List<TransactionRecord> records = new ArrayList<>();
		records.add(new TransactionRecord(12.34, "test deposit", new Date(),
		        String.valueOf(Math.round(Math.random() * 100000000)), "test payer"));
		records.add(new TransactionRecord(-12.34, "test payment", new Date(),
		        String.valueOf(Math.round(Math.random() * 100000000)), "test payee"));

		final List<TransactionRecord> ids = this.client.insertRecords(records, this.accountName,
		        100);
		Assert.assertEquals(records.size(), ids.size());

	}

	@Test
	public final void testInsertRecordWithCompare() throws ClearcheckbookException, ParseException {
		final List<TransactionRecord> records = this.client
		        .getTransactionRecordsForAccount(this.client.getAccount(this.accountName), 10);

		final TransactionRecord record = this.createTestRecord();
		final String id = this.client.insertRecord(record, this.accountName);
		Assert.assertNotNull(id);
		final List<TransactionRecord> records2 = this.client
		        .getTransactionRecordsForAccount(this.client.getAccount(this.accountName), 10);
		System.out.println("was " + records);
		System.out.println("now " + records2);
		Assert.assertTrue("Was " + records.size() + " now " + records2.size(),
		        records2.size() == (records.size() + 1));
		System.out.println(records2);
		Assert.assertTrue("Cant find " + record + " in " + records2, records2.contains(record));

	}

}
