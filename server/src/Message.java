import java.util.ArrayList;

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
        ADD_ITEM,
        GET_ITEM_INFO,
        SEND_BID
    }

    public enum ServerMessage {
        SIGNIN_STATUS, 
        SIGNUP_STATUS,
        ADD_ITEM_STATUS,
        SEND_ITEM_INFO,
        SEND_BID_STATUS
    }

    private ClientMessage clientMessageType;
    private ServerMessage serverMessageType;
    private User user;
    private String status;
    private Item item;
    private ArrayList<Item> itemInfo;

    public Message(ClientMessage clientMessageType, User user) {
        this.clientMessageType = clientMessageType;
        this.user = user;
    }

    public Message(ClientMessage clientMessageType, Item item) {
        this.clientMessageType = clientMessageType;
        this.item = item;
    }
    
    public Message(ClientMessage clientMessageType, Item item, User user) {
        this.clientMessageType = clientMessageType;
        this.item = item;
        this.user = user;
    }

    public Message(ClientMessage clientMessageType) {
        this.clientMessageType = clientMessageType;
    }

    public Message(ServerMessage serverMessageType, String status, User user) {
        this.serverMessageType = serverMessageType;
        this.status = status;
        this.user = user;
    }

    public Message(ServerMessage serverMessageType, ArrayList<Item> itemInfo, User user) {
        this.serverMessageType = serverMessageType;
        this.itemInfo = itemInfo;
        this.user = user;
    }

    public Message(ServerMessage serverMessageType, String status, Item item, User user) {
        this.serverMessageType = serverMessageType;
        this.status = status;
        this.item = item;
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

    public Item getItem() {
        return item;
    }

    public ArrayList<Item> getItemInfo() {
        return itemInfo;
    }

    @Override
    public String toString() {
        switch (serverMessageType) {
            default:
                return "no message";
            case SIGNIN_STATUS:
                return status;
            case SIGNUP_STATUS:
                return status;
            case ADD_ITEM_STATUS:
                return status;
        }
    }
}