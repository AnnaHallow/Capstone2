package com.techelevator.tenmo.tenmo.pojos;

import java.math.BigDecimal;
 // pojo without transferId used to save new transfers
public class TransferRequest {

	public int transferTypeId;

	public int transferStatusId;

	public int accountFrom;

	public int accountTo;

	public BigDecimal amount;

	 public int getTransferTypeId() {
		 return transferTypeId;
	 }

	 public void setTransferTypeId(int transferTypeId) {
		 this.transferTypeId = transferTypeId;
	 }

	 public int getTransferStatusId() {
		 return transferStatusId;
	 }

	 public void setTransferStatusId(int transferStatusId) {
		 this.transferStatusId = transferStatusId;
	 }

	 public int getAccountFrom() {
		 return accountFrom;
	 }

	 public void setAccountFrom(int accountFrom) {
		 this.accountFrom = accountFrom;
	 }

	 public int getAccountTo() {
		 return accountTo;
	 }

	 public void setAccountTo(int accountTo) {
		 this.accountTo = accountTo;
	 }

	 public BigDecimal getAmount() {
		 return amount;
	 }

	 public void setAmount(BigDecimal amount) {
		 this.amount = amount;
	 }
 }

