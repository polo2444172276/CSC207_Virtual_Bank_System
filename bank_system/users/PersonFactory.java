package bank_system.users;

public class PersonFactory {
    /**
     * Produce account for users
     *
     * @param type type of accounts
     * @return An instance of account with given type.
     */
    public Person getPeople(String type, String username, String password) {
        switch (type.toLowerCase()){
            case "user":
                return new User(username, password);
            case "manager":
                return new BankManager(username, password);
            case "staff":
                return new BankStaff(username, password);
            default:
                return null;
        }
    }
}
