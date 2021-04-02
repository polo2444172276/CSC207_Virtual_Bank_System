package bank_system.atm_interface;

import bank_system.ATM;
import bank_system.Bank;
import bank_system.users.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BankInterface extends Interface {
    public void start(ArrayList<Person> personList, ArrayList<ATM> atmList) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.printf("Please select ATM from 1 to %d.\n", atmList.size());

        String inputLine;
        while(!(inputLine = br.readLine()).equals("EXIT")){
            if(isNumber(inputLine)){
                int num = Integer.parseInt(inputLine)-1;
                if(num < atmList.size() && num >= 0){
                    Bank.currentAC = atmList.get(num).getAc();
                    atmList.get(num).start(personList);
                }
            }
            System.out.printf("Please select ATM from 1 to %d.\n", atmList.size());
        }
    }
}
