package bank_system.accounts;

import bank_system.MyDate;

/**
 * Super class for assetAccounts.
 */
public abstract class AssetAccount extends Account {

    /**
     * Constructor for AssetAccount.
     */
    AssetAccount(MyDate date, String currency){
        super(date, currency);
    }

    /**
     * Decide whether the current person can access to the function of Transfer money out of account.
     */
    public boolean accessOut(){
        return true;
    }

    /**
     * Decide whether the current person can access to the function of Transfer money into account
     */
    public boolean accessIn(){
        return true;
    }

    /**
     * Decide whether the current person can access to the function of withdraw
     */
    public boolean accessWithdraw(){
        return true;
    }

    /**
     * Decide whether the current person can access to the function of deposit
     */
    public boolean accessDeposit(){
        return true;
    }

    /**
     * Send money into ATM.
     */
    public void transferIn(double amount) {
        balance += amount;
        System.out.println("Transaction Completed! New balance: " + balance);
    }

    public void transferOut(double amount) {
        if (balance - amount >= 0) {
            balance -= amount;
            System.out.println("Transaction Completed! New balance: " + balance);
        } else {
            System.out.println("Insufficient Fund!");
        }
    }
}
