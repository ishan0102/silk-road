/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

public class Message {
    public enum ClientMessage {
        SIGNIN, 
        SIGNUP
    }

    public enum ServerMessage {
        SIGNIN_STATUS, 
        SIGNUP_STATUS
    }

    private ClientMessage clientMessageType;
    private ServerMessage serverMessageType;
    private User user;
    private String status;

    public Message(ClientMessage clientMessageType, User user) {
        this.clientMessageType = clientMessageType;
        this.user = user;
    }

    public Message(ServerMessage serverMessageType, String status, User user) {
        this.serverMessageType = serverMessageType;
        this.status = status;
        this.user = user;
    }

    public ClientMessage getClientMessageType() {
        return clientMessageType;
    }

    public ServerMessage getServerMessageType() {
        return serverMessageType;
    }

    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        switch (serverMessageType) {
            default:
                return "no message";
            case SIGNIN_STATUS:
                return "no account with this email";
            case SIGNUP_STATUS:
                return "account with this email exists";
        }
    }
}