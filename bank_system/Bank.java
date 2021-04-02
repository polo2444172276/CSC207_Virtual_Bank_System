package bank_system;

import bank_system.FinancialProduct.Funding;
import bank_system.FinancialProduct.Stock;
import bank_system.InterestStrategy.GivenInterestCalculator;
import bank_system.InterestStrategy.AmountBasedInterestCalculator;
import bank_system.InterestStrategy.RandomBasedInterestCalculator;
import bank_system.accounts.Account;
import bank_system.accounts.AdaptiveInterestSavingAccount;
import bank_system.accounts.SavingAccount;
import bank_system.atm_interface.BankInterface;
import bank_system.users.Person;
import bank_system.users.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Bank {

    /**
     * An ArrayList of users information.
     */
    private static final ArrayList<Person> personList = new ArrayList<>();

    /**
     * Return ArrayList of person.
     * @return person list.
     */
    public static ArrayList<Person> getPersonList() {
        return personList;
    }

    public static ATMCash currentAC;

    private static final ArrayList<ATM> atmArrayList = new ArrayList<>();

    public static ArrayList<ATM> getAtmArrayList() {
        return atmArrayList;
    }

    /**
     * Check the remaining cash of all ATM
     * @throws IOException file output
     */
    public static void alertManager() throws IOException{
        String fileName = "phase2/manager/alert.txt";
        FileWriter fw = new FileWriter(fileName);
        fw.close();
        for(int i = 0; i < atmArrayList.size(); i++){
            ATMCash tempAC = atmArrayList.get(i).getAc();
            for(Currency c : tempAC.getCurrencyList()){
                c.alertManager(i + 1, fileName);
            }
        }
    }

    /**
     * Stores all stocks that users can purchase through this bank.
     */
    private static final ArrayList<Stock> stockArrayList = new ArrayList<>();

    /**
     * Stores all funding that users can purchase through this bank.
     */
    private static final ArrayList<Funding> fundingArrayList = new ArrayList<>();

    /**
     * Store all EMTs which still doesn't be accepted.
     */
    private static final ArrayList<EMT> emtArrayList = new ArrayList<>();

    /**
     * If it's opening time.
     */
    public static boolean isOpen = true;

    /**
     * Update interest for all SavingAccount
     */
    static void updateInterest(){
        for(Person person : personList) {
            if(person instanceof User) {
                for(Account account : ((User)person).getAccountList()) {
                    if(account instanceof SavingAccount){
                        ((SavingAccount)account).update(new GivenInterestCalculator());
                    } else if (account instanceof AdaptiveInterestSavingAccount){
                        ((AdaptiveInterestSavingAccount)account).update(new AmountBasedInterestCalculator());
                    }
                }
            }
        }
    }

    /**
     * Check whether a funding project finished or not.
     */
    static void checkFunding(){
        for(Funding f: fundingArrayList){
            if(ATM.getTime().toString().contains(f.getProjectEnds())){
                fundingArrayList.remove(f);
                f.projectFinished();
            }
        }
    }

    /**
     * Update stock price.
     */
    static void checkStock(){
        RandomBasedInterestCalculator randomBasedInterestCalculator = new RandomBasedInterestCalculator();
        for(Stock s: stockArrayList){
            s.updatAlgorithm(randomBasedInterestCalculator);
        }
    }

    public void start(){
        try{
            //import all data before start
            FileLoader fileLoader = new FileLoader();
            fileLoader.importData();

            BankInterface bankUI = new BankInterface();
            bankUI.start(personList, atmArrayList);

            //save all data when exiting
            fileLoader.saveData();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The list of Stocks in the bank
     */
    public static ArrayList<Stock> getStockArrayList(){
        return stockArrayList;
    }

    /**
     * Start a new Stock
     * @param s The brand new stock
     */
    public static void addNewStock(Stock s){
        stockArrayList.add(s);
    }

    /**
     * Start a new funding project
     * @param f new funding project.
     */
    public static void addNewFunding(Funding f){
        fundingArrayList.add(f);
    }

    /**
     * Remove funding when project finished.
     * @param f funding wish to remove
     */
    public static void removeFinishedFunding(Funding f){
        fundingArrayList.remove(f);
    }

    /**
     * Get list of funding projects that this bank currently holds.
     * @return ArrayList of funding.
     */
    public static ArrayList<Funding> getFundingArrayList() {
        return fundingArrayList;
    }

    /**
     * Add a new EMT to the bank
     * @param emt new EMT
     */
    public static void addNewEMT(EMT emt){
        emtArrayList.add(emt);
    }

    /**
     * Remove the EMT from the bank
     * @param emt the target EMT
     */
    public static void removeEMT(EMT emt){
        emtArrayList.remove(emt);
    }

    /**
     * Get list of funding projects that this bank currently holds.
     * @return ArrayList of funding.
     */
    public static ArrayList<EMT> getEMTArrayList() {
        return emtArrayList;
    }
}
