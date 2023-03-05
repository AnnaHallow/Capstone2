package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
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

        HttpEntity<Object> entity = authenticationService.getHeaders(currentUser);
        ResponseEntity<List<Transfer>> transferResponse =
                restTemplate.exchange(API_BASE_URL + "transfer/history/" + currentAccount.getAccountId(),
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Transfer>>() {
                        });
        List<Transfer> transfers = transferResponse.getBody();


        return transfers;
    }

    public int saveTransfer(Transfer transfer, AuthenticatedUser currentUser){
        HttpEntity<Transfer> entity = updateTransfer(currentUser);
               Transfer transferId = restTemplate.exchange(API_BASE_URL + "transfer/addtransfer/",
                        HttpMethod.POST, entity, new ParameterizedTypeReference<Transfer>() {}).getBody();
               return transferId.getTransferId();
    }

    public int updateTransfer(Transfer transferToUpdate, AuthenticatedUser authenticatedUser) {
        int id = 0;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + authenticatedUser.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(transferToUpdate, headers);
            Transfer a = restTemplate.exchange(API_BASE_URL + "transfer/savetransfer/",
                    HttpMethod.POST, entity, Transfer.class).getBody();
            id = a.getTransferId();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
            return id;
    }
}
