package bank_system;

import bank_system.atm_interface.ATMInterface;
import bank_system.users.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * The ATM machine itself.
 */
public class ATM extends Observable {

    /**
     * A timer that track the current time.
     */
    private static final Timer timer = new Timer("SystemClock");

    /**
     * Return the current time of ATM.
     * @return current time in MyDate
     */
    public static MyDate getTime(){
        return timer.getTime();
    }

    /**
     * The "cash holder" of this ATM
     */
    private final ATMCash ac;

    /**
     * Getter method for the ac of this ATM
     * @return ATMCash
     */
    public ATMCash getAc() {
        return ac;
    }

    public ATM(ArrayList<Currency> currency){
        ac = new ATMCash();
        for(Currency c : currency) {
            ac.addCurrency(c);
        }
        timer.start();
    }

    /**
     * Starter of this ATM
     * @param personList User's data
     */
    public void start(ArrayList<Person> personList){
        try{
            ATMInterface atmUI = new ATMInterface();
            atmUI.start(personList);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * Save all data into a file.
//     * Called every mid night
//     * @throws InterruptedException Thread
//     */
//    static void restart() throws InterruptedException{
//        System.out.println("Restarting");
//        saveData();
//        Thread.sleep(1000);
//        System.out.println("DONE!");
//    }

//    public static void main(String[] args) {
//        importData();
//        timer.start();
//
//        try{
//            MainInterface mainUI = new MainInterface();
//            mainUI.start(personList);
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        saveData(); //save all data when ATM is shut down.
//        System.exit(0);
//    }
}