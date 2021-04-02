package bank_system.InterestStrategy;

public class AmountBasedInterestCalculator implements StrategyCalculator {

    /**
     * Calculate new balance based on amount of balance..
     * @param factor represent number of month that money since last withdraw, controlled in account.
     * @return new period balance.
     */
    @Override
    public double updateValue(double current, double factor){
        double interest;

        //rate according to balance
        if (current <2000){
            interest = (1.005);
        } else if (2000 <= current & current < 4000){
            interest = (1.01);
        } else if (4000 <= current & current < 8000){
            interest = (1.02);
        } else if (8000 <= current & current < 15000){
            interest = (1.03);
        } else if (15000 <= current & current < 50000){
            interest = (1.04);
        } else{
            interest = (1.05);
        }
        return Math.round(current* (interest + factor) * 100) / 100.0; //interest rate get higher if money stays longer in this account.
        //2 decimal places
    }
}
