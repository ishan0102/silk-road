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
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

class Database {
	
	private final DataSource dataSource;
	
	Database(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	void initialize() throws SQLException {
		String createGuest = "CREATE TABLE guest (" +
				"id INTEGER NOT NULL " +
					"PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(255), " +
                "email VARCHAR(255), " +
                "password VARCHAR(255), " +
				"last_visit TIMESTAMP" +
			")";
		try (
			Connection connection = dataSource.getConnection();
			Statement createGuestStatement = connection.createStatement();
		) {
			connection.setAutoCommit(false);
			createGuestStatement.executeUpdate(createGuest);
			connection.commit();
		}
	}
	
	int insertGuest(Guest guest) throws SQLException {
		String insertGuest = "INSERT INTO guest (name, email, password, last_visit) " +
			"VALUES (?, ?, ?, ?)";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement insertGuestStatement = connection.prepareStatement(insertGuest, Statement.RETURN_GENERATED_KEYS);
		) {
			insertGuestStatement.setString(1, guest.getName());
			insertGuestStatement.setString(2, guest.getEmail());
			insertGuestStatement.setString(3, guest.getPassword());
			insertGuestStatement.setTimestamp(4, Timestamp.from(guest.getLastVisit()));

			insertGuestStatement.executeUpdate();
			
			ResultSet generatedKeys = insertGuestStatement.getGeneratedKeys();
			generatedKeys.next();
			int id = generatedKeys.getInt(1);
			return id;
		}
	}
	
	void updateGuest(Guest guest) throws SQLException {
		String updateGuest = "UPDATE guest " +
				"SET name = ?, email = ?, password = ?, last_visit = ? " +
				"WHERE id = ?";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement updateGuestStatement = connection.prepareStatement(updateGuest);
		) {
			updateGuestStatement.setString(1, guest.getName());
			updateGuestStatement.setString(2, guest.getEmail());
			updateGuestStatement.setString(3, guest.getPassword());
			updateGuestStatement.setTimestamp(4, Timestamp.from(guest.getLastVisit()));
			updateGuestStatement.setInt(5, guest.getId());
			
			updateGuestStatement.executeUpdate();
		}
	}
	
	Guest getGuest(int id) throws SQLException {
		String selectGuest = "SELECT * FROM guest " +
				"WHERE id = ?";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement selectGuestStatement = connection.prepareStatement(selectGuest);
		) {
			selectGuestStatement.setInt(1, id);
			ResultSet results = selectGuestStatement.executeQuery();
			return readGuestResultSet(results).get(0);
		}
	}
	
	private static List<Guest> readGuestResultSet(ResultSet results) throws SQLException {
		List<Guest> guests = new ArrayList<>();
		while (results.next()) {
			int newId = results.getInt("id");
			String name = results.getString("name");
            String email = results.getString("email");
			String password = results.getString("password");
			Instant lastVisited = results.getTimestamp("last_visit").toInstant();

			guests.add(new Guest(newId, name, email, password, lastVisited));
		}
		return guests;
	}
	
	void clearDatabase() throws SQLException {
		String clearGuests = "DELETE FROM guest";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement clearGuestsStatement = connection.prepareStatement(clearGuests);
		) {
			connection.setAutoCommit(false);
			clearGuestsStatement.executeUpdate();
			connection.commit();
		}
	}
	
}