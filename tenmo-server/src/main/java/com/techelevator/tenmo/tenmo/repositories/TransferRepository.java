package com.techelevator.tenmo.tenmo.repositories;

import com.techelevator.tenmo.tenmo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {


	List<Transfer> findAllByAccountFromOrAccountTo(int accountFrom, int accountTo);

	List<Transfer> findAllByAccountFromAndTransferStatusId(int accountFrom, int transferStatusId);

	List<Transfer> findAllByAccountToAndTransferStatusId(int accountTo, int transferStatusId);
}
