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

import javax.sql.DataSource;

public class ServerUtils {
    private static Database db;
    private static DataSource ds;

    public static void initialize(Database database, DataSource datasource) {
        db = database;
        ds = datasource;
    }

    public static void getGuestList() throws SQLException {
        String getGuests = "SELECT * FROM guest";
        try (
			Connection connection = ds.getConnection();
			PreparedStatement getGuestsStatement = connection.prepareStatement(getGuests);
		) {
			ResultSet results = getGuestsStatement.executeQuery();
            while (results.next()) {
                System.out.println("id: " + results.getString("id"));
                System.out.println("name: " + results.getString("name"));
                System.out.println("email: " + results.getString("email"));
                System.out.println("password: " + results.getString("password"));
                System.out.println("last visit: " + results.getString("last_visit"));
                System.out.println();
            }
		}
    }

    public static void signUp(String name, String email, String password) {
        Guest guest = new Guest(name, email, password);
        System.out.println(guest);
        int id;
        try {
            id = db.insertGuest(guest);
            Guest getGuest = db.getGuest(id);
            System.out.println(id);
            System.out.println(getGuest);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
