package bank_system.users;


import bank_system.atm_interface.WorkerInterface;

import java.io.BufferedReader;
import java.io.IOException;


public class BankStaff extends Worker {
    /*
    This BankStaff class has the following properties:
    - staff has accounts, like a user
    - staff can create user
    - staff can create account for a user
    - staff can change their own passwords
     */
    private final User user;

    public BankStaff(String username, String password) {
        super(username, password);
        user = new User(username,password);
    }

    public User getUser() {
        return user;
    }

    @Override
    public void userInterface(BufferedReader br) throws IOException {
        WorkerInterface UI = new WorkerInterface(this);
        String inputLine = null;
        do {
            if (inputLine != null) {
                switch (inputLine) {
                    case "1":
                        user.userInterface(br);
                        break;
                    case "2":
                        UI.createNewUserUI();
                        break;
                    case "3":
                        UI.createNewAccountUI();
                        break;
                    case "4":
                        UI.createJointAccountUI();
                        break;
                    default:
                        System.out.println("INVALID INPUT.");
                        break;
                }
            }
            System.out.println("\nMain menu:");
            System.out.println("Type 1 to check your own accounts.");
            System.out.println("Type 2 to create a new user.");
            System.out.println("Type 3 to create a new account.");
            System.out.println("Type 4 to create a joint account for two users.");
            System.out.println("Type LOGOUT to exit.");
        } while (!(inputLine = br.readLine().toLowerCase()).equals("logout"));
        System.out.println("Thank you! See you next time!");
    }

    @Override
    public String fileOutputFormat(){
        return String.format("staff,%s,%s", getUsername(), getPassword());
    }
}
