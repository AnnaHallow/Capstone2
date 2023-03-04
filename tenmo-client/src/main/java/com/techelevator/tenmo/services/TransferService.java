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
    private AuthenticatedUser currentUser;
    private Account currentAccount;
    private final ConsoleService consoleService = new ConsoleService();

    //Sophie
    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        //Need a List<Transfer> from API
        //Send accountId/ account_id
        //Will require checking accountTo and accountFrom
        //add JWD token
        //throw in transfer service class

        HttpEntity entity = getHeaders();
        ResponseEntity<List<Transfer>> transferResponse =
                restTemplate.exchange(API_BASE_URL + "transfer/history/" + currentAccount.getAccountId(),
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Transfer>>() {
                        });
        List<Transfer> transfers = transferResponse.getBody();

        if(transfers != null && !transfers.isEmpty()) {
            consoleService.printTransferHistory(transfers);
        }else {
            System.out.println("no transactions found");
        }
    }

    //Anne
    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        /*
         * Need List<Transfer> where transferStatusId = 1
         * Send accountId / account_id
         *
         * */

    }

    //Sophie
    private void sendBucks() {
        // TODO Auto-generated method stub
        /* reducing current account
            increasing receiving
            Need: Void
            Send Current accountId
                Receiving   accountId
                amount being sent
                build the Transfer obj - everything but ID
                Display -> List of users to choose from
                Have them select by UserID with Username (to grab that user, user there accountID)
                use the promptForInt method consoleService (add in the string of what you ask for to use this metod,
                it will return an int)
                With the accountID create account obj and call API to get users account
                After grabbing account from API, set 'accountTo' to the accountId pulled
                AccountFrom is already in method(currentUser Account)
                This is stored in currentAccount.getId

                Endpoint /account/GetUsernames
                foreach iterate through the list != currentUser.equals(currentUser.getUsername)
                send and get response - "How much do you want to send?"
                Don't forget - before ending method, reduce and increase both accounts.
         */

        HttpEntity entity = getHeaders();
        ResponseEntity<List<Transfer>> sendResponse = restTemplate.exchange(API_BASE_URL + "/account/getaccount/{userId}" +
                currentAccount.getAccountId(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<Transfer>>() {});


        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Who would you like to make a transfer to?");






    }

    //Anne
    private void requestBucks() {
        // TODO Auto-generate d method stub
        /*
        Need: Void
        Send Current accountId
                requested   accountId
                amount being being requested
         */

    }

    private HttpEntity<String> getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + currentUser.getToken());
        return new HttpEntity<String>(headers);
    }
}
