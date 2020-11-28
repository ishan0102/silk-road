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
import java.time.Instant;
import java.util.HashMap;

import javax.sql.DataSource;

public class ServerUtils {
    private static Server server;
    private static Database db;
    private static DataSource ds;
    public static HashMap<String, Integer> userEmails;

    public static void initialize(Database database, DataSource datasource) {
        db = database;
        ds = datasource;
        userEmails = new HashMap<String, Integer>();
    }

    public static void initialize(Server s, Database database, DataSource datasource) {
        server = s;
        db = database;
        ds = datasource;
        userEmails = new HashMap<String, Integer>();
    }

    public static void getGuestList() throws SQLException {
        String getGuests = "SELECT * FROM guest";
        try (
			Connection connection = ds.getConnection();
			PreparedStatement getGuestsStatement = connection.prepareStatement(getGuests);
		) {
			ResultSet rs = getGuestsStatement.executeQuery();
            while (rs.next()) {
                userEmails.putIfAbsent(rs.getString("email"), Integer.valueOf(rs.getString("id")));

                // System.out.println("id: " + rs.getString("id"));
                // System.out.println("name: " + rs.getString("name"));
                // System.out.println("email: " + rs.getString("email"));
                // System.out.println("password: " + rs.getString("password"));
                // System.out.println("last visit: " + rs.getString("last_visit"));
                // System.out.println();
            }
		}
    }

    public static void signIn(String email, String password) {
        String selectGuest = "SELECT * FROM guest " +
                "WHERE id = ?";

        User user = new User(email);
        int id;
        if (!userEmails.containsKey(email)) {
            Message message = new Message(Message.ServerMessage.SIGNIN_STATUS, "No account with this email exists", user);
            server.sendToClient(message);
            return;
        } else {
            id = userEmails.get(email);
        }

        try (
			Connection connection = ds.getConnection();
			PreparedStatement signInStatement = connection.prepareStatement(selectGuest);
		) {
			signInStatement.setInt(1, id);
			ResultSet results = signInStatement.executeQuery();
            results.next();
            if (email.equals(results.getString("email")) && password.equals(results.getString("password"))) {
                Message message = new Message(Message.ServerMessage.SIGNIN_STATUS, "Login successful", user);
                server.sendToClient(message);
            } else {
                Message message = new Message(Message.ServerMessage.SIGNIN_STATUS, "Incorrect password", user);
                server.sendToClient(message);
            }
		} catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void signUp(String name, String email, String password) {
        User user = new User(email);
        
        if (userEmails.containsKey(email)) {
            Message message = new Message(Message.ServerMessage.SIGNUP_STATUS, "An account with this email already exists", user);
            server.sendToClient(message);
            return;
        }
        Guest guest = new Guest(name, email, password);
        int id;
        try {
            id = db.insertGuest(guest);
            userEmails.putIfAbsent(email, id);
            Message message = new Message(Message.ServerMessage.SIGNUP_STATUS, "Login successful", user);
            server.sendToClient(message);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
