package bank_system;

import bank_system.accounts.Account;

/**
 represents a transaction. This class is created in case we want to record
 the transaction history for every user in the future. The transaction is not executed
 upon construction of this object, but when execute() is called.
 */
public class Transaction {

    /**
     * Place where money transfers out.
     */
    private final Object moneyOut;

    /**
     * Place where money transfers in.
     */
    private final Object moneyIn;

    /**
     * Stores balance for money out account before transaction processed.
     */
    private double originalOutBalance;


    /**
     * Amount of money involved in this transaction.
     */
    private final double amount;

    /**
     * When does the transaction take place.
     */
    private MyDate date;

    /**
     * Type of the transaction, e.g. deposit, withdraw, EMT, pay-bill...
     */
    private final String type;

    /**
     * Default money type.
     */
    private String outMoneyType = "CAD";
    private String inMoneyType = "CAD";

    /**
     * Can this transaction be undone by the manager.
     */
    private boolean undoable = true;


    /**
     * Constructor for Transaction class:
     * Account/ATM/Bill issuer to Account/ATM/Bill issuer.
     *
     * Call setToCantUndo if the transaction is not undoable.
     *
     * @param type String type of this transaction
     * @param out place where money will transfer out
     * @param in place where money will transfer in
     * @param amount amount of money involved in this transaction
     */
    public Transaction(String type, Object out,
                       Object in, double amount) {
        this.moneyOut = out;
        this.moneyIn = in;
        this.amount = amount;
        this.type = type;
        this.date = ATM.getTime();

        if (out instanceof Account){
            this.outMoneyType =((Account) out).getMoneyType();
            originalOutBalance = ((Account) out).getBalance();}

        if (in instanceof Account) {
            this.inMoneyType =((Account) in).getMoneyType();
        }
    }

    public Transaction(String type, Object out, Object in, double amount, MyDate date, boolean undoable){
        this(type, out, in, amount);
        this.date = date;
        this.undoable = undoable;
    }

    /**
     * Set a transaction to can't undo.
     */
    public void setToCantUndo() {
        this.undoable = false;
    }

    public double getAmount() {
        return amount;
    }

    public MyDate getDate() {
        return date;
    }

    /**
     * Check if a transaction is undoable
     * @return true if it is undoable
     */
    public boolean isUndoable() {
        return undoable;
    }

    /**
     * Name of this transaction
     * @return String name
     */
    public String getType() {
        return type;
    }

    /**
     * Get the account/String who receives money
     * @return receiver
     */
    public Object getMoneyIn(){return moneyIn;}

    /**
     * Get account/String who made this transaction
     * @return transferee.
     */
    public Object getMoneyOut() {
        return moneyOut;
    }

    public String getMoneyType() { return outMoneyType; }

    /**
     returns null if transaction is irreversible
     otherwise returns the reverse of current transaction object
     */
    public Transaction Reverse() {
        Transaction t;
        if (undoable){
            t = new Transaction("UNDO Previous Transaction", moneyIn, moneyOut, this.amount);
        } else {
            t = null;
        } return t;
    }



    /**
     executes the transaction and records it
     */
    public void execute() {
        //account to account money transfer
        if (this.moneyOut instanceof Account && this.moneyIn instanceof Account){
            if (inMoneyType.equals(outMoneyType)){ //check currency type
                ((Account) moneyOut).transferOut(amount);
                ((Account) moneyIn).transferIn(amount);
            } else {
                System.out.println(String.format("Account#%s and account#%s are in different currency. Cannot transfer between!\n",
                        ((Account) moneyOut).getId(), ((Account) moneyIn).getId()));
            }
        }
        //account to non-account money transfer
         else if (this.moneyOut instanceof Account){
             ((Account) moneyOut).transferOut(amount);
        }
        //non-account to account money transfer
         else if (this.moneyIn instanceof Account){
             ((Account) moneyIn).transferIn(amount);

        } checkIsComplete();
    }

    /**
     * Helper function to check whether the transaction is rejected by any reason.
     */
    private void checkIsComplete(){
        if (this.moneyOut instanceof Account &&
                ((Account) this.moneyOut).getBalance() != originalOutBalance &&
                !(this.type.equals("UNDO Previous Transaction"))){
            //undo transactions don't need to be added to transaction stack
            ((Account) moneyOut).addToTransactionStack(this);
        }
        if (this.type.equals("Deposit") &&
                ((Account) this.moneyIn).getBalance() != originalOutBalance
               ){
            //undo transactions don't need to be added to transaction stack
            ((Account) moneyIn).addToTransactionStack(this);
        }

    }

    /**
     * Print the information of this transaction in one line
     */
    public String printOneLine(){
        String out = "";
        String in = "";
        if (moneyOut instanceof Account) {out = ((Account) moneyOut).getId();}
        if (moneyIn instanceof Account) {in = ((Account) moneyIn).getId();}
        //if (moneyOut instanceof String){ out = (String) moneyOut;}
        //if (moneyIn instanceof String){ out = (String) moneyIn;}
        return String.format("Type: %s Date: %s From: %s To: %s Amount: %s %s", type, date, out,
                in, amount, outMoneyType);}


    public void printInfo() { // should be implemented after calling execute()
        System.out.println(String.format("Transaction info: \nType: %s\nDate: %s\nFrom: %s\nTo: %s\nAmount: %s %s", type, date, moneyOut,
                moneyIn, amount, outMoneyType));
    }
}
