package bank_system.accounts;

import bank_system.MyDate;

/**
 * Produce an instance of Account's subclass if needed.
 */
public class AccountsFactory {

    /**
     * Produce account for users
     *
     * @param type type of accounts
     * @return An instance of account with given type.
     */
    public Account getAccount(String type, MyDate date, String currency) {
        if (type == null) {
            return null;
        } else if (type.equalsIgnoreCase("Chequing")) {
            return new ChequingAccount(date, currency);
        } else if (type.equalsIgnoreCase("Saving")) {
            return new SavingAccount(date, currency);
        } else if (type.equalsIgnoreCase("CreditCard")) {
            return new CreditCardAccount(date, currency);
        } else if (type.equalsIgnoreCase("LineOfCredit")) {
            return new LineOfCreditAccount(date, currency);
        } else if (type.equalsIgnoreCase("AdaptiveInterestSaving")) {
            return new AdaptiveInterestSavingAccount(date, currency);
        }
        return null;
    }
}
