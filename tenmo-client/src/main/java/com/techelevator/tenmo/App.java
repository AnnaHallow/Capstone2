package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private Account currentAccount;
    private TransferService transferService = new TransferService();
    private AccountServices accountServices = new AccountServices();
    private UserService userService = new UserService();


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
                HttpEntity<Object> entity = authenticationService.getHeaders(currentUser);
                currentAccount = restTemplate.exchange(API_BASE_URL + "account/getaccount/" +
                        currentUser.getUser().getId(), HttpMethod.GET, entity, Account.class).getBody();
            } catch (RestClientResponseException e) {
                BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
            } catch (ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }


//           Account currentAccount = accountServices.getAccount(currentUser);

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
    //**this needs an else statement
	private void viewCurrentBalance() {

        if (currentAccount.getBalance() != null) {
            System.out.println("Current Balance: " + currentAccount.getBalance());
        }
    }

    //Sophie
    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        //Need a List<Transfer> from API
        //Send accountId/ account_id
        //Will require checking accountTo and accountFrom
        //add JWD token
        //throw in transfer service class
        List<Transfer> transfers = transferService.viewTransferHistory(currentUser, currentAccount);

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
            Send Current accountId
                Receiving   accountId
                amount being sent
                build the Transfer obj - everything but ID
                Display -> List of users to choose from
                Have them select by UserID with Username (to grab that user, user there accountID)
                use the promptForInt method consoleService (add in the string of what you ask for to use this method,
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

        List<Account> accounts = accountServices.listOfAccounts(currentUser);
        List<User> users = userService.listOfUsers(currentUser);
        int receivingUserId = consoleService.selectUser(users);
        int amountToSend = consoleService.amountToSend(receivingUserId);
        Account receivingAccount = accountServices.getAccount(currentUser, receivingUserId);

        BigDecimal currentUserAccount = currentAccount.getBalance();
        BigDecimal receivingUserAccount = receivingAccount.getBalance();

        currentAccount.setBalance(currentUserAccount.subtract(BigDecimal.valueOf(amountToSend)));
        receivingAccount.setBalance(receivingUserAccount.add(BigDecimal.valueOf(amountToSend)));

        accountServices.updateAccount(currentAccount, currentUser);
        accountServices.updateAccount(receivingAccount, currentUser);

        Transfer transfer = new Transfer(2, 2, currentAccount.getAccountId(),
                receivingAccount.getAccountId(), BigDecimal.valueOf(amountToSend));

      transferService.saveTransfer(transfer, currentUser);

      accountServices.transactionComplete(currentAccount);

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





}