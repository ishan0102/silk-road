/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.client;

public class Message {
    enum Type {
        SIGNIN, SIGNUP
    }

    private Type type;
    private User user;

    public Message(Type type, User user) {
        this.type = type;
        this.user = user;
    }

    @Override
    public String toString() {
        return "name: " + user.name + " - email: " + user.email + " - password: " + user.password;
    }
}