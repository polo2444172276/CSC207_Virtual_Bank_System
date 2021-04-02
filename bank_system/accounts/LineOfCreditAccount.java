package bank_system.accounts;

import bank_system.MyDate;

class LineOfCreditAccount extends DebtAccount {
    public LineOfCreditAccount(MyDate date, String currency){super(date, currency);}

    /**
     * User can transfer money in to Credit account.
     * @return true
     */
    @Override
    public boolean accessIn() {
        return true;
    }

    /**
     * User can transfer money in to Credit account.
     * @return true
     */
    @Override
    public boolean accessOut() {
        return true;
    }

    public String getType() {
        return "LineOfCredit";
    }
}
