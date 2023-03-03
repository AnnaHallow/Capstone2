package com.techelevator.tenmo.tenmo.controller;

import com.techelevator.tenmo.tenmo.model.Transfer;
import com.techelevator.tenmo.tenmo.pojos.TransferRequest;
import com.techelevator.tenmo.tenmo.services.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
public class TransferController {

	TransferService transferService;

	public TransferController(TransferService transferService) {
		this.transferService = transferService;
	}

	@GetMapping("/history/{accountId}")
	public List<Transfer> getHistory(@PathVariable int accountId){
		return transferService.getHistory(accountId);
	}

	@PostMapping("/addtransfer")
	public Transfer addTransfer(@RequestBody TransferRequest transferRequest){
		return transferService.saveTransfer(transferRequest);
	}

	@PostMapping("/updatetransfer")
	public Transfer updateTransfer(@RequestBody Transfer transfer){
		return transferService.updateTransfer(transfer);
	}

	@GetMapping("/pendingfrom/{accountId}")
	public List<Transfer> getPendingFrom(@PathVariable int accountFrom){
		return transferService.getPendingFrom(accountFrom);
	}

	@GetMapping("/pendingto/{accountId}")
	public List<Transfer> getPendingTo(@PathVariable int accountTo){
		return transferService.getPendingTo(accountTo);
	}
}
