package com.techelevator.tenmo.tenmo.controller;

import com.techelevator.tenmo.tenmo.model.Account;
import com.techelevator.tenmo.tenmo.services.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

	AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}






	@GetMapping("/getaccount/{userId}")
	public Account getAccount(@PathVariable int userId){
		return accountService.getAccount(userId);
	}

	@PostMapping("/updateaccount")
	public Account saveAccount(@RequestBody Account account){
		return accountService.saveAccount(account);
	}

}
