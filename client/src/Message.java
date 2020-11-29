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
        SIGNUP,
        ADD_ITEM
    }

    public enum ServerMessage {
        SIGNIN_STATUS, 
        SIGNUP_STATUS,
        ADD_ITEM_STATUS
    }

    private ClientMessage clientMessageType;
    private ServerMessage serverMessageType;
    private User user;
    private String status;
    private Item item;

    public Message(ClientMessage clientMessageType, User user) {
        this.clientMessageType = clientMessageType;
        this.user = user;
    }

    public Message(ServerMessage serverMessageType, String status, User user) {
        this.serverMessageType = serverMessageType;
        this.status = status;
        this.user = user;
    }

    public Message(ClientMessage clientMessageType, Item item) {
        this.clientMessageType = clientMessageType;
        this.item = item;
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

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        switch (clientMessageType) {
            default:
                return "no message";
            case SIGNIN:
                return "SIGNING IN! email: " + user.email + ", password: " + user.password;
            case SIGNUP:
                return "SIGNING UP! name: " + user.name + ", email: " + user.email + ", password: " + user.password;
            case ADD_ITEM:
                return "ADDING ITEM!";
        }
    }
}