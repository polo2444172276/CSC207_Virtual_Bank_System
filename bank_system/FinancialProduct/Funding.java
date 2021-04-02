package bank_system.FinancialProduct;

import bank_system.InterestStrategy.AmountBasedInterestCalculator;
import bank_system.InterestStrategy.RandomBasedInterestCalculator;
import bank_system.ATM;
import bank_system.Bank;
import bank_system.Transaction;
import bank_system.accounts.Account;
import bank_system.users.User;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Financing Product CrowdFunding.
 * <p>
 * User can join one funding at specific time and interest period starts right after endFunding time.
 * Costs for join one funding is fixed when the funding was created, users can join one funding multiple times
 * as long as there is still remaining.
 * <p>
 * Our bank is encouraging users to join 5000CAD-lowRisk-highEarning funding, so default is set to that.
 */
public class Funding extends Observable {

    private final Vector<Observer> obs = new Vector<>();

    /**
     * Name of the funding.
     */
    private final String name;

    /**
     * Last day of the interest period, money will return back to user's primary account
     */
    private final String projectEnds;

    /**
     * Users can purchase after this.
     */
    private String startFunding;

    /**
     * Users can purchase before this.
     */
    private final String endFunding;

    /**
     * Price for purchasing each unit, default is 5000 CAD.
     */
    private int unitPrice = 5000;

    /**
     * Total amount of units, default is 500.
     */
    private int amount = 500;

    /**
     * Generally interest rate is randomly calculated by RandomBasedInterestCalculator, but managers have the ability to shift
     * the interest to a higher or lower rate.
     */
    private final double interestShifter;

    private final String period;

    /**
     * Constructor for a new Funding.
     * @param name        name of the funding.
     * @param period     last of the funding project, money returned, in the format of "YYYY-MM-DD"
     * @param fundingEnds date for end funding period adn start interest period, in the format of "YYYY-MM-DD"
     * @param shifter     shift interest rate.
     */
    public Funding(String name, String period, String fundingEnds, double shifter) {
        this.name = name;
        this.startFunding = ATM.getTime().toString();
        this.endFunding = fundingEnds;
        this.interestShifter = shifter;
        this.projectEnds = convertPeriodToDate(fundingEnds, period);
        this.period = period;
    }

    /**
     * Convert period to date to check whether a fund ends or not.
     * @param start starting date
     * @param duration period in month
     * @return end date.
     */
    private String convertPeriodToDate(String start, String duration){
        //period in terms of year, month
        int year = Integer.valueOf(duration) / 12;
        int month = Integer.valueOf(duration) % 12;

        //add period to starting date to compute end date.
        String[] date = start.split("-") ;
        date[0] = String.valueOf(Integer.valueOf(date[0]) + year);
        date[1] = String.valueOf(Integer.valueOf(date[1] + month));
        return date[0] + "-" + date[1] + "-" + date[2];
    }

    /**
     * Get period of the fund project
     * @return period in terms of month in string.
     */
    public String getPeriod() {
        return period;
    }

    public void setStartFunding(String startFunding) {
        this.startFunding = startFunding;
    }

    /**
     * Change unit price and amount to non-default value in minor situations, since bank is promoting 5000CAD-lowRisk-highEarning fund.
     *
     * @param desiredPrice  change unit price if needed before anyone purchases
     * @param desiredAmount change total amount of units if need before anyone purchases.
     */
    public void setFunds(int desiredPrice, int desiredAmount) {
        this.unitPrice = desiredPrice;
        this.amount = desiredAmount;
    }

    public String getStartFunding() {
        return startFunding;
    }

    public Vector<Observer> getObs() {
        return obs;
    }

    /**
     * Get starting date of interest period, ending date of funding period.
     * @return String date.
     */
    public String getEndFunding() {
        return endFunding;
    }

    /**
     * Get money returns date.
     * @return date which this project ends.
     */
    public String getProjectEnds() {
        return projectEnds;
    }

    /**
     * Get name of the funding.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Get unit price of this funding project.
     * @return double unit price.
     */
    public int getUnitPrice() {
        return unitPrice;
    }

    /**
     * Get the potential interest rate factor.
     * @return potential interest rate.
     */
    public double getInterestShifter(){
        return interestShifter;
    }

    /**
     * Return number of units
     * @return int number of units.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Override equals method for class Funding.
     *
     * @param obj another obj that wish to compare.
     * @return true if they are the same type and identical in all aspects.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Funding)) {
            return false;
        } else {
            return this.amount == ((Funding) obj).amount && this.endFunding.equals(((Funding) obj).endFunding) &&
                    this.startFunding.equals(((Funding) obj).startFunding) && this.projectEnds.equals(((Funding) obj).projectEnds) &&
                    this.name.equals(((Funding) obj).name) && this.unitPrice == ((Funding) obj).unitPrice;
        }
    }


    /**
     * Overrides addObserver method in Observable so that one observer can add multiple times to the list.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        obs.addElement(o);
    }

    /**
     * Join the funding.
     *
     * @param a   account to deduce the money
     * @param u   user who joins the funding
     * @param num num of units this user desires to purchase.
     */
    public void joinFund(Account a, User u, int num) {
        if (obs.size() + num <= amount) {
            if ((!(a.accessOut() && a.getBalance() >= num * unitPrice)) || num == 0) {
                System.out.println("!Invalid Selection!");
                System.out.println("Potentially Caused by: Insufficient Funds, invalid units");
            } else {
                for (int i = 0; i < num; i++) {
                    addObserver(u);
                    u.addToFundingList(this); //add this funding to user's funding list num times
                } //register user for n times according to num of units that user purchased.
                Transaction t = new Transaction("Join Funding Project", a, "Bank.Fund", num * unitPrice);
                t.setToCantUndo();
                t.execute();//deduce money
            }
        } else{
            System.out.println("Sorry, maximum capacity of this project reached!"); //unable to join this found since maximum capacity reached
        }
    }

    /**
     * This crowd funding project completed, money returns to user's account.
     */
    public void projectFinished() {
        RandomBasedInterestCalculator randomBasedInterestCalculator = new RandomBasedInterestCalculator();
        AmountBasedInterestCalculator amountBasedInterestCalculator = new AmountBasedInterestCalculator();
        //complex method to calculate interest of funding project, combine random based and amount based calculators.

        //notify all subscribers to receive money
        setChanged();
        notifyObservers(amountBasedInterestCalculator.updateValue(randomBasedInterestCalculator.updateValue(unitPrice, interestShifter), interestShifter));
        clearChanged();
        //delete this fund after finishes.
        Bank.removeFinishedFunding(this); //delete finished funding.

    }
}
