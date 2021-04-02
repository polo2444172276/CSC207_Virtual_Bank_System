package bank_system.atm_interface;

import bank_system.*;
import bank_system.FinancialProduct.Funding;
import bank_system.accounts.Account;
import bank_system.users.BankManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManagerInterface extends WorkerInterface {
    private final BankManager self;
    public ManagerInterface(BankManager manager){
        super(manager);
        this.self = manager;
    }


    /**
     * @return The list of currencies' name in this ATM.
     */
    private ArrayList<String> listHelper(){
        ArrayList<String> lst = new ArrayList<>();
        for(Currency c: Bank.currentAC.getCurrencyList()){
            lst.add(c.getType());
        }
        return lst;
    }

    /**
     * Interface for manager to interact with cash in atm.
     * @throws IOException Console input
     */
    public void addMoneyUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("exit")){
                    return;
                }
                System.out.println("INVALID INPUT!");
            }

            System.out.println("Type 1 to add cash.");
            System.out.println("Type 2 to add a domination");
            System.out.println("Type 3 to add a new currency.");
            System.out.println("Type EXIT back to main.");
        } while(!isNumber(inputLine = br.readLine()) ||
                !(Integer.parseInt(inputLine) >= 1 && Integer.parseInt(inputLine) <= 3));

        switch (inputLine){
            case "1":
                addStorageUI();
                break;
            case "2":
                addDenominationUI();
                break;
            case "3":
                addNewCurrencyUI();
                break;
        }
    }

    /**
     * Interface for manager to add a new type of currency in this atm
     * @throws IOException handle in out exception
     */
    private void addNewCurrencyUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Currency canceled!");
                    return;
                }
                System.out.println("Currency already exist. Please enter another currency name ");
            }
            System.out.println("Please enter a currency name. Type CANCEL to cancel: ");

        } while(isNumber(inputLine = br.readLine()) ||
                listHelper().contains(inputLine));
        String name = inputLine;

        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Currency canceled!");
                    return;
                }
                System.out.println("Please enter a non-integer symbol ");
            }
            System.out.println("Please enter a non-integer symbol. Type CANCEL to cancel: ");
        } while(isNumber(inputLine = br.readLine()));
        String symbol = inputLine;
        self.addCurrency(name, symbol);
        System.out.println("Done!");
    }

    /**
     * Interface for manager to add a new type of denomination in this atm
     * @throws IOException console input
     */
    private void addDenominationUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        //Select a certain money type.
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add denomination canceled!");
                    return;
                }
                System.out.println("Please only enter number from 1 to " + Bank.currentAC.getCurrencyList().size());
            }

            for (int i = 0; i < Bank.currentAC.getCurrencyList().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to add  " + Bank.currentAC.getCurrencyList().get(i).getType());
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                Bank.currentAC.getCurrencyList().size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));

        int index_type  = Integer.parseInt(inputLine) - 1;
        Currency currency = Bank.currentAC.getCurrencyList().get(index_type);
        //Enter the new denomination.
        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add denomination canceled!");
                    return;
                }
                System.out.println("Denomination already exist. Please enter correct denomination value ");
            }
            System.out.println("Please enter denomination value. Type CANCEL to cancel: ");

        } while(!(isNumber(inputLine = br.readLine()) &&
                !currency.getDenomination().contains(Integer.getInteger(inputLine))));

        //New denomination does not exist for the currency in this ATM
        int denominaion = Integer.parseInt(inputLine);
        //Enter the amount of denomination want to store.
        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add denomination canceled!");
                    return;
                }
                System.out.println("Please only enter number");
            }

            System.out.println("Please enter the number of this denomination. Type CANCEL to cancel: ");
        } while(!isNumber(inputLine = br.readLine()));
        int amount = Integer.parseInt(inputLine);

        self.addDenomination(currency.getType(), denominaion, amount);
        System.out.println("Done!");
    }

    /**
     * Interface for manager to add cash
     * @throws IOException console input
     */
    private void addStorageUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Deposit canceled!");
                    return;
                }
                System.out.println("Please only enter number from 1 to " + Bank.currentAC.getCurrencyList().size());
            }

            for (int i = 0; i < Bank.currentAC.getCurrencyList().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to add  " + Bank.currentAC.getCurrencyList().get(i).getType());
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                Bank.currentAC.getCurrencyList().size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));

        int index_type  = Integer.parseInt(inputLine) - 1;

        if(Bank.currentAC.getCurrencyList().get(index_type).getDenomination().size() == 0){
            System.out.println("There is no denomination for this currency. Please add denomination first.");
            return;
        }

        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Deposit canceled!");
                    return;
                }
                System.out.println("Please only enter number from 1 to " +
                        Bank.currentAC.getCurrencyList().get(index_type).getDenomination().size());
            }

            for (int i = 0; i < Bank.currentAC.getCurrencyList().get(index_type).getDenomination().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to add denomination " +
                        Bank.currentAC.getCurrencyList().get(index_type).getSymbol() +
                        Bank.currentAC.getCurrencyList().get(index_type).getDenomination().get(i));
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                Bank.currentAC.getCurrencyList().get(index_type).getDenomination().size() >=
                        Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));

        int index_deno = Integer.parseInt(inputLine) - 1;
        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Deposit canceled!");
                    return;
                }
                System.out.println("Please only enter number");
            }

            System.out.println("Please enter the number of cashes. Type CANCEL to cancel: ");
        } while(!isNumber(inputLine = br.readLine()));

        int amount = Integer.parseInt(inputLine);

        self.restoreCash(Bank.currentAC.getCurrencyList().get(index_type).getType(), index_deno, amount);

        Bank.alertManager();

        System.out.println("Done!");
    }



    /**
     * An interface method for manager to check remaining cash in ATM
     */
    public void checkAtmCash() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Canceled!");
                    return;
                }
                System.out.println("Please only enter number from 1 to " + Bank.currentAC.getCurrencyList().size());
            }

            for (int i = 0; i < Bank.currentAC.getCurrencyList().size(); i++){
                int index = i + 1;
                System.out.println("Type " + index + " to view  " + Bank.currentAC.getCurrencyList().get(i).getType());
            }
            System.out.println("Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine()) &&
                Bank.currentAC.getCurrencyList().size() >= Integer.parseInt(inputLine) &&
                Integer.parseInt(inputLine) >= 1));
        int index_type  = Integer.parseInt(inputLine) - 1;

        System.out.println("Here's the remaining amount of cash in ATM");
        System.out.println(Bank.currentAC.getCurrencyList().get(index_type).getType() + ": ");
        for(int i = 0; i < Bank.currentAC.getCurrencyList().get(index_type).getDenomination().size(); i++){
            System.out.printf("$%d: %d left.  \n",
                    Bank.currentAC.getCurrencyList().get(index_type).getDenomination().get(i),
                    Bank.currentAC.getCurrencyList().get(index_type).getStorage().get(i));
        }
        System.out.println();
    }


    /**
     * Interface for manager to undo the previous n-th transaction of a specific account.
     * @throws IOException Console input
     */
    public void undoTransactionUI() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        System.out.println("Please enter the account ID that you wish to undo.");
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
            if(findAllAccount(inputLine).getTransactionStack().isEmpty()) {
                System.out.println("There is no transaction on this account");
            }
            else {
                System.out.println("Account cannot be found!");
            }
            System.out.println("Please re-enter the account ID that you wish to undo.");
            System.out.println("Type CANCEL to cancel:");
        }
        System.out.println("Please enter which transaction of the following to undo.");
        System.out.println(PrintTransHelper(account));
        System.out.println("Type CANCEL to cancel:");
        while (!isNumber(inputLine = br.readLine()) || Integer.parseInt(inputLine) < 0 ||
                Integer.parseInt(inputLine) >= account.getTransactionStack().size()) {
            if(inputLine.toLowerCase().equals("cancel")){
                System.out.println("Canceled!");
                return;
            }
            if(!isNumber(inputLine)){
                System.out.println("Please only enter numbers!");
            }
            else {
                System.out.println("Please enter natural number smaller than your number of transactions on this account");
            }
        }
        self.undoSpecificTransaction(account,Integer.parseInt(inputLine));
        System.out.println("Done!");
    }


    /**
     * Adding new funds UI for both quick funds and customized funds.
     * @param br BufferedReader
     * @throws IOException Console input
     */
    public void addFundingUI(BufferedReader br) throws IOException {
        String inputLine = null;
        do{
            if(inputLine != null){
                switch (inputLine){
                    case "1":
                        this.addFundingUI("default");
                        return;
                    case "2":
                        this.addFundingUI("customized");
                        return;
                    default:
                        System.out.println("INVALID INPUT.");
                        break;
                }
            }
            System.out.println("Type 1 to start a new quick funding project with unit price pre-set to 5000CAD and total of 500 units.");
            System.out.println("Type 2 to start a new customized funding project.");
            System.out.println("Type Cancel to exit.");
        } while(!(inputLine = br.readLine().toLowerCase()).equals("cancel"));
    }

    /**
     * Initiate a new quick funding project with unit price = 5000$ CAD and 500 total units..
     * @throws IOException Console input
     */
    private void addFundingUI(String s) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Funding canceled!");
                    return;
                }
            }
            System.out.println("Please enter a name for the new funding, type CANCEL to cancel.");
        } while(!(isAlphanumeric(inputLine = br.readLine()) & !inputLine.equalsIgnoreCase("cancel")));
        String name = inputLine;

        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Funding canceled!");
                    return;
                }
                System.out.println( "Please enter correct desired date follows format: YYYY-MM-DD (Entered date should later than current date)");
            }
            System.out.println("Please enter desired end date for funding period (aka. start date for interest period) follows format: YYYY-MM-DD, type CANCEL to cancel: ");
            System.out.println("Funding will start immediately.");
        } while(!(isDate(inputLine = br.readLine()) && !ATM.getTime().isEarlier(new MyDate(inputLine+" 23:59:59"))));
        String funEnds = inputLine;

        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Funding canceled!");
                    return;
                }
                System.out.println("Please enter correct desired duration for the interest period (only integer number represents number months)");
            }
            System.out.println("Please enter desired duration (in terms of months) for the interest period. Type CANCEL to cancel: ");
        } while(!(isNumber(inputLine = br.readLine())));
        String duration = inputLine;

        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Funding canceled!");
                    return;
                }
                System.out.println("Please enter correct shifter");
            }
            System.out.println("Please enter interest rate shifter. Type CANCEL to cancel: ");
            System.out.println("Positive shifter can shift interest rate right along x-axis, vice versa.");
            System.out.println("!Friendly Reminder! Usually shifter is between 0.0015 to 0.02 to avoid potentially Bankrupt.");
        } while(!(isDouble(inputLine = br.readLine())) && !isNumber(inputLine));
        String factor = inputLine;

        Funding funding = new Funding(name, duration, funEnds, Double.valueOf(factor));

        if (s.equalsIgnoreCase("customized")){
            inputLine = null;
            do{
                if(inputLine != null){
                    if(inputLine.toLowerCase().equals("cancel")){
                        System.out.println("Add Funding canceled!");
                        return;
                    }
                    System.out.println("Please enter correct amount");
                }
                System.out.println("Please enter amount of units wish to create. Type CANCEL to cancel: ");
            } while(!(isNumber(inputLine = br.readLine())));
            String amount = inputLine;

            inputLine = null;
            do{
                if(inputLine != null){
                    if(inputLine.toLowerCase().equals("cancel")){
                        System.out.println("Add Funding canceled!");
                        return;
                    }
                    System.out.println("Please enter correct unit price");
                }
                System.out.println("Please enter desired unit price. Type CANCEL to cancel: ");
            } while(!(isNumber(inputLine = br.readLine())));
            String price = inputLine;
            funding.setFunds(Integer.valueOf(price), Integer.valueOf(amount));
        }
        Bank.addNewFunding(funding);

        System.out.println(String.format("Funding Project Created, Starting Date: %s, End in %s months. ", funding.getEndFunding(), funding.getProjectEnds()));
        System.out.println(String.format("Total funding goal is %s CAD", funding.getUnitPrice() * funding.getAmount()));
    }

    /**
     * The interface which can be used when manager need to add a new stock.
     * @throws IOException console input
     */
    public void releaseStockUI() throws IOException{
        final double minRate = -0.002;
        final double maxRate = 0.001;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Stock canceled!");
                    return;
                }
                System.out.println("Please enter correct unit price");
            }
            System.out.println("Please enter desired positive unit price for the new stock. \nType CANCEL to cancel: ");
        } while(!isDouble(inputLine = br.readLine()) || Double.parseDouble(inputLine) <= 0);
        double price = Double.parseDouble(inputLine);

        inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.toLowerCase().equals("cancel")){
                    System.out.println("Add Stock canceled!");
                    return;
                }
                System.out.printf("Please enter correct interest rate controller. \nFrom %.3f to %.3f.", minRate, maxRate);
            }
            System.out.printf("Please enter desired interest rate controller for this stock." +
                    "\nFrom %.3f to %.3f. \nType CANCEL to cancel: ", minRate, maxRate);
        } while(!((isDouble(inputLine = br.readLine()) || isNumber(inputLine)) && ((Double.valueOf(inputLine) <= maxRate) &&
                (Double.valueOf(inputLine) >= minRate))));
        double rate = Double.parseDouble(inputLine);

        self.addNewStock(price, rate);
        System.out.println("Done!");
    }


    /**
     * Interface for manager to change password
     * @throws IOException console input
     */
    public void changePasswordUI() throws IOException{
        super.changePasswordUI(self);
    }
}
