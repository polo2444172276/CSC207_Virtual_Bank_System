package bank_system.accounts;

import bank_system.InterestStrategy.StrategyCalculator;
import bank_system.ATM;
import bank_system.MyDate;

/**
 * Special kind of saving account with relatively higher interest rate.
 */
public class AdaptiveInterestSavingAccount extends AssetAccount {

    private String[] lastTransferOutTime;

    public String getType() {
        return "AdaptiveSaving";
    }

    /**
     * Constructor for AdaptiveInterestSavingAccount.
     * @param date date to set up this account
     * @param currency type of currency this account stores
     */
    public AdaptiveInterestSavingAccount(MyDate date, String currency) {
        super(date, currency);
    }

    /**
     * Send money into bank_system.ATM.
     */
    public void transferOut(double amount) {
        super.transferOut(amount);
        lastTransferOutTime = ATM.getTime().toString().split(":");
    }

    /**
     * Update balance periodically, interest rate increases as time goes by since last withdraw date.
     */
    public void update(StrategyCalculator s) {
        String[] currTime = ATM.getTime().toString().split("-");
        //PeriodBasedInterestCalculator, only suitable for AdaptiveInterestSavingAccount since it needs to know last transfer out date.
        int period = 0;
        if (Integer.valueOf(currTime[2].split(" ")[0]) >= Integer.valueOf(lastTransferOutTime[2].split(" ")[0])){
            if (Integer.valueOf(currTime[0]) > Integer.valueOf(lastTransferOutTime[0])){
            period += (Integer.valueOf(currTime[0]) - Integer.valueOf(lastTransferOutTime[0])) * 12 + Integer.valueOf(currTime[1]);} //more than one year and current Day >= lastWithdraw Day
            else{period +=  Integer.valueOf(currTime[1]) - Integer.valueOf(lastTransferOutTime[1]); }} //less than one year and current Day >= lastWithdraw Day
        else{if (Integer.valueOf(currTime[0]) > Integer.valueOf(lastTransferOutTime[0])){ //if current Day < lastWithdraw Day, number of period -1 (last month not counted)
            period += (Integer.valueOf(currTime[0]) - Integer.valueOf(lastTransferOutTime[0])) * 12 + Integer.valueOf(currTime[1]) - 1;} //more than one year and current Day < lastWithdraw Day
        else{period +=  Integer.valueOf(currTime[1]) - Integer.valueOf(lastTransferOutTime[1]) - 1; }} //less than one year and current Day < lastWithdraw Day


        balance = s.updateValue(balance, Math.min(period * 0.001, 0.02)); //interest increase 0.001 per period, maximum 0.02
    }
}
