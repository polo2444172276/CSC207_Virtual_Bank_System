package bank_system;


import bank_system.FinancialProduct.Funding;
import bank_system.FinancialProduct.Stock;
import bank_system.accounts.Account;
import bank_system.accounts.AccountsFactory;
import bank_system.users.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observer;

/**
 * A helper class for Bank to import and output data into txt files
 */
class FileLoader {

    private final String loginDataFileName = "phase2/data/loginData.txt";
    private final String accountDataFileName = "phase2/data/accountData.txt";
    private final String cashDataFileName = "phase2/data/cashData.txt";
    private final String transactionDataFileName = "phase2/data/transactionData.txt";
    private final String stockDataFileName = "phase2/data/stockData.txt";
    private final String fundDataFileName = "phase2/data/fundData.txt";
    private final String emtDateFileName = "phase2/data/emtData.txt";

    /**
     * Importing login information from file under data/loginData.txt
     * All data should be the following format with a comma(,) in between:
     *  0. type(user/manager/staff)
     *  1. username
     *  2. password
     * @throws IOException file reader
     */
    private void importLogin() throws IOException{
        FileReader fr = new FileReader(loginDataFileName);
        BufferedReader br = new BufferedReader(fr);

        String currentLine;
        PersonFactory pf = new PersonFactory();

        while((currentLine = br.readLine()) != null){
            String[] information = currentLine.split(",");
            Person tempP = pf.getPeople(information[0], information[1], information[2]);
            Bank.getPersonList().add(tempP);
        }

        br.close();
        fr.close();
    }

    /**
     * Importing all account information from file under data/accountData.txt
     * Every 3 line represents a single account
     *   First line indicate primary type for corresponding user in second line.
     *   Second line represents the list of user who has the account
     *   Third line represents the information of this account.
     * All account are in accountID order!
     * @throws IOException file reader
     */

    private void importAccount() throws IOException{
        FileReader fr = new FileReader(accountDataFileName);
        BufferedReader br = new BufferedReader(fr);

        String currentLine;
        AccountsFactory af = new AccountsFactory();
        while((currentLine = br.readLine()) != null) {
            String[] primaryInformation = currentLine.split(",");
            String[] usernameInformation = (br.readLine()).split(",");
            String[] accountInformation = (br.readLine()).split(",");

            String accountType = accountInformation[0];
            double accountBalance = Double.parseDouble(accountInformation[1]);
            String currencyName = accountInformation[2];
            MyDate creationTime = new MyDate(accountInformation[3]);


            Account newAccount = af.getAccount(accountType, creationTime, currencyName);
            newAccount.setBalance(accountBalance);

            for(int index = 0; index < usernameInformation.length; index++){
                User targetUser = findUser(usernameInformation[index]);
                Objects.requireNonNull(targetUser).addAccount(newAccount);
                if(Integer.parseInt(primaryInformation[index]) == 1){
                    targetUser.setPrimaryAccount(newAccount);
                }
            }
        }
        br.close();
        fr.close();
    }

    /**
     * Importing the amount of cash that an atm current had before shutdown/restart, under data/cashData.txt
     * Every 3 lines represent a currency within a atm machine,
     * Each machine is separated by ***
     * @throws IOException file reader
     */
    private void importCash() throws IOException{
        FileReader fr = new FileReader(cashDataFileName);
        BufferedReader br = new BufferedReader(fr);

        String inputLine;
        ArrayList<Currency> currencyArrayList = new ArrayList<>();
        while((inputLine = br.readLine()) != null){
            if(inputLine.charAt(0) == '*'){
                ATM a = new ATM(currencyArrayList);
                Bank.getAtmArrayList().add(a);
                currencyArrayList = new ArrayList<>();
                continue;
            }
            String name = inputLine.split(",")[0];
            String symbol = inputLine.split(",")[1];
            Currency newCurrency = new Currency(name, symbol);

            inputLine = br.readLine();
            String[] denomination = inputLine.split(",");

            inputLine = br.readLine();
            String[] value = inputLine.split(",");

            for(int i = 0; i < value.length; i++){
                newCurrency.addDenomination(Integer.parseInt(denomination[i]), Integer.parseInt(value[i]));
            }
            currencyArrayList.add(newCurrency);
        }

        br.close();
        fr.close();
    }

    private void importTransaction() throws IOException{
        FileReader fr = new FileReader(transactionDataFileName);
        BufferedReader br = new BufferedReader(fr);

        String currentLine;
        while((currentLine = br.readLine()) != null){
            String[] information = currentLine.split(",");

            Account targetAccount = findAllAccount(information[0]);
            if(targetAccount == null){
                System.out.println("Input error!");
                System.exit(0);
            }

            String type = information[1];

            Object out;
            if(information[2].matches("[0-9]+")){
                out = findAllAccount(information[2]);
            } else {
                out = information[2];
            }

            Object in;
            if(information[3].matches("[0-9]+")){
                in = findAllAccount(information[3]);
            } else {
                in = information[3];
            }

            double amount = Double.parseDouble(information[4]);
            MyDate myDate = new MyDate(information[5]);
            Boolean undoable = (Integer.parseInt(information[6])==1);

            Transaction transaction = new Transaction(type, out, in, amount, myDate, undoable);
            targetAccount.getTransactionStack().push(transaction);
        }
    }

