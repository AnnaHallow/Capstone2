package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Scanner;

// //Use this to send information, be sure to use correct path, post, get, put ect.
//                HttpEntity<String> entity = getHeaders();
//                currentAccount = restTemplate.exchange(API_BASE_URL + "account/getaccount/" +
//                currentUser.getUser().getId(), HttpMethod.GET, entity, Account.class).getBody();

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final ConsoleService consoleService = new ConsoleService();

    //Sophie
    public List<Transfer> viewTransferHistory(AuthenticatedUser currentUser, Account currentAccount) {
        // TODO Auto-generated method stub
        //Need a List<Transfer> from API
        //Send accountId/ account_id
        //Will require checking accountTo and accountFrom
        //add JWD token
        //throw in transfer service class

        HttpEntity<String> entity = authenticationService.getHeaders(currentUser);
        ResponseEntity<List<Transfer>> transferResponse =
                restTemplate.exchange(API_BASE_URL + "transfer/history/" + currentAccount.getAccountId(),
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Transfer>>() {
                        });
        List<Transfer> transfers = transferResponse.getBody();


        return transfers;
    }

    public int saveTransfer(Transfer transfer, AuthenticatedUser currentUser){
        HttpEntity<String> entity = authenticationService.getHeaders(currentUser);
               int transferId = restTemplate.exchange(API_BASE_URL + "transfer/addtransfer/", transfer,
                        HttpMethod.POST, entity, new ParameterizedTypeReference<List<Transfer>>() {});
    }
}
