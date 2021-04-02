package bank_system.accounts;

import bank_system.MyDate;

/**
 * One type of bank account that extends Account.
 */
public class ChequingAccount extends AssetAccount {

    /**
     * Constructor for class ChequingAccount.
     */
    public ChequingAccount(MyDate date, String currency) {
        super(date, currency);
    }

    /**
     * Take cash from ATM.
     */
    @Override
    public void transferOut(double amount) {
        if (balance - amount >= -100 & balance > 0) {
            balance -= amount;
            System.out.println("Transaction Completed! New balance: " + balance);
            if (balance < 0) {
                System.out.println("WARNING! You now have a negative balance!");
            }
        } else {
            System.out.println("Negative Balance Limit Exceeded");
        }
    }

    public String getType() {
        return "Chequing";
    }

}
