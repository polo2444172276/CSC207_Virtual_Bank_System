package bank_system.FinancialProduct;

import bank_system.InterestStrategy.StrategyCalculator;

public class Stock {
    /**
     * Number nth Stock created.
     */
    private static int numStock = 0;
    /**
     * The name for the stock.
     */
    private String stockId;
    /**
     * The current price for the stock.
     */
    private double currentPrice;

    /**
     * The amount of percentage, the manager want control.
     */
    private double rate;

    /**
     * Generate the id of current stock.
     * @return int id.
     */
    private static String setId() {
        int length = Integer.toString(numStock).length();
        return "00000000".substring(0, 8 - length) + numStock;
    }

    /**
     * A constructor for stock.
     * @param price The original price for the stock.
     * @param backDoor The amount of percentage, the manager want control.
     * @throws StringIndexOutOfBoundsException when maximum account limit exceeded.
     */
    public Stock(double price, double backDoor) throws StringIndexOutOfBoundsException {
        try {
            numStock ++;
            stockId = setId();
            currentPrice = price;
            rate = backDoor;
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Maximum number of accounts reached!");
        }
    }


    /**
     * Get the name for the stocks.
     *
     * @return A String for the name.
     */
    public String getStockName() {
        return stockId;
    }

    /**
     * Get the price for the stock.
     *
     * @return An integer of current price.
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Algorithm for updating stock value randomly.
     */
    public void updatAlgorithm(StrategyCalculator s) {
        currentPrice = s.updateValue(currentPrice, rate); //Using strategy pattern to update stock price.
    }

    /**
     * Get the change rate for the stock
     *
     * @return A double of change rate
     */
    public double getRate() {
        return rate;
    }
}
