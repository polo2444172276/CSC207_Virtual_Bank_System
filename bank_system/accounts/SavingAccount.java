package bank_system.accounts;

import bank_system.InterestStrategy.StrategyCalculator;
import bank_system.MyDate;

public class SavingAccount extends AssetAccount{

    /**
     * Monthly interest rate for this account, default set as 0.1%
     */
    private double interest = 0.005;

    /**
     * Constructor for class SavingAccount.
     */
    public SavingAccount(MyDate date, String currency) {
        super(date, currency);
    }

    /**
     * Setter for interest.
     */
    public void setInterest(double rate){
        this.interest = rate;
    }

    /**
     * Display current interest rate.
     * @return double interest rate.
     */
    public double getInterest(){
        return this.interest;
    }

    /**
     * Update balance periodically.
     */
    public void update(StrategyCalculator s) {
        balance = s.updateValue(balance, 0.01);
    }

    public String getType() {
        return "Saving";
    }
}
