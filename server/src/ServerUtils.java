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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.sql.DataSource;

public class ServerUtils {
    private static Server server;
    private static Database db;
    private static DataSource ds;
    public static HashMap<String, Integer> guestList;
    public static HashMap<Integer, BiddingItem> itemList;

    public static void initialize(Database database, DataSource datasource) {
        db = database;
        ds = datasource;
        guestList = new HashMap<String, Integer>();
        itemList = new HashMap<Integer, BiddingItem>();
    }

    public static void initialize(Server s, Database database, DataSource datasource) {
        server = s;
        db = database;
        ds = datasource;
        guestList = new HashMap<String, Integer>();
        itemList = new HashMap<Integer, BiddingItem>();
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
                BiddingItem item = new BiddingItem(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                        rs.getDouble("bid_price"), rs.getDouble("buy_price"), rs.getInt("bidder_id"),
                        rs.getInt("seller_id"), rs.getBoolean("buyable"), rs.getBoolean("valid"));
                itemList.putIfAbsent(Integer.valueOf(rs.getString("id")), item);

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

    public static void addItem(String creatorEmail, String name, String description, Double bidPrice, Double buyPrice) {
        int creatorId = (guestList.get(creatorEmail));
        BiddingItem newItem = new BiddingItem(name, description, bidPrice, buyPrice, creatorId); // item belongs to the creator until someone else bids
        try {
            db.insertBiddingItem(newItem);
            User user = new User(creatorEmail);
            Message message = new Message(Message.ServerMessage.ADD_ITEM_STATUS, "Item added successfully!", user);
            server.sendToClient(message);
            updateClientBidding();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException, this should only show up if you run CreateDB.java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateClientBidding() {
        Collection<BiddingItem> items = itemList.values();
        ArrayList<Item> itemInfo = new ArrayList<Item>();
        for (BiddingItem item : items) {
            try {
                Guest guest;
                guest = db.getGuest(item.getBidderId());
                String email = guest.getEmail();
                itemInfo.add(item.toSimpleItem(email));
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        User user = new User("ALL CLIENTS");
        Message message = new Message(Message.ServerMessage.SEND_ITEM_INFO, itemInfo, user);
        server.sendToClient(message);
    }
}
