package banking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Account {

    Random rand = new Random();

    String acc = "400000";
    String pinCode = "";
    long balance = 0;

    public void createNewAccount() {
        for (int i = 0; i < 9; i++) {
            acc += rand.nextInt(10);
            if (i < 4) {
                pinCode += rand.nextInt(10);
            }
        }
        acc += LuhnAlgorithm(acc);
    }

    public String getAccountID() {
        return acc;
    }

    public String getPinCode() {
        return pinCode;
    }

    public long getBalance() {
        return balance;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void addIncome(long amount) {
        this.balance = balance + amount;
        System.out.println("Income was added!");
    }

    public boolean checkTransfer(String accNum, Connect connect) {
        if (accNum.length() != 16) {
            System.out.println("Such a card does not exist.");
            return false;
        }
        String checkLuhn = accNum.substring(0, 15);
        int checked = LuhnAlgorithm(accNum);
        String completedString = checkLuhn + checked;
//            System.out.println("_________________________");
//            System.out.println(checkLuhn + " " + checked);
//            System.out.println(completedString);
//            System.out.println("_________________________");
        if (completedString.equals(accNum)) {
             System.out.println("Probably you made a mistake in the card number. Please try again!");
             return false;
        }
        if (connect.checkExistence(accNum)) {
            return true;
        } else {
//                System.out.println("Probably you made a mistake in the card number. Please try again!");
            System.out.println("Such a card does not exist.");
            return false;
        }
    }

    public void doTransfer(String accNum, long amount, Connect connect) {
        if(amount > balance) {
            System.out.println("Not enough money!");
        } else if (accNum.equals(this.getAccountID())) {
            System.out.println("You can't transfer money to the same account!");
        } else {
            this.balance -= amount;
            connect.transferBalance(accNum, amount);
        }
    }

    public int LuhnAlgorithm(String acc) {
        List<String> accString = new ArrayList<String>(Arrays.asList(acc.split("")));
        List<Integer> accInt = convertStringListToIntList(accString, Integer::parseInt);

        for (int i = 0; i < acc.length(); i++) {
            if (accInt.get(i) == 0) {

            } else if (i % 2 == 0) {
                int val = accInt.get(i) * 2;
                if (val > 9) {
                    accInt.set(i, val - 9);
                } else {
                    accInt.set(i, val);
                }
            }
        }
        int sum = 0;
        int returnValue = -1;
        for (int i = 0; i < acc.length(); i++) {
            sum += accInt.get(i);
        }
        for (int i = 0; i < 10; i++) {
            if (sum % 10 == 0) {
                returnValue = 0;
            } else if (sum % 10 == i) {
                returnValue = 10 - i;
            }
        }
        return returnValue;
    }

    public <T, U> List<U> convertStringListToIntList(List<T> listOfString, Function<T, U> function) {
        return listOfString.stream().map(function).collect(Collectors.toList());
    }
}