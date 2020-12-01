/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import javax.sql.DataSource;

public class ServerUtils {
    public static Server server;
    private static Database db;
    private static DataSource ds;
    public static HashMap<String, Integer> guestList;
    public static HashMap<String, BidItem> itemList;
    public static Object lock = new Object();

    public static void initialize(Database database, DataSource datasource) {
        db = database;
        ds = datasource;
        guestList = new HashMap<String, Integer>();
        itemList = new HashMap<String, BidItem>();
    }

    public static void initialize(Server s, Database database, DataSource datasource) {
        server = s;
        db = database;
        ds = datasource;
        guestList = new HashMap<String, Integer>();
        itemList = new HashMap<String, BidItem>();
    }

    public static void generateGuestList() throws SQLException {
        String getGuests = "SELECT * FROM guest";
        try (
			Connection connection = ds.getConnection();
			PreparedStatement getGuestsStatement = connection.prepareStatement(getGuests);
		) {
			ResultSet rs = getGuestsStatement.executeQuery();
            while (rs.next()) {
                guestList.putIfAbsent(rs.getString("email"), rs.getInt("id"));
            }
		}
    }

    public static void generateItemList() throws SQLException {
        String getItems = "SELECT * FROM item";
        try (
			Connection connection = ds.getConnection();
			PreparedStatement getItemsStatement = connection.prepareStatement(getItems);
		) {
			ResultSet rs = getItemsStatement.executeQuery();
            while (rs.next()) {
                BidItem item = new BidItem(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                        rs.getDouble("bid_price"), rs.getDouble("buy_price"), rs.getInt("bidder_id"),
                        rs.getInt("seller_id"), rs.getBoolean("buyable"), rs.getBoolean("valid"));
                itemList.putIfAbsent(rs.getString("name"), item);

                System.out.println(item);
            }
		}
    }

    public static void signIn(String email, String password) {
        User user = new User(email);
        int id;
        if (!guestList.containsKey(email)) {
            Message message = new Message(Message.ServerMessage.SIGNIN_STATUS, "No account with this email exists", user);
            server.sendToClient(message);
            return;
        } else {
            id = guestList.get(email);
        }

        try {
            Guest guest = db.getGuest(id);
            Message message;
            if (email.equals(guest.getEmail()) && password.equals(guest.getPassword())) {
                user = new User(guest.getName(), guest.getEmail(), guest.getPassword(), guest.getLastVisit());
                guest.recordVisit();
                db.updateGuest(guest);
                message = new Message(Message.ServerMessage.SIGNIN_STATUS, "Login successful", user);
            } else {
                message = new Message(Message.ServerMessage.SIGNIN_STATUS, "Incorrect password", user);
            }
            server.sendToClient(message);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void signUp(String name, String email, String password) {
        User user = new User(email);
        
        if (guestList.containsKey(email)) {
            Message message = new Message(Message.ServerMessage.SIGNUP_STATUS, "An account with this email already exists", user);
            server.sendToClient(message);
            return;
        }
        Guest guest = new Guest(name, email, password);
        int id;
        try {
            guest.recordVisit();
            id = db.insertGuest(guest);
            guestList.putIfAbsent(email, id);
            user = new User(name, email, password, guest.getLastVisit());
            Message message = new Message(Message.ServerMessage.SIGNUP_STATUS, "Login successful", user);
            server.sendToClient(message);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, this should only show up if you run CreateDB.java");
        }
    }

    public static void addItem(String creatorEmail, String name, String description, String bidPriceStr, String buyPriceStr) {
        int creatorId = (guestList.get(creatorEmail));

        if (name.isEmpty() || description.isEmpty() || bidPriceStr.isEmpty() || buyPriceStr.isEmpty()) {
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "All fields must be completed", user);
            server.sendToClient(message);
            return;
        }

        Double bidPrice, buyPrice;
        try {
            bidPrice = Double.valueOf(bidPriceStr);
            buyPrice = Double.valueOf(buyPriceStr);
        } catch (NumberFormatException e) {
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "Bid and buy prices must be numbers", user);
            server.sendToClient(message);
            return;
        }

        if (bidPrice < 0 || buyPrice < 0) {
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "Bid and buy prices must be greater than 0", user);
            server.sendToClient(message);
            return;
        }

