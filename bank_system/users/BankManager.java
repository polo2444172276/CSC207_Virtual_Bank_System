package bank_system.users;


import bank_system.FinancialProduct.Stock;
import bank_system.*;
import bank_system.accounts.Account;
import bank_system.atm_interface.ManagerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

public class BankManager extends Worker {
    public BankManager(String username, String password) {
        super(username, password);
    }

    /**
     * Create a new currency type in this ATM.
     * @param name The name of this currency.
     * @param notation The notation for this currency.
     */
    public void addCurrency(String name, String notation){
        Currency currency = new Currency(name, notation);
        Bank.currentAC.addCurrency(currency);
    }

    /**
     * Add new denomination for the certain moneyType.
     * @param moneyType The string name for the certain money type.
     * @param denomination The new denomination for the money type.
     * @param value The number of this denomination want to store.
     */
    public void addDenomination(String moneyType, int denomination, int value){
        Bank.currentAC.addDenomination(moneyType, denomination, value);
    }


    /**
     * Check every denomination, make sure each of them is over 20
     * @param moneyType The name of money.
     * @param index The denomination want to restore.
     * @param amount The number of denomination want to restore.
     */
    public void restoreCash(String moneyType, int index, int amount) {
        Bank.currentAC.moneyInOut(moneyType, index, amount);
    }



    /**
    * Undo the nth transaction
    * @param account: the account of this transaction
    * @param n: the nth transaction to be undone
    * precondition: 0 <= n <= account.transactionStack.size()
     */
    public void undoSpecificTransaction(Account account, int n){
        Stack<Transaction> s = account.getTransactionStack();
        int depth = 0;
        Stack<Transaction> temp = new Stack<>();
        while (!s.isEmpty()) {
            if (depth == n) {
                Transaction t = s.peek().Reverse();
                if (t != null) {
                    t.execute();
                    s.pop();
                    break;
                }
                else {
                    System.out.println("Transaction cannot be undo!");
                }
            }
            temp.push(s.pop());
            depth += 1;
        }
        while (!temp.isEmpty()) {s.push(temp.pop());}
    }


    /**
     * The UI for manager
     * @param br BufferedReader
     * @throws IOException handle in out exception
     */

    public void userInterface(BufferedReader br) throws IOException {
        ManagerInterface UI = new ManagerInterface(this);
        String inputLine = null;
        do{
            if(inputLine != null){
                switch (inputLine){
                    case "1":
                        UI.createNewUserUI();
                        break;
                    case "2":
                        UI.createNewAccountUI();
                        break;
                    case "3":
                        UI.createJointAccountUI();
                        break;
                    case "4":
                        UI.addMoneyUI();
                        break;
                    case "5":
                        UI.undoTransactionUI();
                        break;
                    case "6":
                        UI.checkAtmCash();
                        break;
                    case "7":
                        UI.changePasswordUI();
                        break;
                    case "8":
                        UI.addFundingUI(br);
                        break;
                    case "9":
                        UI.releaseStockUI();
                        break;
                    default:
                        System.out.println("INVALID INPUT.");
                        break;
                }
            }
            System.out.println("\nMain menu:");
            System.out.println("Type 1 to create a new user.");
            System.out.println("Type 2 to create a new account.");
            System.out.println("Type 3 to create a joint account for two users.");
            System.out.println("Type 4 to interact with ATM cash.");
            System.out.println("Type 5 to undo transaction");
            System.out.println("Type 6 to check cash in ATM");
            System.out.println("Type 7 to change password");
            System.out.println("Type 8 to start a new funding project.");
            System.out.println("Type 9 to start a new stock.");
            System.out.println("Type LOGOUT to exit.");
        } while(!(inputLine = br.readLine().toLowerCase()).equals("logout"));
        System.out.println("Thank you! See you next time!");
    }

    /**
     * Add a new stock to the bank system.
     * @param price The initial price for the stock.
     * @param rate The amount of percentage, the manager want control.
     */
    public void addNewStock(double price, double rate){
        Stock newStock = new Stock(price, rate);
        Bank.addNewStock(newStock);
    }

    @Override
    public String fileOutputFormat(){
        return String.format("manager,%s,%s", getUsername(), getPassword());
    }
}