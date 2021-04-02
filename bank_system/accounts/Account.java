package bank_system.accounts;

import bank_system.MyDate;
import bank_system.Transaction;

import java.util.Stack;

/**
 * Abstract class Account for bank accounts.
 */
public abstract class Account{

    /**
     * Number nth account created.
     */
    private static int numAccounts = 0;

    /**
     * Unique 8 digits id of current account.
     */
    private String id;

    /**
     * Balance of current account.
     */
    double balance;

    /**
     * A stack to store the transaction histories, the top element of this stack is the most recent transaction
     * of this account.
     */
    private Stack<Transaction> transactionStack;

    /**
     * Type of currency that this account stored.
     */
    private String moneyType;
    /**
     * The time when this account is created
     */
    private MyDate creationTime;

    public static int getNumAccounts() {
        return numAccounts;
    }

    /**
     * Return type of currency this account stores
     * @return type of currency as String.
     */
    public String getMoneyType(){
        return moneyType;
    }
    /**
     * Getter method for transactionStack
     * @return transactionStack
     */
    public Stack<Transaction> getTransactionStack() {
        return transactionStack;
    }

    /**
     * Get the most recent transaction.
     */
    public Transaction getMostRecentTransaction(){
        if(transactionStack.isEmpty()){
            return null;
        }
        return transactionStack.peek();
    }

    /**
     * Add a new transaction to the transaction stack.
     * @param t new transaction to be added into the stack
     */
    public void addToTransactionStack(Transaction t){
            transactionStack.push(t);
            t.printInfo();
    }

    /**
     * Display the current balance.
     *
     * @return current balance of this account.
     */
    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    /**
     * Generate the id of current account.
     *
     * @return int id.
     */
    private static String setId() {
        int length = Integer.toString(numAccounts).length();
        return "00000000".substring(0, 8 - length) + numAccounts;
    }

    /**
     * Return id of current bank account.
     * @return id in String.
     */
    public String getId(){
        return this.id;
    }

    /**
     * Abstract constructor of Accounts.
     *
     * @throws StringIndexOutOfBoundsException when maximum account limit exceeded.
     */
    Account(MyDate date, String currency) throws StringIndexOutOfBoundsException {
        try {
            numAccounts++;
            this.id = setId();
            this.transactionStack = new Stack<>();
            creationTime = date;
            moneyType = currency;
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Maximum number of accounts reached!");
        }
    }

    /**
     * Getter method for creationTime
     * @return time when this account is created
     */
    public MyDate getCreationTime() {
        return creationTime;
    }

    /**
     * String representation of account.
     *
     * @return string of account balance and id.
     */
    public String toString() {
        return String.format("Account ID: %s. Account type: %s. Current Balance: %.2f%s.",
                this.getId(), this.getType(), this.getBalance(), this.moneyType);
    }


    /**
     * Take cash from ATM.
     */
    public abstract void transferOut(double amount);

    /**
     * Send money into ATM.
     */
    public abstract void transferIn(double amount);

    /**
     * Decide whether the current bank_system.users.User can access to the function of Transfer money out of account.
     * If can do this, return true. Otherwise, return false.
     */
    public abstract boolean accessOut();

    /**
     * Decide whether the current bank_system.users.User can access to the function of Transfer money into account
     * If can do this, return true. Otherwise, return false.
     */
    public abstract boolean accessIn();

    /**
     * Decide whether the current bank_system.users.User can access to the function of withdraw.
     * If can do this, return true. Otherwise, return false.
     */
    public abstract boolean accessWithdraw();

    /**
     * Decide whether the current bank_system.users.User can access to the function of deposit.
     * If can do this, return true. Otherwise, return false.
     */
    public abstract boolean accessDeposit();

    /**
     * Returns the type of the account
     * @return Type of the account in String
     */
    public abstract String getType();
}
