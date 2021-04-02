package bank_system.InterestStrategy;

public class RandomBasedInterestCalculator implements StrategyCalculator {

    /**
     * Randomly generate the interest rate trend for the upcoming period.
     * @param current current balance.
     * @param factor manually control the multiplier.
     * @return new period balance.
     */
    @Override
    public double updateValue(double current, double factor){
        double rate = (Math.random() * 0.005) - 0.0025; //generate random value between -0.05% to + 0.05%
        return  Math.round((current * (1 + rate) + factor) * 100) / 100.0; //generate new balance in correct format (2 decimal places)
    }
}
