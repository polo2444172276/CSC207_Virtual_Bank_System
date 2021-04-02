package bank_system.accounts;

import bank_system.MyDate;
import bank_system.accounts.Account;

/**
 * One type of bank account that extends Account
 */
public abstract class DebtAccount extends Account {

    /**
     * This is the maximum amount of debt user can incur.
     */
    private double maxDebt = 1000;

    /**
     * Points gained from using credit cards.
     */
    private double cashBackPoint = 0;

    /**
     * Constructor for abstract class Debt.
     */
    DebtAccount(MyDate date, String currency) {super(date, currency);}

    /**
     * Set the maximum amount of debt user can incur Permanently.
     * @param amount double amount of money.
     */
    public void setMaxDebt(double amount){
        this.maxDebt = amount;
    }

    /**
     * View current points of certain debt account.
     * @return double amount of points.
     */
    public double getCashBackPoint(){
        return this.cashBackPoint;
    }

    /**
     * Use those points.
     * @param amount amount of points wish to use.
     */
    public void usePoints(double amount){
        this.cashBackPoint-= amount;
    }

    /**
     * Receive money from other accounts
     * @param amount The amount of money
     */
    public void transferIn(double amount){
        balance -= amount;
    }

    /**
     * Send money to other accounts
     * @param bill The amount of money
     */
    public void transferOut(double bill){
        //To check whether maximum debt amount is exceeded or not.
        if (balance + bill <= maxDebt){
            balance += bill;
            cashBackPoint+= bill; //Adding cash back points.
        }
        else{
            System.out.println("Insufficient Funds!");
        }
    }

    /**
     * Decide whether the current person can access to the function of withdraw
     */
    public boolean accessWithdraw(){
        return false;
    }

    /**
     * Decide whether the current person can access to the function of deposit
     */
    public boolean accessDeposit(){
        return false;
    }
}