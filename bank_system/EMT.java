package bank_system;

import bank_system.accounts.Account;

/**
 * A class to store all information for a certain EMT.
 */
public class EMT {

    /**
     * Number nth account created.
     */
    private static int numEMT = 0;
    /**
     * The sender for this EMT
     */
    private final Account sender;
    /**
     * The receiver for this EMT
     */
    private final Account receiver;
    /**
     * The amount of money of this EMT.
     */
    private final double amount;
    /**
     * The password for the receiver need to answer to get the EMT.
     */
    private final String password;
    /**
     * The id for this EMT.
     */
    private String EMTid;

    /**
     * The constructor for the EMT.
     * @param send The information about sender.
     * @param receive The information about receiver.
     * @param money The amount of money about this EMT.
     * @param key The password for this EMT.
     */
    public EMT(Account send, Account receive, double money, String key){
        numEMT ++;
        this.sender = send;
        this.receiver = receive;
        this.amount = money;
        this.password = key;
        this.EMTid = setId();
    }

    public static int getNumEMT() {
        return numEMT;
    }

    public static void setNumEMT(int numEMT) {
        EMT.numEMT = numEMT;
    }

    /**
     * @return return the sender for the EMT
     */
    public Account getSender(){
        return sender;
    }

    /**
     * @return return the receiver fot the EMT
     */
    public Account getReceiver(){
        return receiver;
    }

    /**
     * @return return the password for the EMT
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return return the amount of money in this EMT
     */
    public double getAmount(){
        return amount;
    }

    /**
     * Generate the id of current account.
     * @return String id.
     */
    private static String setId() {
        int length = Integer.toString(numEMT).length();
        return "00000000".substring(0, 8 - length) + numEMT;
    }

    public void setID(String id) {
        this.EMTid = id;
    }

    /**
     * @return The string for the EMT's id.
     */
    public String getEMTid(){
        return EMTid;
    }
}
