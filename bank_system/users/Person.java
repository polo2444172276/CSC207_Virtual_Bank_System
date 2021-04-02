package bank_system.users;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class should hold all similar features for both worker and user
 */
public abstract class Person {
    private final String username;
    private String password;

    Person(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password;
    }

    /**
     * Default constructor for class Person.
     */

    public String getUsername() {
        return this.username;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "username: " + this.username + ", password: " + this.password;
    }

    public abstract void userInterface(BufferedReader br) throws IOException;

    /**
     * Gets the output information using in file save.
     * Should be in a format of:
     * "{type},{username},{password}"
     * @return String of information
     */
    public abstract String fileOutputFormat();
}

