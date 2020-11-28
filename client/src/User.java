/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

public class User {
    String name;
    String email;
    String password;

    // Used for verifying users during sign-in
    public User(String email, String password) {
        this.name = null;
        this.email = email;
        this.password = password;
    }

    // Used for registering users during sign-up
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
