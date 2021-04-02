package bank_system.users;

import bank_system.ATM;
import bank_system.Bank;
import bank_system.accounts.Account;
import bank_system.accounts.AccountsFactory;

/**
Class users.Worker for bank works, extend from Person
 */
public abstract class Worker extends Person {
    Worker(String username, String password){
        super(username, password);
    }

    /**
     * Add user to the bank_system.ATM
     *
     * @param username username of the new user
     * @param password password of the new user
     */
    public void createUser(String username, String password) {
        AccountsFactory af = new AccountsFactory();
        User newUser = new User(username, password);

        Bank.getPersonList().add(newUser);
        newUser.addAccount(af.getAccount("Chequing", ATM.getTime(), "CAD"));
    }

    /**
     * Create account for user as their needs.
     */
    public void createAccount(User user, String Acc, String moneyType){
        AccountsFactory af = new AccountsFactory(); //create an instance of account factory to generate new account.
        user.addAccount(af.getAccount(Acc, ATM.getTime(), moneyType));
    }

    /**
     * Create joint accounts for two users one account as their needs.
     */
    public void createAccount(User user1, User user2, String Acc, String moneyType){
        AccountsFactory af = new AccountsFactory(); //create an instance of account factory to generate new account.
        Account account = af.getAccount(Acc, ATM.getTime(), moneyType);
        user1.addAccount(account);
        user2.addAccount(account); //joint accounts.
    }

}