        if (buyPrice < bidPrice) {
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "Buy price must be above starting bid", user);
            server.sendToClient(message);
            return;
        }

        BidItem newItem = new BidItem(name, description, bidPrice, buyPrice, creatorId); // item belongs to the creator until someone else bids
        try {
            db.insertBidItem(newItem);
            itemList.putIfAbsent(name, newItem);
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "Item added successfully!", user);
            server.sendToClient(message);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, this should only show up if you run CreateDB.java");
        } catch (Exception e) {
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "Invalid input, please try again", user);
            server.sendToClient(message);
        }
    }

    public static void getAddition(Item newItem) {
        Collection<BidItem> items = itemList.values();
        HashMap<String, Item> itemInfo = new HashMap<String, Item>();
        for (BidItem item : items) {
            try {
                Guest guest;
                guest = db.getGuest(item.getBidderId());
                String email = guest.getEmail();
                itemInfo.put(item.getName(), item.toSimpleItem(email));
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        Item.itemInfo = itemInfo;
        User user = new User("ALL CLIENTS");
        Message message = new Message(Message.ServerMessage.SEND_NEW_ADDITION, itemInfo, newItem, user);
        server.sendToClient(message);
    }

    public static void checkBid(String bidderEmail, Item item) {
        BidItem bidItem = itemList.get(item.getName());
        int id = guestList.get(bidderEmail);
        
        Double bidPrice;
        try {
            bidPrice = Double.valueOf(item.getBidPrice());
        } catch (Exception e) {
            bidPrice = -1.0;
            User user = new User(bidderEmail);
            Message message = new Message(Message.ServerMessage.SEND_BID_STATUS,
                    "Your bid is invalid (" + e.toString() + ")", bidItem.toSimpleItem(bidderEmail), user);
            server.sendToClient(message);
        }
        
        if (bidPrice > 0 && bidPrice >= bidItem.getBuyPrice()) {
            bidItem.setBidPrice(bidPrice);
            synchronized(lock) {
                bidItem.setBidPrice(bidPrice);
            }
            bidItem.setBuyable(false);
            User user = new User("ALL CLIENTS");
            Message message = new Message(Message.ServerMessage.SEND_BID_STATUS, bidderEmail + " has won this auction!",
                    bidItem.toSimpleItem(bidderEmail), user);
            server.sendToClient(message);
        } else if (bidPrice > 0 && bidPrice > bidItem.getBidPrice()) {
            synchronized(lock) {
                bidItem.setBidPrice(bidPrice);
            }
            bidItem.setBidderId(id);
            User user = new User("ALL CLIENTS");
            Message message = new Message(Message.ServerMessage.SEND_BID_STATUS, "Bid has been updated",
                    bidItem.toSimpleItem(bidderEmail), user);
            server.sendToClient(message);
        } else if (bidPrice > 0 && bidPrice <= bidItem.getBidPrice()) {
            User user = new User(bidderEmail);
            Message message = new Message(Message.ServerMessage.SEND_BID_STATUS, "Bid is not high enough",
                    bidItem.toSimpleItem(bidderEmail), user);
            server.sendToClient(message);
        }
    }

    public static void updateClientBidding() {
        Collection<BidItem> items = itemList.values();
        HashMap<String, Item> itemInfo = new HashMap<String, Item>();
        for (BidItem item : items) {
            try {
                Guest guest;
                guest = db.getGuest(item.getBidderId());
                String email = guest.getEmail();
                itemInfo.put(item.getName(), item.toSimpleItem(email));
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        Item.itemInfo = itemInfo;
        User user = new User("ALL CLIENTS");
        Message message = new Message(Message.ServerMessage.SEND_ITEM_INFO, itemInfo, user);
        server.sendToClient(message);
    }

    public static void updateItemDB() {
        Collection<BidItem> items = itemList.values();
        for (BidItem item : items) {
            try {
                BidItem oldItem = db.getItem(item.getItemId());
                System.out.println(oldItem);
                oldItem = item;
                System.out.println(oldItem);
                db.updateItem(oldItem);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }
}
