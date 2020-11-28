/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.sql.SQLException;

public class ServerUtils {
    private static Database db;

    public static void initialize(Database database) {
        db = database;
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
