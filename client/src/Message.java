/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

public class Message {
    public enum Type {
        SIGNIN, SIGNUP
    }

    private Type type;
    private User user;

    public Message(Type type, User user) {
        this.type = type;
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        switch (type) {
            default:
                return "no message";
            case SIGNIN:
                return "SIGNING IN! email: " + user.email + ", password: " + user.password;
            case SIGNUP:
                return "SIGNING UP! name: " + user.name + ", email: " + user.email + ", password: " + user.password;
        }
    }
}