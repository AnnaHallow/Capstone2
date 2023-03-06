package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentAuthenticatedUser;
    private Account currentAccount;
    private TransferService transferService = new TransferService();
    private AccountServices accountServices = new AccountServices();
    private UserService userService = new UserService();

    public App() {
    }


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentAuthenticatedUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentAuthenticatedUser == null) {
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
        currentAuthenticatedUser = authenticationService.login(credentials);
        if (currentAuthenticatedUser == null) {
            consoleService.printErrorMessage();
        } else {
            try {
                HttpEntity<Object> entity = authenticationService.getHeaders(currentAuthenticatedUser);
                currentAccount = restTemplate.exchange(API_BASE_URL + "account/getaccount/" +
                        currentAuthenticatedUser.getUser().getId(), HttpMethod.GET, entity, Account.class).getBody();
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
                searchForTransfer();
            } else if (menuSelection == 5) {
                sendBucks();
            } else if (menuSelection == 6) {
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
        } else {
            System.err.println("Balance not found ");
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
        List<Transfer> transfers = transferService.viewTransferHistory(currentAuthenticatedUser, currentAccount);

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
        //Incoming Requests Cannot be approved by logged in user
        List<Transfer> incoming = transferService.getPendingIncoming(currentAuthenticatedUser, currentAccount);
        //Outgoing Requests to be approved by logged in user
        List<Transfer> outgoing = transferService.getPendingOutgoing(currentAuthenticatedUser, currentAccount);

        List<Transfer> updatedOutgoing = consoleService.handlePendingOutgoing(outgoing, currentAuthenticatedUser,
                accountServices, transferService);

        consoleService.printIncomingPending(incoming);
        consoleService.printOutgoingPending(outgoing);
    }


    //Sophie
    public void sendBucks() {
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
        int receivingUserId = 0;
        List<User> users = userService.listOfUsers(currentAuthenticatedUser);
        while(receivingUserId == 0){
            receivingUserId = consoleService.selectUser(users, currentAuthenticatedUser.getUser());
        }
        double amountToSend = consoleService.amountToSend(receivingUserId, currentAccount);
        if (amountToSend > 0) {
            Account receivingAccount = accountServices.getAccount(currentAuthenticatedUser, receivingUserId);

            Account updatedCurrentAccount = accountServices.processTransactionsSent(currentAccount, amountToSend);
            Account updatedReceivingAccount = accountServices.processTransactionReceived(receivingAccount,amountToSend);

            accountServices.updateAccount(updatedCurrentAccount, currentAuthenticatedUser);
            accountServices.updateAccount(updatedReceivingAccount, currentAuthenticatedUser);

            Transfer transfer = new Transfer(2, 2, currentAccount.getAccountId(),
                    receivingAccount.getAccountId(), BigDecimal.valueOf(amountToSend));

            transferService.saveTransfer(transfer, currentAuthenticatedUser);

            accountServices.transactionComplete(currentAccount);
        } else {
            System.out.println("Unable to complete transaction. Please try again later.");
        }
    }

    //Anne
    private void requestBucks() {

        // pull current account
        int requestingUserId = 0;
        List<User> users = userService.listOfUsers(currentAuthenticatedUser);

        //pull account being sent to
        while(requestingUserId == 0){
            requestingUserId = consoleService.selectUser(users, currentAuthenticatedUser.getUser());
        }


        double amountToRequest = consoleService.amountToRequest(requestingUserId);
        Account requestingAccount = accountServices.getAccount(currentAuthenticatedUser, requestingUserId);

        BigDecimal currentUserBalance = currentAccount.getBalance();
        //do we need to check the requesting account? Could possibly auto decline for insufficient funds?
        BigDecimal requestingUserBalance = requestingAccount.getBalance();

        //set status to pending (=1)  What is the transfer type?
        //**Transfer Typre Request is 1 also
        //** Your accounts are backward here the first account should be who the money is coming from
        // and the second account should be who the money is going to
        //** you could wrap this is an if statement...if requesting amount is > 0
        // AND requestingAmount is lessthan requestingAccount.getBalance
        //and throw an else saying the target users account balance dows not support this transaction or something to that effect.

        Transfer transfer = new Transfer(1, 1, requestingAccount.getAccountId(),
                currentAccount.getAccountId(), BigDecimal.valueOf(amountToRequest));

        transferService.saveTransfer(transfer, currentAuthenticatedUser);

        accountServices.transactionComplete(currentAccount);

    }

    public void searchForTransfer(){
        int transferID = consoleService.promptForInt("Please enter the transfer ID: ");
        Transfer transfer = transferService.getTransfer(transferID, currentAuthenticatedUser);
        consoleService.printTransfer(transfer);
    }





}