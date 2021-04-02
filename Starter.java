import bank_system.Bank;

/**
 * A class that starts the whole program
 */
public class Starter {
    public static void main(String[] args){
        Bank bank = new Bank();
        try {
            bank.start();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }
}
