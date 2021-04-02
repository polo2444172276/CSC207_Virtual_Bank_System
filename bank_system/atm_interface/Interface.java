package bank_system.atm_interface;

import bank_system.*;
import bank_system.users.*;
import bank_system.accounts.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

/**
 * The collection for all user interface.
 */
class Interface {

    protected static final int requiredLength = 8;

    /**
     * Check if input contains only number
     * @param line input
     * @return true if line contains only number
     */
    boolean isNumber(String line){
        return line.matches("[0-9]+");
    }

    /**
     * Check if input contains only number
     * @param line input
     * @return true if line contains only number
     */
    boolean isDouble(String line){
        if (line.matches("([-]?[0-9]*)\\.([0-9]*)")){
            return true;
        }
        else {
            return line.matches("([-]?[0-9]*)");
        }
    }

    /**
     * Check if input contains only desired date format
     * @param line input
     * @return true if line contains only date
     */
    boolean isDate(String line){
        return line.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
    }

    /**
     * Check if string is alphanumeric
     * @return true if the contains numbers and letters
     */
    boolean isAlphanumeric(String s){
        return s.matches("[a-zA-Z0-9]+");
    }

    /**
     * User interface for user to change his/her password
     * @param self the user
     * @throws IOException console input
     */
    void changePasswordUI(Person self) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        System.out.println("Please type your new password(numbers only)");
        System.out.println("Type CANCEL to cancel:");
        while(!isNumber(inputLine = br.readLine())
             || inputLine.trim().length() != requiredLength) {
            if(inputLine.toLowerCase().equals("cancel")){
                System.out.println("Cancelled password change!");
                return;
            }
            System.out.printf("Please re-enter your new password(must be %d-digit number)", requiredLength);
            System.out.println();
            System.out.println("Type CANCEL to cancel:");
        }
        String newPassword = inputLine;

        System.out.println("Please type your new password again to confirm");
        inputLine = br.readLine();
        if(!inputLine.equals(newPassword)) {
            System.out.println("Two password doesn't match. Please try again.");
        }
        else{
            self.setPassword(newPassword);
            System.out.println("Done!");
        }
    }

    Account findAllAccount(String accountID){
        for(Person person : Bank.getPersonList()){
            if(person instanceof BankStaff){
                person = ((BankStaff) person).getUser();
            }
            if(person instanceof User){
                for(Account account : ((User) person).getAccountList()){
                    if(account.getId().equals(accountID)){
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find a User by its username
     * @param username username in String
     * @return null if this username of User doesn't exist
     */
    User findUser(String username) {
        for(Person person : Bank.getPersonList()) {
            if(person.getUsername().equals(username)) {
                if(person instanceof User) {
                    return (User) person;
                }
                else if(person instanceof BankStaff) {
                    return ((BankStaff) person).getUser();
                }
            }
        }
        return null;
    }

    /**
     * Helper function to print all transactions on one account.
     * @param account The account to check.
     */
    String PrintTransHelper(Account account) {
        Stack<Transaction> s = account.getTransactionStack();
        StringBuilder r = new StringBuilder();
        int depth = 0;
        Stack<Transaction> temp = new Stack<>();
        while(!s.isEmpty()){
            r.append(depth).append(": ").append(s.peek().printOneLine()).append("\n");
            temp.push(s.pop());
            depth += 1;
        }
        while (!temp.isEmpty()) {s.push(temp.pop());}
        return r.toString();
    }
}
