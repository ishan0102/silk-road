/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.server;

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
				"visits INT NOT NULL, " +
				"note VARCHAR(255), " +
				"last_visit TIMESTAMP" +
			")";
		String createMessage = "CREATE TABLE message (" +
				"id INTEGER NOT NULL " +
					"PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
				"text VARCHAR(255) NOT NULL, " +
				"guest_id INTEGER NOT NULL, " +

				"FOREIGN KEY (guest_id) REFERENCES guest (id)" +
			")";
		try (
			Connection connection = dataSource.getConnection();
			Statement createGuestStatement = connection.createStatement();
			Statement createMessageStatement = connection.createStatement();
		) {
			connection.setAutoCommit(false);
			createGuestStatement.executeUpdate(createGuest);
			createMessageStatement.executeUpdate(createMessage);
			connection.commit();
		}
	}
	
	
	int insertGuest(Guest guest) throws SQLException {
		String insertGuest = "INSERT INTO guest (name, visits, note, last_visit) " +
			"VALUES (?, ?, ?, ?)";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement insertGuestStatement = connection.prepareStatement(insertGuest, Statement.RETURN_GENERATED_KEYS);
		) {
			insertGuestStatement.setString(1, guest.getName());
			insertGuestStatement.setInt(2, guest.getVisits());
			insertGuestStatement.setString(3, guest.getNote());
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
				"SET name = ?, visits = ?, note = ?, last_visit = ? " +
				"WHERE id = ?";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement updateGuestStatement = connection.prepareStatement(updateGuest);
		) {
			updateGuestStatement.setString(1, guest.getName());
			updateGuestStatement.setInt(2, guest.getVisits());
			updateGuestStatement.setString(3, guest.getNote());
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
			int visits = results.getInt("visits");
			String note = results.getString("note");
			Instant lastVisited = results.getTimestamp("last_visit").toInstant();

			guests.add(new Guest(newId, name, visits, note, lastVisited));
		}
		return guests;
	}
	
	List<Guest> getAllGuests() throws SQLException {
		String selectGuests = "SELECT * FROM guest";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement selectGuestsStatement = connection.prepareStatement(selectGuests);
		) {
			ResultSet results = selectGuestsStatement.executeQuery();

			return readGuestResultSet(results);
		}	
    }

	void insertMessage(Message message) throws SQLException {
		String insertMessage = "INSERT INTO message (text, guest_id)" +
				"VALUES (?, ?)";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement insertMessageStatement = connection.prepareStatement(insertMessage);
		) {
			insertMessageStatement.setString(1, message.getText());
			insertMessageStatement.setInt(2, message.getGuestId());

			insertMessageStatement.executeUpdate();
		}
	}
	
	List<String> getMessages(int guestId) throws SQLException {
		String selectGuest = "SELECT * FROM message " +
				"WHERE guest_id = ?";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement selectGuestStatement = connection.prepareStatement(selectGuest);
		) {
			selectGuestStatement.setInt(1, guestId);

			ResultSet results = selectGuestStatement.executeQuery();

			List<String> messages = new ArrayList<>();
			while (results.next()) {
				messages.add(results.getString("text"));
			}

			return messages;
		}
	}
	
	void clearDatabase() throws SQLException {
		String clearGuests = "DELETE FROM guest";
		String clearMessages = "DELETE FROM message";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement clearGuestsStatement = connection.prepareStatement(clearGuests);
			PreparedStatement clearMessagesStatement = connection.prepareStatement(clearMessages);
		) {
			connection.setAutoCommit(false);
			clearMessagesStatement.executeUpdate();
			clearGuestsStatement.executeUpdate();
			connection.commit();
		}
	}
	
}