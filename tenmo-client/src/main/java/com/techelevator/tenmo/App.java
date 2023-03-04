package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private Account currentAccount;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            try {
                HttpEntity<String> entity = getHeaders();
                currentAccount = restTemplate.exchange(API_BASE_URL + "account/getaccount/" +
                        currentUser.getUser().getId(), HttpMethod.GET, entity, Account.class).getBody();
            } catch (RestClientResponseException e) {
                BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
            } catch (ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }

        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    //Anne

	private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        // What we are pulling from the API and what we are providing
        // to get it
        //Need: Current Balance from the Account Table
        //Send accountId / account_id

//        Account currentBal = null;
//        try {
//            currentBal = restTemplate.getForObject(API_BASE_URL + "account/getaccount/" +
//            currentUser.getUser().getId(), Account.class);
//
//        } catch (RestClientResponseException e) {
//            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
//        } catch (ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        if (currentBal != null) {
//            System.out.println("Current Balance: " + currentBal.getBalance());
//        }
//        if (currentBal == null) {
//            System.out.println("Null");
//        }
        if (currentAccount.getBalance() != null) {
            System.out.println("Current Balance: " + currentAccount.getBalance());
        }
    }




}