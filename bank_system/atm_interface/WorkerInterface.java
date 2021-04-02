package bank_system.atm_interface;

import bank_system.Bank;
import bank_system.users.Person;
import bank_system.users.User;
import bank_system.users.Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WorkerInterface extends Interface {
    private final Worker self;
    public WorkerInterface(Worker worker) {this.self = worker;}


    /**
     * Interface for staff to create a new user account.
     * @throws IOException Console input
     */
    public void createNewUserUI() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username;
        String password;

        System.out.println("Please enter your username. Type CANCEL to cancel creation: ");
        while(!(username = br.readLine()).matches("[0-9a-z]+") || usernameExist(username)) {
            if(!username.matches("[0-9a-z]+")){
                System.out.println("Username can only contain lower case letters and numbers.");
            }
            else{
                System.out.println("Username already has been taken.");
            }
            System.out.println("Please re-enter your username. Type CANCEL to cancel creation: ");
        }
        if(username.toLowerCase().equals("cancel")) {
            System.out.println("Creation canceled!");
            return;
        }

        System.out.println("Please enter your password. Type CANCEL to cancel creation: ");
        while(!isNumber(password = br.readLine().trim()) ||
                password.length()!= requiredLength) {
            if(password.toLowerCase().equals("cancel")){
                System.out.println("Canceled!");
                return;
            }
            System.out.printf("Password can only be %d-digit numbers.\n", requiredLength);
            System.out.println("Please re-enter your password. Type CANCEL to cancel creation: ");
        }

        self.createUser(username, password);

        System.out.println("Done!");
    }


    /**
     * Interface for worker to create a new account for a user
     * @throws IOException Console input
     */
    public void createNewAccountUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username;
        System.out.println("Please enter the username of the user. Type CANCEL to cancel creation: ");
        while(!isAlphanumeric(username = br.readLine()) || findUser(username) == null) {
            if (username.toLowerCase().equals("cancel")){
                break;
            }
            if(!isAlphanumeric(username)){
                System.out.println("Username can only contain alphanumeric.");
            }
            else{
                System.out.println("Username doesn't exists.");
            }
            System.out.println("Please re-enter your username: ");
        }
        if(username.toLowerCase().equals("cancel")) {
            System.out.println("Creation canceled!");
            return;
        }

        String accountType;

        System.out.println("Here's the type of accounts:");
        System.out.println("Chequing: Chequing account");
        System.out.println("Saving: Saving account");
        System.out.println("CreditCard: Credit card account");
        System.out.println("LineOfCredit: Line of Credit account");
        System.out.println("Please enter the type of account that you wish to create:");

        accountType = br.readLine();
        while(!accountType.equals("Chequing") &&
                !accountType.equals("Saving") &&
                !accountType.equals("CreditCard") &&
                !accountType.equals("LineOfCredit")) {
            System.out.println("Please enter the correct type!");
            accountType=br.readLine();
        }

        String moneyType;
        System.out.println("Please enter the type of currency of this account.");
        moneyType = br.readLine().toUpperCase();

        User target = findUser(username);

        self.createAccount(target, accountType, moneyType);

        System.out.println("DONE!");

    }


    /**
     * Interface for worker to create a new account for a user
     * @throws IOException Console input
     */
    public void createJointAccountUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String user1;
        String user2;
        System.out.println("Please enter the username of the first user. Type CANCEL to cancel creation: ");
        while(!isAlphanumeric(user1 = br.readLine()) || findUser(user1) == null) {
            if (user1.toLowerCase().equals("cancel")){
                break;
            }
            if(!isAlphanumeric(user1)){
                System.out.println("Username can only contain alphanumeric.");
            }
            else{
                System.out.println("Username doesn't exists.");
            }
            System.out.println("Please re-enter your username: ");
        }
        if(user1.toLowerCase().equals("cancel")) {
            System.out.println("Creation canceled!");
            return;
        }
        System.out.println("Please enter the username of the second user. Type CANCEL to cancel creation: ");
        while(!isAlphanumeric(user2 = br.readLine()) || findUser(user2) == null || user2.equals(user1)) {
            if (user2.toLowerCase().equals("cancel")){
                break;
            }
            if(!isAlphanumeric(user2)){
                System.out.println("Username can only contain alphanumeric.");
            }
            if (user2.equals(user1)){
                System.out.println("Joint account can only be owned by two different users.");
            }
            else{
                System.out.println("Username doesn't exists.");
            }
            System.out.println("Please re-enter your username: ");
        }
        if(user2.toLowerCase().equals("cancel")) {
            System.out.println("Creation canceled!");
            return;
        }

        String accountType;

        System.out.println("Here's the type of accounts:");
        System.out.println("Chequing: Chequing account");
        System.out.println("Saving: Saving account");
        System.out.println("CreditCard: Credit card account");
        System.out.println("LineOfCredit: Line of Credit account");
        System.out.println("Please enter the type of account that you wish to create:");

        accountType = br.readLine();
        while(!accountType.equals("Chequing") &&
                !accountType.equals("Saving") &&
                !accountType.equals("CreditCard") &&
                !accountType.equals("LineOfCredit")) {
            System.out.println("Please enter the correct type!");
            accountType=br.readLine();
        }

        String moneyType;
        System.out.println("Please enter the type of currency of this account.");
        moneyType = br.readLine().toUpperCase();

        User u1 = findUser(user1);
        User u2 = findUser(user2);
        self.createAccount(u1,u2,accountType,moneyType);

        System.out.println("DONE!");
    }


    /**
     * Helper method for create new user. Check if a username already exist
     * @param username username in String
     * @return true if the username is already existed
     */
    private boolean usernameExist(String username){
        for(Person p : Bank.getPersonList()){
            if(username.equals(p.getUsername())){
                return true;
            }
        }
        return false;
    }
}
