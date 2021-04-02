package bank_system;
import java.util.ArrayList;
import java.util.List;

public class ATMCash {

    /**
     * A list of currency which collect the information for all currency in this ATM.
     */
    private final List<Currency> currencyList = new ArrayList<>();

    /**
     * @return The collection of all currency in this ATM.
     */
    public List<Currency> getCurrencyList(){
        return currencyList;
    }

    /**
     * Add a new currency to this ATM
     * @param newCurrency the new type of currency
     */
    public void addCurrency(Currency newCurrency){
        currencyList.add(newCurrency);
    }

    public void addDenomination(String moneyType, int denomination, int value){
        for (Currency curr: currencyList){
            if (moneyType.equals(curr.getType())){
                curr.addDenomination(denomination, value);
            }
        }
    }

    /**
     * Person can call this method only when they want to add money in certain currency.
     * If amount is negative, withdraw money. Otherwise, add money.
     * @param moneyType The type of money.
     * @param index The order index of denomination. From large to small.
     * @param amount The number of denomination.
     */
    public void moneyInOut(String moneyType, int index, int amount){
        for (Currency curr: currencyList){
            if (moneyType.equals(curr.getType())){
                curr.moneyInOut(index, amount);
            }
        }
    }

    /**
     * Return the result of how to handle cash. If the handling cannot be process, return null.
     * @param index The index of denomination in currency list.
     * @param totalMoney The amount of money we need handle.
     * @return The result of method to handle cash.
     */
    private int[] evaluateCash(int index, int totalMoney){
        List<Integer> moneyStorage = currencyList.get(index).getStorage();
        List<Integer> denomination = currencyList.get(index).getDenomination();
        List<int[]> result = new ArrayList<>();
        for (int j = 0; j < moneyStorage.size(); j++){
            int[] temp = new int[moneyStorage.size()];
            int curr = totalMoney;
            for (int i = j; i < moneyStorage.size(); i++){
                temp[i] = Math.min(curr / denomination.get(i), moneyStorage.get(i));
                curr -= temp[i] * denomination.get(i);
            }
            if (curr == 0){
                result.add(temp);
            }
        }

        if (result.size() == 0){
            System.out.println("Cannot process the money you want, please try again");
            return null;
        }
        return result.get(0);
    }

    /**
     * This method will be called by all person when they want to withdraw money.
     * @param moneyType The type of money.
     * @param totalMoney The total money want to withdraw.
     */
    public boolean handlingMoney(String moneyType, int totalMoney){
        int index = -1;
        for (int i = 0; i < currencyList.size(); i++){
            if (moneyType.equals(currencyList.get(i).getType())){
                index = i;
                break;
            }
        }
        int[] method = evaluateCash(index, totalMoney);
        if (method != null){
            for (int i = 0; i < method.length; i++){
                moneyInOut(moneyType, i, -1 * method[i]);
            }
        }
        return method != null;
    }
}
