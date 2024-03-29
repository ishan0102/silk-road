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
    
    /**
     * Initialize guest and item databases
     * @throws SQLException
     */
	void initialize() throws SQLException {
		String createGuest = "CREATE TABLE guest (" +
				"id INTEGER NOT NULL " +
					"PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(255), " +
                "email VARCHAR(255), " +
                "password VARCHAR(255), " +
				"last_visit TIMESTAMP" +
            ")";
        String createItems = "CREATE TABLE item (" +
                "id INTEGER NOT NULL " +
                    "PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "name VARCHAR(255) NOT NULL, " +
                "description VARCHAR(255) NOT NULL, " +
                "bid_price DOUBLE NOT NULL, " +
                "buy_price DOUBLE NOT NULL, " +
                "bidder_id INTEGER NOT NULL, " +
                "seller_id INTEGER NOT NULL, " +
                "buyable BOOLEAN NOT NULL, " +
                "valid BOOLEAN NOT NULL, " +
                "FOREIGN KEY (bidder_id) REFERENCES guest (id)" +
            ")";
		try (
			Connection connection = dataSource.getConnection();
            Statement createGuestStatement = connection.createStatement();
            Statement createItemsStatement = connection.createStatement();
		) {
			connection.setAutoCommit(false);
            createGuestStatement.executeUpdate(createGuest);
            createItemsStatement.executeUpdate(createItems);
			connection.commit();
		}
	}
    
    /**
     * Insert a new guest into the database
     * @param guest guest to insert
     * @return guest ID
     * @throws SQLException
     */
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
    
    /**
     * Update guest data
     * @param guest guest to be updated
     * @throws SQLException
     */
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
    
    /**
     * Get a specific guest
     * @param id guest id to be pulled
     * @return guest
     * @throws SQLException
     */
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
    
    /**
     * Insert item into database
     * @param item item to be inserted
     * @throws SQLException
     */
    void insertBidItem(BidItem item) throws SQLException {
		String insertItem = "INSERT INTO item (name, description, bid_price, buy_price, bidder_id, seller_id, buyable, valid)" +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement insertItemStatement = connection.prepareStatement(insertItem);
		) {
			insertItemStatement.setString(1, item.getName());
			insertItemStatement.setString(2, item.getDescription());
			insertItemStatement.setDouble(3, item.getBidPrice());
			insertItemStatement.setDouble(4, item.getBuyPrice());
			insertItemStatement.setInt(5, item.getBidderId());
			insertItemStatement.setInt(6, item.getSellerId());
			insertItemStatement.setBoolean(7, item.getBuyable());
            insertItemStatement.setBoolean(8, item.getValid());
            
			insertItemStatement.executeUpdate();
		}
    }
    
    /**
     * Update a particular item
     * @param item item to be updated
     * @throws SQLException
     */
    void updateItem(BidItem item) throws SQLException {
		String updateItem = "UPDATE item " +
				"SET name = ?, description = ?, bid_price = ?, buy_price = ?, bidder_id = ?, seller_id = ?, buyable = ?, valid = ? " +
				"WHERE id = ?";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement updateItemStatement = connection.prepareStatement(updateItem);
		) {
			updateItemStatement.setString(1, item.getName());
			updateItemStatement.setString(2, item.getDescription());
			updateItemStatement.setDouble(3, item.getBidPrice());
			updateItemStatement.setDouble(4, item.getBuyPrice());
			updateItemStatement.setInt(5, item.getBidderId());
			updateItemStatement.setInt(6, item.getSellerId());
			updateItemStatement.setBoolean(7, item.getBuyable());
            updateItemStatement.setBoolean(8, item.getValid());
            updateItemStatement.setInt(9, item.getItemId());
            
			updateItemStatement.executeUpdate();
		}
	}
    
    /**
     * Item to be pulled
     * @param id id of item to be pulled
     * @return
     * @throws SQLException
     */
	BidItem getItem(int id) throws SQLException {
		String selectItem = "SELECT * FROM item " +
				"WHERE id = ?";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement selectGuestStatement = connection.prepareStatement(selectItem);
		) {
			selectGuestStatement.setInt(1, id);
			ResultSet results = selectGuestStatement.executeQuery();
			return readItemResultSet(results).get(0);
		}
	}
	
	private static List<BidItem> readItemResultSet(ResultSet rs) throws SQLException {
		List<BidItem> itemList = new ArrayList<>();
		while (rs.next()) {
			itemList.add(new BidItem(rs.getInt("id"), rs.getString("name"), rs.getString("description"),
                        rs.getDouble("bid_price"), rs.getDouble("buy_price"), rs.getInt("bidder_id"),
                        rs.getInt("seller_id"), rs.getBoolean("buyable"), rs.getBoolean("valid")));
		}
		return itemList;
	}
}