package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printTransferHistory(List<Transfer> transfers){
        for (Transfer x : transfers) {
            if(x.getTransferStatusId() != 1) {
                System.out.println(
                        "\nTransfer " + x.getTransferId() +
                                ": ");

                int type = x.getTransferTypeId();
                String transferType;
                switch (type) {
                    case 1:
                        transferType = "Sent";
                        break;
                    case 2:
                        transferType = "Received";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + type);
                }

                System.out.print(transferType + " ");

                int status = x.getTransferStatusId();
                String transferStatus;
                switch (status) {
                    case 1:
                        transferStatus = "Pending";
                        break;
                    case 2:
                        transferStatus = "Approved";
                        break;
                    case 3:
                        transferStatus = "Rejected";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + status);
                }

                System.out.println(": " + transferStatus);

                System.out.println("Account From: " + x.getAccountFrom() +
                        "  Account To: " + x.getAccountTo());

                System.out.println("Amount: " + x.getAmount());
            }
        }
    }

    public int selectUser(List<User> userList){
        for (User user : userList) {

            System.out.println("\n" + user.getId() + ": " + user.getUsername());
        }
        return promptForInt("Please select the number of the User you would like to transfer to.");
    }
    public int amountToSend(int receivingUserId){
        return promptForInt("Specify an amount to send to User " + receivingUserId + ": ");
    }
}
