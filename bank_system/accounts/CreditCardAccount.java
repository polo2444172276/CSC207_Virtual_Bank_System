package bank_system.accounts;

import bank_system.MyDate;

class CreditCardAccount extends DebtAccount {
    public CreditCardAccount(MyDate date, String currency){super(date, currency);}

    /**
     * User can transfer money in to Credit account.
     * @return true
     */
    @Override
    public boolean accessIn() {
        return true;
    }

    /**
     * User cannot cannot transfer money out from Credit account.
     * @return false
     */
    @Override
    public boolean accessOut() {
        return false;
    }

    public String getType() {
        return "CreditCard";
    }
}
