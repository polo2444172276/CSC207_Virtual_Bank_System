package bank_system;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The super class for all types of money, which store the information of money in ATM
 */
public class Currency {
    /**
     * The name for the Currency.
     */
    private final String type;
    /**
     * The collection of denomination.
     */
    private final List<Integer> denominationList = new ArrayList<>();
    /**
     * The collection of storage.
     */
    private final List<Integer> storage = new ArrayList<>();
    /**
     * The symbol for the symbol
     */
    private final String symbol;


    /**
     * The constructor for the class.
     * @param name the name for the currency.
     * @param notation the symbol for the currency.
     */
    public Currency(String name, String notation){
        this.type = name;
        this.symbol = notation;
    }

    /**
     * Get the name for denomination.
     * @return A String for the type of currency.
     */
    public String getType(){
        return type;
    }

    /**
     * Get the storage for each denomination for certain currency.
     * @return An arrayList of storage.
     */
    public List<Integer> getStorage(){
        return storage;
    }

    /**
     * Get all denomination for CAD.
     * @return An arrayList of denomination.
     */
    public List<Integer> getDenomination(){
        return denominationList;
    }

    /**
     * Get all symbol for CAD.
     * @return A String of symbol.
     */
    public String getSymbol(){
        return symbol;
    }

    /**
     * Add a new denomination to the current denomination list.
     * @param denomination Integer of new denomination.
     * @param value Integer of the number of new denomination.
     */
    public void addDenomination(int denomination, int value){
        int index;
        for (index = 0; index < denominationList.size(); index++)
            if (denomination > denominationList.get(index)){
                denominationList.add(index, denomination);
                storage.add(index, value);
                break;
            }
        if (index == denominationList.size()){
            denominationList.add(denomination);
            storage.add(value);
        }
    }

    /**
     * Add into or take money from the storage.
     * @param index The index number in denomination list.
     * @param amount The amount of money want to change.
     */
    public void moneyInOut(int index, int amount){
        int newValue = storage.get(index) + amount;
        storage.set(index, newValue);
    }

    /**
     * Alert the manager if any denomination of the currency is less than 20.
     * @param num the ATM number that this currency is in
     * @throws IOException No enough money.
     */
    public void alertManager(int num, String fileName) throws IOException {
        FileWriter fr = new FileWriter(fileName, true);
        for (int i = 0; i < storage.size(); i++){
            if (storage.get(i) < 20){
                fr.write(String.format("ATM#%d has less than 20 of $%d %s bill!\n",
                        num,denominationList.get(i), type));
            }
        }
        fr.close();
    }
}
