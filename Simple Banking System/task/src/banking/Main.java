package banking;

import java.util.*;


public class Main {
    public static void main(String[] args) {

        Connect connect = new Connect();
        connect.createDB();
        connect.createTable();
        Account acc = new Account();
        analyzeInput(bankLog(), acc, connect);
//        connect.clearDB();
    }

    public static int bankLog() {
//        Connect connect = new Connect();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
//        connect.selectAll();
        return scanner.nextInt();
    }

    public static void analyzeInput(int usersInput, Account acc, Connect connect) {
        switch (usersInput) {
            case 0:
                System.out.println("Bye!");
                break;
            case 1:
                acc = createAnAccount(connect);
                break;
            case 2:
                logIntoAccount(acc, connect);
                break;
        }
    }

    public static void logIntoAccount(Account acc, Connect connect) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String givenAccountID = scanner.next();
        System.out.println("Enter your PIN:");
        String givenPIN = scanner.next();

        if(!connect.checkExistence(givenAccountID)) {
            System.out.println("Wrong Account ID!");

            analyzeInput(bankLog(), acc, connect);
        } else if (connect.checkData(givenAccountID, Integer.parseInt(givenPIN))) {
            loggedIn(connect.connectToAcc(givenAccountID, acc), connect, false);
        } else {
            System.out.println("Wrong PIN!");

            analyzeInput(bankLog(), acc, connect);
        }
    }

    public static void loggedIn(Account acc, Connect connect, boolean loggedInBefore) {
        Scanner scanner = new Scanner(System.in);
        if (!loggedInBefore) {
            System.out.println("You have successfully logged in!");
        }

        System.out.println("");
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do Transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        int askedNumber = scanner.nextInt();
        switch (askedNumber) {
            case 1:

                System.out.println(acc.getBalance());
                loggedIn(acc, connect, true);
                break;
            case 2:
                System.out.println("Enter income:");
                long amount = scanner.nextLong();
                acc.addIncome(amount);
                loggedIn(acc, connect, true);
                break;
            case 3:
                System.out.println("Transfer");
                System.out.println("Enter card Number");
                String accNum = scanner.next();

                if (acc.checkTransfer(accNum, connect)) {
                    System.out.println("Enter how much money you want to transfer");
                    long transferAmount = scanner.nextLong();
                    acc.doTransfer(accNum, transferAmount, connect);
                }
                loggedIn(acc, connect, true);
                break;
            case 4:
                System.out.println("This account has been closed");
                connect.deleteAcc(acc.getAccountID());
                analyzeInput(bankLog(), new Account(), connect);
                break;
            case 5:
                System.out.println("You have successfully logged out!");
                connect.updateBalance(acc);
                analyzeInput(bankLog(), acc, connect);
                break;
            case 0:
                System.out.println("Bye!");
                connect.updateBalance(acc);
                break;
        }
    }

    public static Account createAnAccount(Connect connect) {
        Account acc = new Account();
        acc.createNewAccount();
        System.out.println("Your card has been created");
        System.out.println("Your code number:");
        System.out.println(acc.getAccountID());
        System.out.println("Your card PIN:");
        System.out.println(acc.getPinCode());
        System.out.println("");
        connect.addInfoDB(acc);
        analyzeInput(bankLog(), acc, connect);
        return acc;
    }
}