    private void importStock() throws IOException{
        FileReader fw = new FileReader(stockDataFileName);
        BufferedReader br = new BufferedReader(fw);

        String currentLine;
        while((currentLine = br.readLine()) != null) {
            String[] information = currentLine.split(",");
            double price = Double.parseDouble(information[0]);
            double rate = Double.parseDouble(information[1]);

            Stock stock = new Stock(price, rate);
            Bank.getStockArrayList().add(stock);

            if((currentLine = br.readLine()) != null){
                String[] userList = currentLine.split(",");

                if(userList.length >= 2) {
                    for (int i = 0; i < userList.length; i += 2) {
                        String username = userList[i];
                        int amount = Integer.parseInt(userList[i + 1]);

                        User user = findUser(username);
                        Object[] newInfo = {stock.getStockName(), amount};
                        Objects.requireNonNull(user).getStockArrayList().add(newInfo);
                    }
                }
            }
        }

        br.close();
        fw.close();
    }

    private void importFunding() throws IOException{
        FileReader fr = new FileReader(fundDataFileName);
        BufferedReader br = new BufferedReader(fr);

        String currentLine;
        while((currentLine = br.readLine()) != null) {
            String[] information = currentLine.split(",");

            String name = information[0];
            String period = information[1];
            String startFunding = information[2];
            String endFunding = information[3];
            int unitPrice = Integer.parseInt(information[4]);
            int amount = Integer.parseInt(information[5]);
            double interestShifter = Double.parseDouble(information[6]);

            Funding funding = new Funding(name, period, endFunding, interestShifter);
            funding.setStartFunding(startFunding);
            funding.setFunds(unitPrice, amount);

            if((currentLine = br.readLine()) != null){
                String[] userList = currentLine.split(",");

                if(userList.length > 1) {
                    for (String username : userList) {
                        User user = findUser(username);

                        funding.addObserver(user);
                        Objects.requireNonNull(user).getFundingArrayList().add(funding);
                    }
                }
            }
        }

        fr.close();
        br.close();
    }

    private void importEMT() throws IOException {
        FileReader fr = new FileReader(emtDateFileName);
        BufferedReader br = new BufferedReader(fr);

        String currentLine;
        int totalEmt = 0;
        if((currentLine = br.readLine()) != null) {
            totalEmt = Integer.parseInt(currentLine);
        }

        while((currentLine = br.readLine()) != null) {
            String[] information = currentLine.split(",");

            String id = information[0];
            Account send = findAllAccount(information[1]);
            Account receiver = findAllAccount(information[2]);
            double amount = Double.parseDouble(information[3]);
            String password = information[4];

            EMT emt = new EMT(send, receiver, amount, password);
            emt.setID(id);

            Bank.getEMTArrayList().add(emt);
        }

        EMT.setNumEMT(totalEmt);

        fr.close();
        br.close();
    }

