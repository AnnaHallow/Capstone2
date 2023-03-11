package com.techelevator.tenmo.tenmo.controller;

import com.techelevator.tenmo.tenmo.model.Transfer;
import com.techelevator.tenmo.tenmo.pojos.TransferRequest;
import com.techelevator.tenmo.tenmo.services.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferControllerTests {

	@Mock
	private TransferService transferService;

	private TransferController transferController;

	@BeforeEach
	public void setUp() {

		transferController = new TransferController(transferService);
	}

	@Test
	public void testGetHistory() {
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfer.setTransferId(1);
		transfer.setAccountFrom(1);
		transfer.setAccountTo(2);
		transfer.setAmount(BigDecimal.valueOf(50.00));
		transfer.setTransferTypeId(2);
		transfer.setTransferStatusId(2);
		transfers.add(transfer);
		when(transferService.getHistory(anyInt())).thenReturn(transfers);

		List<Transfer> result = transferController.getHistory(1);

		assertNotNull(result);
		assertEquals(transfers, result);
	}

	@Test
	public void testAddTransfer() {
		TransferRequest transferRequest = new TransferRequest();
		transferRequest.setAccountFrom(1);
		transferRequest.setAccountTo(2);
		transferRequest.setAmount(BigDecimal.valueOf(50.00));
		Transfer transfer = new Transfer();
		transfer.setTransferId(1);
		transfer.setAccountFrom(1);
		transfer.setAccountTo(2);
		transfer.setAmount(BigDecimal.valueOf(50.00));
		transfer.setTransferTypeId(2);
		transfer.setTransferStatusId(2);
		when(transferService.saveTransfer(any())).thenReturn(transfer);

		Transfer result = transferController.addTransfer(transferRequest);

		assertNotNull(result);
		assertEquals(transfer, result);
	}

	@Test
	public void testUpdateTransfer() {
		Transfer transfer = new Transfer();
		transfer.setTransferId(1);
		transfer.setAccountFrom(1);
		transfer.setAccountTo(2);
		transfer.setAmount(BigDecimal.valueOf(50.00));
		transfer.setTransferTypeId(2);
		transfer.setTransferStatusId(2);
		when(transferService.updateTransfer(any())).thenReturn(transfer);

		Transfer result = transferController.updateTransfer(transfer);

		assertNotNull(result);
		assertEquals(transfer, result);
	}

	@Test
	public void testGetPendingFrom() {
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfer.setTransferId(1);
		transfer.setAccountFrom(1);
		transfer.setAccountTo(2);
		transfer.setAmount(BigDecimal.valueOf(50.00));
		transfer.setTransferTypeId(2);
		transfer.setTransferStatusId(1);
		transfers.add(transfer);
		when(transferService.getPendingFrom(anyInt())).thenReturn(transfers);

		List<Transfer> result = transferController.getPendingFrom(1);

		assertNotNull(result);
		assertEquals(transfers, result);
	}
}

