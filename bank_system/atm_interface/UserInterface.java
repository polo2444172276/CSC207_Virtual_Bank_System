package bank_system.atm_interface;

import bank_system.FinancialProduct.Funding;
import bank_system.*;
import bank_system.accounts.*;
import bank_system.users.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import bank_system.FinancialProduct.Stock;
public class UserInterface extends Interface {
    private final User self;
    public UserInterface(User user){
        this.self = user;
    }
    /**
     * User interface for withdraw
     *
     * @throws IOException console input
     */
    public void withdrawUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Withdraw canceled!");
                    return;
                }
                System.out.println("Please only enter number from 1 to " + Bank.currentAC.getCurrencyList().size());
            }

            for (int i = 0; i < Bank.currentAC.getCurrencyList().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to withdraw  " + Bank.currentAC.getCurrencyList().get(i).getType());
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                Bank.currentAC.getCurrencyList().size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));
        int index_type  = Integer.parseInt(inputLine) - 1;

        System.out.println("Here's the list of your account that you can withdraw from.");
        for(Account i : self.getAccountList()) {
            if(i.accessOut() && i.accessWithdraw() &&
                    (i.getMoneyType().equals(Bank.currentAC.getCurrencyList().get(index_type).getType()))) {
                System.out.println(i);
            }
        }

        int amount;
        String accountID;
        inputLine = null;
        do {
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Withdraw canceled!");
                    return;
                }
                System.out.println("Please enter the correct ID!");
            }

            System.out.println("Please enter the account ID that you wish to withdraw. \nType CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                self.findAccount(inputLine) != null &&
                self.findAccount(inputLine).accessOut()));

        accountID = inputLine;
        inputLine = null;

        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Withdraw canceled!");
                    return;
                }
                System.out.println("Please re-enter the amount!");
            }
            System.out.println("Please enter the amount of money you wish to withdraw. " +
                    "Type CANCEL to cancel:");
            System.out.println("Note: can only be multiple of 5!");
        } while(!isNumber(inputLine = br.readLine()) || Integer.parseInt(inputLine)%5 != 0);

        amount = Integer.parseInt(inputLine);

        self.withdraw(Bank.currentAC.getCurrencyList().get(index_type).getType(), amount, accountID);
    }

    /**
     * deposit interface for user
     *
     * @throws IOException File input
     */
    public void depositUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter deposit type(CASH or CHEQUE)");
        System.out.println("Type CANCEL to cancel deposit.");
        String depositType = br.readLine();
        depositType = depositType.toLowerCase();

        // ask the user to deposit with cash or cheque
        while(!depositType.toLowerCase().equals("cash") && !depositType.toLowerCase().equals("cheque")){
            if(depositType.toLowerCase().equals("cancel")){
                System.out.println("Deposit CANCELED!");
                return;
            }

            System.out.println("Please re-enter deposit type(CASH or CHEQUE)");
            System.out.println("Type CANCEL to cancel deposit.");
            depositType = br.readLine();
            depositType = depositType.toLowerCase();
        }

        if(depositType.equals("cash")){
            depositCashUI();
        }
        else {
            depositChequeUI();
        }
    }

    /**
     * Cheque deposit interface for user
     * @throws IOException console input
     */
    private void depositChequeUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // The type of currency on the cheque
        System.out.println("Please enter the name of currency on the cheque (In capital letter).");
        System.out.println("Type CANCEL to cancel");

        String currencyName = br.readLine();
        while(currencyName.length() != 3 || !currencyName.matches("[A-Z]+")){
            if(currencyName.toLowerCase().equals("cancel")) {
                System.out.println("Canceled");
                return;
            }

            System.out.println("Please re-enter the correct name of currency on the cheque.");
            System.out.println("Type CANCEL to cancel");
            currencyName = br.readLine();
        }

        // The amount of money on a cheque
        System.out.println("Please enter the value of the cheque.");
        System.out.println("Type CANCEL to cancel");

        String inputLine;
        while(!isDouble(inputLine = br.readLine()) || inputLine.equals("-") || Double.parseDouble(inputLine) <= 0){
            if(inputLine.toLowerCase().equals("cancel")){
                System.out.println("Canceled!");
                return;
            }
            System.out.println("Please re-enter the value of the cheque.");
            System.out.println("Type CANCEL to cancel");
        }
        double value = Double.parseDouble(inputLine);
        self.deposit(value, currencyName);
    }

    /**
     * Cash deposit interface for user
     * @throws IOException console input
     */
    private void depositCashUI() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Tell user what currency he/she can deposit into this ATM.
        System.out.println("Please enter the corresponding number for currency type.");
        System.out.println("NOTE: This ATM can only store:");
        int counter = 1;
        for(Currency c : Bank.currentAC.getCurrencyList()){
            System.out.printf("%d. %s\n", counter++, c.getType());
        }
        System.out.println("If you wish to store another currency, please go to other ATM or contact us!");
        System.out.println("Type CANCEL to cancel deposit");

        // Get the currency that the user wish to deposit
        String inputLine = br.readLine();
        while(!isNumber(inputLine) ||
                Integer.parseInt(inputLine) > Bank.currentAC.getCurrencyList().size() ||
                Integer.parseInt(inputLine) == 0){
            if(inputLine.toLowerCase().equals("cancel")){
                System.out.println("Deposit CANCELED!");
                return;
            }
            System.out.printf("Please re-enter number from 1 to %d.\n", Bank.currentAC.getCurrencyList().size());
            inputLine = br.readLine();
        }
        Currency targetCurrency = Bank.currentAC.getCurrencyList().get(Integer.parseInt(inputLine)-1);
        // Check if user has a primary account for this type of currency
        if(self.findPrimaryType(targetCurrency.getType()) == null){
            System.out.printf("You do not have a primary account for %s. " +
                    "Please contact our employee to create a new account!\n", targetCurrency.getType());
            return;
        }

        // Get the amount of cash by its denomination
        System.out.println("Please enter the amount of different denomination cash you wish to deposit respectively (From high to low).");

        int totalSum = 0;
        for(int i = 0; i < targetCurrency.getDenomination().size(); i++){
            do {
                System.out.printf("%s%d:", targetCurrency.getSymbol(), targetCurrency.getDenomination().get(i));
            } while(!isNumber(inputLine = br.readLine()));

            int value = Integer.parseInt(inputLine);
            totalSum += targetCurrency.getDenomination().get(i) * value;
            targetCurrency.moneyInOut(i, value);
        }

        self.deposit(totalSum, targetCurrency.getType());
        System.out.println("DONE!");
    }


    /**
     * User interface for viewing the total balance of a specific user.
     */
    public void viewBalanceUI() {
        //print out all account information
        System.out.println("Here's the list of your account.");
        for(Account i : self.getAccountList()) {
            System.out.println(i);
        }
        //calculate net total for each currency
        System.out.println("Net Balance:");

        ArrayList<String> currencyName = new ArrayList<>();
        for(Account i : self.getAccountList()) {
            if(!currencyName.contains(i.getMoneyType())){
                currencyName.add(i.getMoneyType());
                double totalSum = 0;

                for(Account j : self.getAccountList()){
                    if(j.getMoneyType().equals(i.getMoneyType())){
                        if(j instanceof AssetAccount){
                            totalSum += j.getBalance();
                        }
                        if(j instanceof DebtAccount){
                            totalSum -= j.getBalance();
                        }
                    }
                }
                System.out.printf("%S: %.2f\n", i.getMoneyType(), totalSum);
            }
        }
    }

    /**
     * UI for changing primary account
     * @throws IOException Console input
     */
    public void changePrimaryAccountUI() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        System.out.println("Your current primary accounts are:");
        for(Account account: self.getPrimaryAccount()){
            System.out.println(account);
        }
        System.out.println();
        System.out.println("You can select from following the new primary accounts:");
        for(Account account : self.getAccountList()){
            if(account instanceof ChequingAccount){
                System.out.println(account);
            }
        }
        Account targetAccount;
        do {
            System.out.println("Please enter an accountID to set it as your new primary account.");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();

            if (inputLine.toLowerCase().equals("cancel")) {
                System.out.println("Change canceled!");
                return;
            }
            targetAccount  = self.findAccount(inputLine);
            if(!(targetAccount instanceof ChequingAccount)){
                System.out.println("This account cannot be set as primary account!");
            }
        } while(!(targetAccount instanceof ChequingAccount));
        self.setPrimaryAccount(targetAccount);
        System.out.println("Done!");
    }

    /**
     * An interface method for user to check his/her account(s) creation time.
     */
    public void checkAccountsCreationTime(){
        System.out.println("Here is the account creation for all your accounts:");
        for(Account account : self.getAccountList()){
            System.out.printf("Account ID:%s was created on %s\n", account.getId(), account.getCreationTime());
        }
    }

    /**
     * An interface method for user to check his/her transactions
     * @throws IOException handle in out exception
     */
    public void checkTransactionsUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        System.out.println("Here are the accounts that you can transfer money out:");
        for(Account account : self.getAccountList()){
            if(account.accessOut()){
                System.out.println(account);
            }
        }
        System.out.println("Please enter the account ID that you want to check.");
        System.out.println("Type CANCEL to cancel:");
        Account account;
        while(!isNumber(inputLine = br.readLine()) || (account = findAllAccount(inputLine)) == null
                || findAllAccount(inputLine).getTransactionStack().isEmpty()){
            if(inputLine.toLowerCase().equals("cancel")){
                System.out.println("Canceled!");
                return;
            }
            if(!isNumber(inputLine)){
                System.out.println("Please only enter numbers!");
            }
            if (findAllAccount(inputLine) == null){
                System.out.println("Account cannot be found!");
            }
            else{
                System.out.println("There is no transaction on this account");
            }

            System.out.println("Please re-enter the account ID that you wish to check.");
            System.out.println("Type CANCEL to cancel:");
        }
        System.out.println("Here are the transaction(s) on this account");
        System.out.println("If you want to cancel any of them, please contact the manager!");
        System.out.println(PrintTransHelper(account));


    }

    /**
     * Interface for user to transfer money to another account or pay a bill.
     * @throws IOException Console input
     */
    public void transferMoneyUI() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Transfer money canceled!");
                    return;
                }
                System.out.println("Please enter correctly!");
            }
            System.out.println("Type 1 to transfer to another user.");
            System.out.println("Type 2 to pay bill.");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();
        } while(!inputLine.equals("1") && !inputLine.equals("2"));

        String type = inputLine;

        System.out.println("Here are the accounts that you can transfer money out:");
        for(Account account : self.getAccountList()){
            if(account.accessOut()){
                System.out.println(account);
            }
        }

        inputLine = null;
        Account selectedAccount;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Transfer money canceled!");
                    return;
                }
                System.out.println("Please enter correctly!");
            }
            System.out.println("Please select one of the account above.");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();
        } while(!isNumber(inputLine) ||
                (selectedAccount = self.findAccount(inputLine)) == null ||
                !selectedAccount.accessOut());

        if(type.equals("1")){
            userTransferUI(selectedAccount);
        } else {
            payBillUI(selectedAccount);
        }

    }

    /**
     * Sub-interface for user to transfer money to another user/user's account
     * @param selectedAccount the account to transfer from
     * @throws IOException Console input
     */
    private void userTransferUI(Account selectedAccount) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter the username or accountID that you wish to transfer money to");
        System.out.println("Type CANCEL to cancel");
        String inputLine = br.readLine();
        if(inputLine.toLowerCase().equals("cancel")) {
            System.out.println("Canceled!");
            return;
        }

        Account targetAccount;
        // if the user enters an account ID
        if(isNumber(inputLine)){
            targetAccount = findAllAccount(inputLine);
            // if the account ID cannot be found
            if(targetAccount == null){
                System.out.println("Account/User cannot be found. Please make sure you enter correctly!");
                return;
            }

            // money type must be the same
            if(!selectedAccount.getMoneyType().equals(targetAccount.getMoneyType())) {
                System.out.printf("Cannot transfer %s to this user.", selectedAccount.getMoneyType());
                return;
            }
        } else if(isAlphanumeric(inputLine)) {
            User tempUser = findUser(inputLine);
            // check if this user exists
            if(tempUser != null){
                // find the primary account for this currency type
                targetAccount = tempUser.findPrimaryType(selectedAccount.getMoneyType());

                if(targetAccount == null){
                    System.out.printf("Cannot transfer %s to this user.", selectedAccount.getMoneyType());
                    return;
                }
            }
            else {
                // this user cannot be found
                System.out.println("User cannot be found. Please make sure you enter correctly!");
                return;
            }
        } else { // when user didn't enter something weird and try to break the system. Nice try!
            System.out.println("Please enter correctly!");
            return;
        }

        // if user selected the same account to transfer to
        if(targetAccount == selectedAccount){
            System.out.println("Cannot transfer money between the same account!");
            return;
        }

        // cannot transfer money to this account
        if(!targetAccount.accessIn()){
            System.out.println("Cannot transfer money into target account!");
            return;
        }

        inputLine = null;
        do {
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                }
            }
            System.out.println("Please enter the amount of money that you wish to transfer");
            System.out.println("Type CANCEL to cancel");
        } while(!isDouble(inputLine = br.readLine()) || (inputLine.equals("-"))|| Double.parseDouble(inputLine) <= 0);
        double value = Double.parseDouble(inputLine);

        if(selectedAccount instanceof AssetAccount){
            if(value > selectedAccount.getBalance()){
                System.out.println("You don't have enough money in this account");
                return;
            }
        }

        self.interactE("inter-user", selectedAccount, targetAccount, value);
        System.out.println("Done!");

    }

    /**
     * Sub-interface for user to pay bill
     * @param selectedAccount the account to transfer from
     * @throws IOException Console input
     */
    private void payBillUI(Account selectedAccount) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String inputLine;
        System.out.println("Please enter the name of the payee.");
        System.out.println("Type CANCEL to cancel");
        inputLine = br.readLine();
        if(inputLine.toLowerCase().equals("cancel")){
            System.out.println("Canceled!");
            return;
        }
        String payeeName = inputLine;

        inputLine = null;
        do {
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                }
            }
            System.out.println("Please enter the amount of money that you wish to transfer");
            System.out.println("Type CANCEL to cancel");
        } while(!isDouble(inputLine = br.readLine()) || Double.parseDouble(inputLine) <= 0);
        double value = Double.parseDouble(inputLine);

        if(selectedAccount instanceof AssetAccount){
            if(value > selectedAccount.getBalance()){
                System.out.println("You don't have enough money in this account");
                return;
            }
        }

        self.payBill(payeeName, selectedAccount, value);
        System.out.println("Done!");
    }

    /**
     * Interface for user to change password
     * @throws IOException console input
     */
    public void changePasswordUI() throws IOException{
        super.changePasswordUI(self);
    }

    /**
     * Interface for user to join new funding projects.
     * @throws IOException console input
     */
    public void buyFundsUI() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do {
            Set<Funding> s = new HashSet<>(self.getFundingArrayList());
            //show currently joined
            System.out.println("You have jointed below funding projects:");
            for (Funding fund : s) {
                System.out.println(String.format("Funding project name:%s, #Units Owned:%s, Unit Price:%s CAD, Money Back in %s month(s), POTENTIAL Interest Rate:%s", fund.getName(),
                        Collections.frequency(self.getFundingArrayList(), fund), fund.getUnitPrice(), fund.getProjectEnds(), (fund.getInterestShifter() + 0.1)));
            }
            if (self.getFundingArrayList().isEmpty()) {
                System.out.println("None");
            }
            //show currently available funding projects
            if (Bank.getFundingArrayList().isEmpty()) {
                System.out.println("Sorry, there is no available funding project at current time period, please type CANCEL to go back to main menu.");
            } else {
                System.out.println("Please select a currently available funding projects are listed BELOW to continue:");
            }
            do {
                if (inputLine != null) {
                    if (inputLine.toLowerCase().equals("cancel")) {
                        System.out.println("Back To Main Menu");
                        return;
                    }
                    if (Bank.getFundingArrayList().isEmpty()) {
                        System.out.println("Sorry, there is no available funding project at current time period, please type CANCEL to go back to main menu.");
                    }
                }

                for (int i = 0; i < Bank.getFundingArrayList().size(); i++) {
                    int index = i + 1;
                    System.out.println("Type " + index + " to join " + Bank.getFundingArrayList().get(i).getName() +
                            " With unit price " + Bank.getFundingArrayList().get(i).getUnitPrice() + " and POTENTIAL interest rate: " +
                            (Bank.getFundingArrayList().get(i).getInterestShifter() + 0.1));
                }
                System.out.println("Type CANCEL to cancel: ");
            } while (!(isNumber(inputLine = br.readLine()) &&
                    Bank.getFundingArrayList().size() >= Integer.parseInt(inputLine) &&
                    Integer.parseInt(inputLine) >= 1));
            Funding fund = Bank.getFundingArrayList().get(Integer.parseInt(inputLine) - 1);

            System.out.println("Here's the list of your account that you can use.");
            for (Account i : self.getAccountList()) {
                if (i.accessOut() && (i.getMoneyType().equalsIgnoreCase("cad"))) {
                    System.out.println(i);
                }
            }

            int amount;
            String accountID;
            inputLine = null;
            do {
                if (inputLine != null) {
                    if (inputLine.toLowerCase().equals("cancel")) {
                        System.out.println("Back To Main Menu!");
                        return;
                    }
                    System.out.println("Please enter the correct ID!");
                }

                System.out.println("Please enter the account ID that you wish to use. \nType CANCEL to cancel: ");
            } while (!(isNumber(inputLine = br.readLine()) &&
                    self.findAccount(inputLine) != null &&
                    self.findAccount(inputLine).accessOut()));

            accountID = inputLine;
            inputLine = null;

            do {
                if (inputLine != null) {
                    if (inputLine.toLowerCase().equals("cancel")) {
                        System.out.println("Back To Main Menu!");
                        return;
                    }
                    System.out.println("Please re-enter the amount!");
                }
                System.out.println("Please enter the amount of unit you wish to purchase. " +
                        "Type CANCEL to cancel:");
                System.out.println("Note: amount can only be integers!");
            } while (!isNumber(inputLine = br.readLine()));

            amount = Integer.valueOf(inputLine);

            System.out.println(amount);

            fund.joinFund(self.findAccount(accountID), self, amount);
        } while (!(inputLine.equalsIgnoreCase("cancel")));
    }

    /**
     * The interface which can be used to but stock
     * @throws IOException console input
     */
    public void buyStockUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Purchase canceled!");
                    return;
                }
            }

            for (int i = 0; i < Bank.getStockArrayList().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to buy stock NO." + Bank.getStockArrayList().get(i).getStockName() +
                        "  The price for this stock is: " + Bank.getStockArrayList().get(i).getCurrentPrice());
            }
            if (Bank.getStockArrayList().size() == 0){
                System.out.println("There is no stock this time.");
            } else {
                System.out.println("Please only enter number from 1 to " + Bank.getStockArrayList().size() +
                        " to buy stock");
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                Bank.getStockArrayList().size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));
        String stockid = Bank.getStockArrayList().get(Integer.parseInt(inputLine) - 1).getStockName();
        double price = Bank.getStockArrayList().get(Integer.parseInt(inputLine) - 1).getCurrentPrice();

        inputLine = null;
        do {
            if (inputLine != null) {
                if (inputLine.toLowerCase().equals("cancel")) {
                    System.out.println("Purchase canceled!");
                    return;
                }
                System.out.println("Please re-enter the amount!");
            }
            System.out.println("Please enter the amount of unit for this stock you wish to purchase. " +
                    "\nType CANCEL to cancel:");
            System.out.println("Note: amount can only be integers!");
        } while (!isNumber(inputLine = br.readLine()));
        int amount = Integer.valueOf(inputLine);

        System.out.println("Here is the list of account that can be used to buy stock:");
        for(Account account : self.getAccountList()){
            if(account instanceof ChequingAccount && account.getMoneyType().equals("CAD")){
                System.out.println(account);
            }
        }
        Account targetAccount;
        do {
            System.out.println("Please enter an accountID to buy stock");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();

            if (inputLine.toLowerCase().equals("cancel")) {
                System.out.println("Purchase canceled!");
                return;
            }
            targetAccount  = self.findAccount(inputLine);
            if(!(targetAccount instanceof ChequingAccount && targetAccount.getBalance() >= price * amount
                    && targetAccount.getMoneyType().equals("CAD"))){
                System.out.println("This account does not have much money to buy those stock!");
                System.out.println("Please choose an account or decrease the number of stock you want to buy");
            }
        } while(!(targetAccount instanceof ChequingAccount && targetAccount.getBalance() >= price * amount
                && targetAccount.getMoneyType().equals("CAD")));

        self.purchaseStock(stockid, amount, price, targetAccount);
        System.out.println("Done!");
    }

    /**
     * The interface which can be used to sell stock.
     * @throws IOException console input
     */
    public void sellStockUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Sell canceled!");
                    return;
                }
            }
            if (self.getStockArrayList().size() == 0){
                System.out.println("You don't hold any stock.");
            }else{
                System.out.println("Please only enter number from 1 to " + self.getStockArrayList().size() +
                        " to sell stock");
            }
            for (int i = 0; i < self.getStockArrayList().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to sell " + self.getStockName(i) +
                        "  The price for this stock is: " + stockPriceHelper(self.getStockName(i)) +
                        "  The amount of this stock you have is " + self.getStockAmount(i));
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                self.getStockArrayList().size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));
        String stockID =self.getStockName(Integer.parseInt(inputLine)-1);
        double price = stockPriceHelper(stockID);

        inputLine = null;
        do {
            if (inputLine != null) {
                if (inputLine.toLowerCase().equals("cancel")) {
                    System.out.println("Sell canceled!");
                    return;
                }
                System.out.println("Please re-enter the amount!");
            }
            System.out.println("Please enter the amount of unit for this stock you wish to Sell. " +
                    "Type CANCEL to cancel:");
            System.out.println("Note: amount can only be integers!");
        } while (!isNumber(inputLine = br.readLine()));
        int amount = Integer.valueOf(inputLine);

        System.out.println("Here is the list of account that can be used to store the money:");
        for(Account account : self.getAccountList()){
            if(account instanceof ChequingAccount && account.getMoneyType().equals("CAD")){
                System.out.println(account);
            }
        }
        Account targetAccount;
        do {
            System.out.println("Please enter an accountID to store money");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();

            if (inputLine.toLowerCase().equals("cancel")) {
                System.out.println("Sell canceled!");
                return;
            }
            targetAccount  = self.findAccount(inputLine);
            if(!(targetAccount instanceof ChequingAccount)){
                System.out.println("Cannot store money in this account!");
                System.out.println("Please choose an account which you want to store the money in");
            }
        } while(!(targetAccount instanceof ChequingAccount));

        self.sellStock(stockID, amount, price, targetAccount);
        System.out.println("Done!");
    }

    /**
     * A helper which can be used to get the price for certain in the stock list.
     * @param stockID The name of this stock
     * @return the current price for the price.
     */
    private double stockPriceHelper(String stockID){
        double result = 0;
        for (Stock stock: Bank.getStockArrayList()){
            if (stock.getStockName().equals(stockID)){
                result = stock.getCurrentPrice();
                break;
            }
        }
        return result;
    }

    /**
     * Interface for EMT
     * @throws IOException console input
     */
    public void EMTUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String inputLine = null;
        do {
            if(inputLine != null){
                System.out.println("Please enter correctly!");
            }
            System.out.println("Type 1 to send EMT.");
            System.out.println("Type 2 to accept EMT.");
            System.out.println("Type CANCEL to back to main menu.");
            inputLine = br.readLine();
        } while(!(inputLine.equals("1") || inputLine.equals("2")));

        if(inputLine.equals("1")){
            sendEMTUI();
        }
        else{
            acceptEMT();
        }
    }

    /**
     * The interface can be used to send EMT.
     * @throws IOException console input
     */
    private void sendEMTUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        System.out.println("Here is the list of account that can be used to send EMT:");
        for(Account account : self.getAccountList()){
            if(account instanceof ChequingAccount && account.getMoneyType().equals("CAD")){
                System.out.println(account);
            }
        }
        Account selectedAccount;
        do {
            System.out.println("Please enter an accountID to send EMT");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();

            if (inputLine.toLowerCase().equals("cancel")) {
                System.out.println("Purchase canceled!");
                return;
            }
            selectedAccount  = self.findAccount(inputLine);
            if(!(selectedAccount instanceof ChequingAccount)){
                System.out.println("This account cannot be used to send EMT!");
                System.out.println("Please choose a Canadian chequing account");
            }
        } while(!(selectedAccount instanceof ChequingAccount && selectedAccount.getMoneyType().equals("CAD")));

        Account targetAccount;
        User tempUser;
        // if the user enters an account ID
        do{
            targetAccount = null;
            System.out.println("Please enter the username or accountID that you wish to transfer money to");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();
            if(inputLine.toLowerCase().equals("cancel")) {
                System.out.println("Canceled!");
                return;
            }
            if(isNumber(inputLine)){
                targetAccount = findAllAccount(inputLine);
            } else if(isAlphanumeric(inputLine)) {
                tempUser = findUser(inputLine);
                if(tempUser != null){
                    targetAccount = tempUser.findPrimaryType("CAD");
                }
            }
        } while(!(targetAccount != selectedAccount &&
                targetAccount instanceof ChequingAccount));

        inputLine = null;
        do {
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                } else if (isNumber(inputLine)){
                    if (targetAccount.getBalance() < Integer.parseInt(inputLine)){
                        System.out.println("You don't have enough money in this account.  Balance: " + selectedAccount.getBalance());
                    }
                }
            }
            System.out.println("Please enter the amount of money that you wish to send");
            System.out.println("Type CANCEL to cancel");
        } while(!isDouble(inputLine = br.readLine()) ||
                selectedAccount.getBalance() < Double.parseDouble(inputLine) ||
                Double.parseDouble(inputLine) <= 0);

        double value = Double.parseDouble(inputLine);

        inputLine = null;
        do {
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                }
            }
            System.out.println("Please set the password to accept this EMT");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();
        } while(inputLine.isEmpty());
        String key = inputLine;
        self.sendEMT(selectedAccount, targetAccount, value, key);
        System.out.println("Done!");
    }

    /**
     * The interface to accept EMT.
     * @throws IOException console input
     */
    private void acceptEMT() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        ArrayList<EMT> list = getAccountEMTHelper();
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                }
            }

            for (int i = 0; i < list.size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to accept EMT NO." + list.get(i).getEMTid() +
                        ".       Amount " + list.get(i).getAmount());
            }
            if (self.getStockArrayList().size() == 0){
                System.out.println("You don't have any EMT yet.");
            } else {
                System.out.println("Please only enter number from 1 to " + list.size() +
                        " to choose one EMT to accept");
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) && list.size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));
        String id = list.get(Integer.parseInt(inputLine) - 1).getEMTid();
        String password = list.get(Integer.parseInt(inputLine) - 1).getPassword();

        inputLine = null;
        do {
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                }
            }
            System.out.println("Please enter the correct password to accept this EMT");
            System.out.println("Type CANCEL to cancel");
            inputLine = br.readLine();
        } while(!inputLine.equals(password));

        self.acceptEMT(id);
        System.out.println("Done!");
    }

    /**
     * The helper to get the EMT list which the user can accept
     * @return A list of EMT
     */
    private ArrayList<EMT> getAccountEMTHelper(){
        ArrayList<EMT> result = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        for (Account account: self.getAccountList()){
            temp.add(account.getId());
        }
        for (EMT emt: Bank.getEMTArrayList()){
            if (temp.contains(emt.getReceiver().getId())){
                result.add(emt);
            }
        }
        return result;
    }
}