    private Account findAllAccount(String accountID){
        for(Person person : Bank.getPersonList()){
            if(person instanceof BankStaff){
                person = ((BankStaff) person).getUser();
            }
            if(person instanceof User){
                for(Account account : ((User) person).getAccountList()){
                    if(account.getId().equals(accountID)){
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find a User by its username
     * @param username username in String
     * @return null if this username of User doesn't exist
     */
    private User findUser(String username) {
        for(Person person : Bank.getPersonList()) {
            if(person.getUsername().equals(username)) {
                if(person instanceof User) {
                    return (User) person;
                }
                else if(person instanceof BankStaff) {
                    return ((BankStaff) person).getUser();
                }
            }
        }
        return null;
    }

    /**
     * Importing all data after shutdown
     * @throws IOException file reader
     */
    public void importData() throws IOException{
        importLogin();
        importAccount();
        importCash();
        importTransaction();
        importStock();
        importFunding();
        importEMT();
    }

    /**
     * Save all account information into a file.
     * @throws IOException FileWriter
     */
    private void saveAccount() throws IOException{
        FileWriter fw = new FileWriter(accountDataFileName);
        for(int i = 1; i <= Account.getNumAccounts(); i++) {
            Account account = findAllAccount("00000000".substring(0, 8 - Integer.toString(i).length()) + i);
            if(account == null){
                System.out.println("FileSaving Error!");
                return;
            }

            ArrayList<String> nameList = new ArrayList<>();
            for(Person person : Bank.getPersonList()){
                if(person instanceof BankStaff){
                    person = ((BankStaff) person).getUser();
                }
                if(person instanceof User){
                    if(((User) person).getAccountList().contains(account)){
                        nameList.add(person.getUsername());
                        if(((User) person).getPrimaryAccount().contains(account)){
                            fw.write("1,");
                        }
                        else {
                            fw.write("0,");
                        }
                    }
                }
            }

            fw.write("\n");

            for(String username : nameList){
                fw.write(username + ",");
            }
            fw.write("\n");
            fw.write(String.format("%s,%.2f,%s,%s\n",
                    account.getType(),
                    account.getBalance(),
                    account.getMoneyType(),
                    account.getCreationTime().toString()));
        }
        fw.close();
    }

    /**
     * Save all login information into a file.
     * @throws IOException FileWriter
     */
    private void savePeople() throws IOException{
        FileWriter fw = new FileWriter(loginDataFileName);
        for(Person person : Bank.getPersonList()) {
            fw.write(person.fileOutputFormat() + "\n");
        }
        fw.close();
    }

    /**
     * Save all atm cash information into a file
     * @throws IOException FileWriter
     */
    private void saveCash() throws IOException{
        FileWriter fw = new FileWriter(cashDataFileName);

        for(ATM a : Bank.getAtmArrayList()){
            ATMCash cash = a.getAc();
            for(Currency c : cash.getCurrencyList()){
                fw.write(String.format("%s,%s\n", c.getType(), c.getSymbol()));

                for(int denomination : c.getDenomination()){
                    fw.write(String.format("%d,", denomination));
                }
                fw.write("\n");

                for(int storage : c.getStorage()){
                    fw.write(String.format("%d,", storage));
                }
                fw.write("\n");
            }
            fw.write("*********\n");
        }

        fw.close();
    }

    /**
     * Save all transaction information into a file
     * @throws IOException FileWriter
     */
    private void saveTransaction() throws IOException{
        FileWriter fw = new FileWriter(transactionDataFileName);

        for(Person p : Bank.getPersonList()){
            if(p instanceof BankStaff){
                p = ((BankStaff) p).getUser();
            }

            if(p instanceof User){
                for(Account account : ((User) p).getAccountList()){

                    for(Transaction transaction : account.getTransactionStack()){
                        String from;
                        String to;

                        if(transaction.getMoneyOut() instanceof Account){
                            from = ((Account) transaction.getMoneyOut()).getId();
                        } else {
                            from = ((String) transaction.getMoneyOut());
                        }
                        if(transaction.getMoneyIn() instanceof Account){
                            to = ((Account) transaction.getMoneyIn()).getId();
                        } else {
                            to = ((String) transaction.getMoneyIn());
                        }

                        //storedID, type, fromAccountId, toAccountID, amount, date, undoable
                        fw.write(String.format("%s,%s,%s,%s,%.2f,%s,%d\n",
                                account.getId(),
                                transaction.getType(),
                                from,
                                to,
                                transaction.getAmount(),
                                transaction.getDate().toString(),
                                transaction.isUndoable()?1:0));
                    }

                }
            }
        }

        fw.close();
    }

    /**
     * Save all stock information into a file
     * @throws IOException FileWriter
     */
    private void saveStock() throws IOException {
        FileWriter fw = new FileWriter(stockDataFileName);

        for (Stock stock : Bank.getStockArrayList()) {
            fw.write(String.format("%.2f,%.3f\n", stock.getCurrentPrice(), stock.getRate()));

            for (Person p : Bank.getPersonList()) {
                if (p instanceof BankStaff) {
                    p = ((BankStaff) p).getUser();
                }
                if (p instanceof User) {
                    for (Object[] obj : ((User) p).getStockArrayList()) {
                        if (obj[0].equals(stock.getStockName())){
                            fw.write(String.format("%s,%s", p.getUsername(), obj[1]));
                        }
                    }
                }
            }

            fw.write("\n");
        }


        fw.close();
    }

    /**
     * Save all EMT information into a file
     * @throws IOException FileWriter
     */
    private void saveEMT() throws IOException{
        FileWriter fw = new FileWriter(emtDateFileName);

        fw.write(EMT.getNumEMT() + "\n");

        for (EMT emt: Bank.getEMTArrayList()){
            String id = emt.getEMTid();
            String from = emt.getSender().getId();
            String to = emt.getReceiver().getId();
            double amount = emt.getAmount();
            String password = emt.getPassword();
            fw.write(String.format("%s,%s,%s,%.2f,%s,\n", id, from, to, amount, password));
        }

        fw.close();
    }

    private void saveFunding() throws IOException {
        FileWriter fw = new FileWriter(fundDataFileName);

        for(Funding funding : Bank.getFundingArrayList()){
            //name,projectEnds,startFunding,endFunding,unitPrice,amount,interestShifter
            fw.write(String.format("%s,%s,%s,%s,%d,%d,%.2f\n",
                    funding.getName(),
                    funding.getPeriod(),
                    funding.getStartFunding(),
                    funding.getEndFunding(),
                    funding.getUnitPrice(),
                    funding.getAmount(),
                    funding.getInterestShifter()));

            for(Observer ob : funding.getObs()) {
                fw.write(String.format("%s,", ((User)ob).getUsername()));
            }
            fw.write("\n");
        }

        fw.close();
    }

    /**
     * Save all login information and account information into files
     */
    public void saveData() throws IOException{
        saveAccount();
        savePeople();
        saveCash();
        saveTransaction();
        saveStock();
        saveFunding();
        saveEMT();
    }
}
