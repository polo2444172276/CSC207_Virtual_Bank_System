package bank_system.users;

import bank_system.FinancialProduct.Funding;
import bank_system.*;
import bank_system.accounts.Account;
import bank_system.accounts.AssetAccount;
import bank_system.accounts.ChequingAccount;
import bank_system.accounts.DebtAccount;
import bank_system.atm_interface.UserInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
Class user for bank clients, extend from Person
 */
public class User extends Person implements Observer {
    /** bank_system.users.User class should be able to do these four things:
     -see a summary of all of their account balances -> viewBalanceInterface()
     -the most recent transaction on any account -> mostRecentTransaction
     -the date of creation of one of their accounts -> BankManager.
     -their net total -> getTotalBalance()
     */
    private final ArrayList<Account> accountList = new ArrayList<>();

    /**
     * The primary account for any deposit and default transferIn.
     */
    private final ArrayList<Account> primaryAccount;
    /**
     * The list of stock the user have.
     */
    private final ArrayList<Object[]> stockArrayList = new ArrayList<>();

    /**
     * The list of funding the user joints.
     */
    private final ArrayList<Funding> fundingArrayList = new ArrayList<>();

    /**
     * Constructor for User
     */
    public User(String username, String password) {
        super(username, password);
        primaryAccount = new ArrayList<>();
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    /**
     * @return The list of stocks the user bought.
     */
    public ArrayList<Object[]> getStockArrayList(){
        return stockArrayList;
    }

    /**
     * Add a new funding to the list of funding that user joints.
     * @param f new funding.
     */
    public void addToFundingList(Funding f){
        fundingArrayList.add(f);
    }

    /**
     * Remove all occurrence of specific funding after that funding project finished.
     * @param f one funding project.
     */
    private void removeFromFundingList(Funding f){
        if (fundingArrayList.contains(f)){
            for (int i = 0; i < Collections.frequency(fundingArrayList, f); i++){
                fundingArrayList.remove(f);
            }
        }
    }

    /**
     * Get funding list that specific user joints.
     * @return fundingArrayList.
     */
    public ArrayList<Funding> getFundingArrayList() {
        return fundingArrayList;
    }


    /**
     * Get the total balance for a specific user.
     * @return total balance.
     */
    public double getTotalBalance() {
        double total = 0;
        for (Account currentAccount : accountList) { // loops over all accounts
            if (currentAccount instanceof AssetAccount) {
                total += currentAccount.getBalance();  // adds balance of each asset account
            }
            if (currentAccount instanceof DebtAccount) {
                total -= currentAccount.getBalance();  // subtracts balance of each debt account
            }
        }
        return total; // obtain total balance
    }


    /**
     * Change the primary account to another chequing account
     * @param account A chequing account
     */
    public void setPrimaryAccount(Account account){
        for(int i = 0; i < this.primaryAccount.size(); i++){
            if(this.primaryAccount.get(i).getMoneyType().equals(account.getMoneyType())){
                this.primaryAccount.remove(i);
                break;
            }
        }

        this.primaryAccount.add(account);
    }

    /**
     * Get method for primaryAccount
     * @return ArrayList of Account represents the primary accounts
     */
    public ArrayList<Account> getPrimaryAccount() {
        return this.primaryAccount;
    }

    /**
     * Create a new account.
     * A user must has a chequing account of the currency before having other account with that currency
     * @param a account.
     */
    public void addAccount(Account a) {
        this.accountList.add(a);
        // Check if this user already has a primary account of this currency type
        if(this.findPrimaryType(a.getMoneyType()) != null){
            return;
        }
        // If not, check if this is a Chequing Account
        if(a instanceof ChequingAccount){
            primaryAccount.add(a); //Add it to primary account
        }

    }

    /**
     * Make e transfer between one account of self to other's account.
     * @param self my account
     * @param other destination account
     */
    public void interactE(String type, Account self, Account other, double amount){
        if (self.accessOut()){
            Transaction transaction = new Transaction(type, self, other, amount);
            transaction.execute();
        }
        else{
            System.out.println("!Invalid Account Selected! You Can't Transfer Out With This bank_system.accounts.Account!");
        }
    }

    /**
     * Find the primary account with corresponding currency name
     * @param moneyType name of the currency in String
     * @return primary account in Account if found, otherwise null
     */
    public Account findPrimaryType(String moneyType){
        for(Account a : primaryAccount){
            if(a.getMoneyType().equals(moneyType)){
                return a;
            }
        }
        return null;
    }

    /**
     * Deposit money into ATM machine
     * @param amount the amount of money
     * @param moneyType the type of currency
     */
    public void deposit(double amount, String moneyType){
        Account tempPrimary = this.findPrimaryType(moneyType);
        // if the user does not have account that stores this type of currency
        if(tempPrimary == null){
            System.out.printf("Cannot deposit %s into this account!\n", moneyType);
            return;
        }
        Transaction transaction = new Transaction("Deposit", "ATM", tempPrimary, amount);
        transaction.execute();

        try {
            recordDeposit(amount, moneyType);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to record deposit in a .txt file.
     * @param amount amount of money for the deposit
     * @param moneyType type of currency for the deposit.
     * @throws IOException handle in out exception
     */
    private void recordDeposit(double amount, String moneyType) throws IOException{
        final String depositFile = "phase2/data/deposit.txt";
        FileWriter fw = new FileWriter(depositFile, true);

        fw.write(String.format("%s deposit %.2f %s at %s.\n", this.getUsername(), amount, moneyType, ATM.getTime()));
        fw.close();
    }

    public void payBill(String payee, Account account, double amount) {
        Transaction transaction = new Transaction("bill-paying", account, payee, amount);
        transaction.setToCantUndo();//bill-paying transaction can undo.
        transaction.execute();
        try {
            saveToFile(payee, amount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find account under user by ID
     * @param accountID id of the account
     * @return null if account not found
     */
    public Account findAccount(String accountID){
        for(Account i : accountList){
            if(i.getId().equals(accountID)){
                return i;
            }
        }
        return null;
    }

    /**
     * Withdraw money from an account
     *
     * @param amount The amount of money
     * @param accountID The account ID
     * @throws IOException for saving the file.
     */
    public void withdraw(String moneyType, int amount, String accountID) throws IOException{
        Account withdrawAccount = findAccount(accountID);

        if(withdrawAccount == null) {
            System.out.println("Account doesn't exist!");
            return;
        }
        if(!withdrawAccount.accessOut()) {
            System.out.println("This account cannot transfer money out");
            return;
        }
        if(Bank.currentAC.handlingMoney(moneyType, amount)){
            Transaction transaction = new Transaction("withdraw", withdrawAccount, "ATM", amount);
            transaction.execute();
        }

        Bank.alertManager();
    }

    /**
     * A user interface for the one using bank_system.ATM.
     * @param br BufferedReader.
     * @throws IOException console input
     */
    public void userInterface(BufferedReader br) throws IOException {
        UserInterface UI = new UserInterface(this);
        String inputLine = null;
        do {
            if (inputLine != null) {
                switch (inputLine) {
                    case "1":
                        UI.viewBalanceUI();
                        break;
                    case "2":
                        UI.transferMoneyUI();
                        break;
                    case "3":
                        UI.depositUI();
                        break;
                    case "4":
                        UI.withdrawUI();
                        break;
                    case "5":
                        UI.changePrimaryAccountUI();
                        break;
                    case "6":
                        UI.changePasswordUI();
                        break;
                    case "7":
                        UI.checkAccountsCreationTime();
                        break;
                    case "8":
                        UI.checkTransactionsUI();
                        break;
                    case "9":
                        UI.buyFundsUI();
                        break;
                    case "10":
                        UI.buyStockUI();
                        break;
                    case "11":
                        UI.sellStockUI();
                        break;
                    case  "12":
                        UI.EMTUI();
                        break;
                    default:
                        System.out.println("INVALID INPUT.");
                        break;
                }
            }
            System.out.println("\nMain menu:");
            System.out.println("Type 1 to view the balance of each account.");
            System.out.println("Type 2 to transfer money.");
            System.out.println("Type 3 to deposit");
            System.out.println("Type 4 to withdraw");
            System.out.println("Type 5 to check or change primary accounts");
            System.out.println("Type 6 to change password");
            System.out.println("Type 7 to check account creation time");
            System.out.println("Type 8 to check transactions");
            System.out.println("Type 9 to manage funding projects");
            System.out.println("Type 10 to purchase stock");
            System.out.println("Type 11 to sell stock");
            System.out.println("Type 12 to interact EMT");
            System.out.println("Type LOGOUT to exit.");
        } while (!(inputLine = br.readLine().toLowerCase()).equals("logout"));
        System.out.println("Thank you! See you next time!");
    }

    /**
     * Save completed payments.
     */
    private void saveToFile(String payee, double amount) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("phase2/data/outgoing.txt", true));
        writer.write(getUsername() + " made a" + amount + " dollar(s) payment to " + payee + "\n");

        writer.close();
    }

    /**
     * Purchase a new stock, and add the information to the stock list
     * @param stockID the id for the stock
     * @param amount the amount of the stock
     * @param price the price for the stock when purchase
     * @param account the account pay the money
     */
    public void purchaseStock(String stockID, int amount, double price, Account account){
        Object[] newInfo = {stockID, amount};
        stockArrayList.add(newInfo);
        Transaction transaction = new Transaction("buy Stock", account, "Bank", amount*price);
        transaction.setToCantUndo();
        transaction.execute();
    }

    /**
     * Get the name for this stock.
     * @param index The index of the stock stored in the list.
     * @return The name for the stock.
     */
    public String getStockName(int index){
        return (String) stockArrayList.get(index)[0];
    }
    /**
     * Get the amount for this stock.
     * @param index The index of the stock stored in the list.
     * @return The amount for the stock.
     */
    public int getStockAmount(int index){
        return (int) stockArrayList.get(index)[1];
    }

    /**
     * Purchase a new stock, and update the information to the stock list. If the
     * amount of this stock is 0, delete the stock information.
     * @param stockID the id for the stock
     * @param amount the amount of the stock
     * @param price the price for the stock when purchase
     * @param account the account store the money
     */
    public void sellStock(String stockID, int amount, double price, Account account){
        int index = sellHelper(stockID);
        stockArrayList.get(index)[1] = (int)stockArrayList.get(index)[1] - amount;
        if ((int)stockArrayList.get(index)[1] == 0){
            stockArrayList.remove(index);
        }
        Transaction transaction = new Transaction("sell Stock", "Bank", account, amount*price);
        transaction.setToCantUndo();
        transaction.execute();
    }

    /**
     * @param id The String want to check
     * @return the index of object in stock list who has the same id.
     */
    private int sellHelper (String id){
        ArrayList<String> temp = new ArrayList<>();
        for (Object[] s: stockArrayList){
            temp.add((String)s[0]);
        }
        return temp.indexOf(id);
    }

    /**
     * Update Funding when completed.
     * @param o a Funding
     * @param obj money returned.
     */
    public void update(Observable o, Object obj){
        removeFromFundingList((Funding) o);
        Transaction t = new Transaction("Join Funding Project", "Bank", this.findPrimaryType("CAD"),
                Collections.frequency(fundingArrayList, o) * ((double) obj));
        t.setToCantUndo();
        t.execute();
    }

    /**
     * Send a new EMT. The money will store in the bank until the receiver accept the money.
     * @param senderA The account information about sender.
     * @param receiverA The account information about receiver.
     * @param amount The amount of money for this EMT.
     * @param key The password for this EMT.
     */
    public void sendEMT(Account senderA, Account receiverA, double amount, String key){
        EMT newEMT = new EMT(senderA, receiverA, amount, key);
        Bank.addNewEMT(newEMT);
        Transaction transaction = new Transaction("send EMT", senderA, "Bank", amount);
        transaction.setToCantUndo();
        transaction.execute();
    }

    /**
     * Accept the target EMT. And the EMT information will be deleted in bank.
     * @param EMTid The if for the target EMT.
     */
    public void acceptEMT(String EMTid){
        Account receiver;
        double amount;
        for(EMT e: Bank.getEMTArrayList()){
            if (EMTid.equals(e.getEMTid())){
                receiver = e.getReceiver();
                amount = e.getAmount();
                Transaction transaction = new Transaction("receive EMT", "Bank", receiver, amount);
                transaction.setToCantUndo();
                transaction.execute();
                Bank.removeEMT(e);
                break;
            }
        }
    }

    @Override
    public String fileOutputFormat(){
        return String.format("user,%s,%s", getUsername(), getPassword());
    }
}


