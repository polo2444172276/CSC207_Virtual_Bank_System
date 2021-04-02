package bank_system.InterestStrategy;

/**
 * Interface: method below is for calculating interest periodically according to situation.
 */
public interface StrategyCalculator {

    /**
     * Interest calculate method.
     * @param curr current value.
     * @param fac manually control the factor that may lead to a outstanding high/low interest rate.
     * @return new value.
     */
    double updateValue(double curr, double fac);
}
