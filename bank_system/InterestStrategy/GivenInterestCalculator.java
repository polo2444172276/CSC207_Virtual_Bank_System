package bank_system.InterestStrategy;

public class GivenInterestCalculator implements StrategyCalculator {

    /**
     * Calculate new balance based on given interest rate for the upcoming period.
     * @return new period account balance.
     */
    @Override
    public double updateValue(double current, double factor){
        return Math.round(current * (1 + factor) * 100) / 100.0; //take 2 decimal places
    }
}
