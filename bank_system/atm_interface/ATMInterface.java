package bank_system.atm_interface;

import bank_system.Bank;
import bank_system.users.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ATMInterface extends Interface {
    /**
     * Check whether the input username and password match a certain bUser.
     * If there is the User, return the User. Otherwise, return null.
     * @param entry The input username and password.
     * @param personList The personList stored in the ATM.
     * @return The person who match both username and password.
     */
    private Person login(String entry, ArrayList<Person> personList){
        if(!Bank.isOpen) {
            System.out.println("Bank is now close, It will be open within 10 minute!");
            return null;
        }
        entry = entry.toLowerCase();
        Person result = null;
        String[] temp = entry.split("-");
        if (temp.length != 2){
            return null;
        }
        String name = temp[0];
        String password = temp[1];
        for (Person p: personList){
            if (p.getUsername().equals(name)){
                if (p.getPassword().equals(password)){
                    result = p;
                }
            }
        }
        return result;
    }

    /**
     * The starting interface which would be used to log the person into his own account.
     * @param personList The list collection all person in the bank system.
     * @throws IOException Exception for reading file.
     */
    public void start(ArrayList<Person> personList) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        do{
            if(inputLine != null){
                if(inputLine.equals("EXIT")){
                    return;
                }
                Person p = login(inputLine, personList);
                if(p == null) {
                    System.out.println("Invalid input");
                } else {
                    System.out.println("Welcome!");
                    p.userInterface(br);
                }
            }
            System.out.println("Type EXIT to shutdown.");
            System.out.print("Please enter username and password in the form of bracket exclusive." +
                    "\n(username-password): ");

        } while((inputLine = br.readLine()) != null);
    }
}
