import java.time.Instant;

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
    Instant lastVisit;

    // Used to compare server responses with the corresponding client
    public User(String email) {
        this.name = null;
        this.email = email;
        this.password = null;
    }

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

    // Used when sending server information back to client
    public User(String name, String email, String password, Instant lastVisit) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastVisit = lastVisit;
    }

    @Override
    public String toString() {
        return "name: " + name + " email: " + email + " password: " + " last visit: " + lastVisit;
    }
}
