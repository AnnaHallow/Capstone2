package com.techelevator.tenmo.tenmo.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.tenmo.controller.AccountController;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.tenmo.tenmo.model.Account;
import com.techelevator.tenmo.tenmo.services.AccountService;

import static org.junit.Assert.*;

public class AccountControllerTest {

	private AccountService accountService;
	private AccountController accountController;

	@Before
	public void setup() {
		accountService = mock(AccountService.class);
		accountController = new AccountController(accountService);
	}

	@Test
	public void getAccounts_returnsListOfAccounts() {
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(1, 1, BigDecimal.valueOf(1000.0)));
		accounts.add(new Account(2, 2, BigDecimal.valueOf(2000.0)));
		when(accountService.getAccounts()).thenReturn(accounts);

		List<Account> result = accountController.getAccounts();

		assertEquals(2, result.size());
		assertEquals(accounts.get(0), result.get(0));
		assertEquals(accounts.get(1), result.get(1));
	}

	@Test
	public void getAccount_returnsAccount() {
		Account account = new Account(1, 1, BigDecimal.valueOf(1000.0));
		when(accountService.getAccount(1)).thenReturn(account);

		Account result = accountController.getAccount(1);

		assertEquals(account, result);
	}

	@Test
	public void saveAccount_returnsSavedAccount() {
		Account account = new Account(1, 1, BigDecimal.valueOf(1000.0));
		when(accountService.saveAccount(account)).thenReturn(account);

		Account result = accountController.saveAccount(account);

		assertEquals(account, result);
	}

	@Test
	public void getAccountByAccountId_returnsAccount() {
		Account account = new Account(1, 1, BigDecimal.valueOf(1000.0));
		when(accountService.getAccountByAccountId(1)).thenReturn(account);

		Account result = accountController.getAccountByAccountId(1);

		assertEquals(account, result);
	}
}