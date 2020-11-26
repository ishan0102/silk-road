/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.server;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

import static java.util.Comparator.comparing;

public class GuestList {
	
	private static class Option {
		private final String label;
		private final Runnable runnable;

		Option(String label, Runnable runnable) {
			this.label = label;
			this.runnable = runnable;
		}

		String getLabel() {
			return label;
		}
		void run() {
			runnable.run();
		}
	}

	private static String prompt() {
		System.out.println();
		System.out.print("> ");
		return input.nextLine();
	}
	
	private static void showMenu(String prompt, Option... options) {
		System.out.println(prompt);
		for (int i = 0; i < options.length; i++) {
			System.out.println(i + ") " + options[i].getLabel());
		}
		String inputLine = prompt();
		try {
			int choice = Integer.parseInt(inputLine);
			options[choice].run();
		} catch (NumberFormatException nfe) {
			System.out.println("Error: " + inputLine + " is not an integer.");
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println("Error: " + inputLine + " is not a valid choice.");
		}
	}
	
	private static final Scanner input = new Scanner(System.in);

	
	
	private static Database db;
	
	public static void main(String[] args) {
		
		// Makes Connections to URL `jdbc:derby:guest_list;create=true`
		EmbeddedDataSource dataSource = new EmbeddedConnectionPoolDataSource();
		dataSource.setDatabaseName("guest_list");
		dataSource.setCreateDatabase("create");
		
		db = new Database(dataSource);
		
		while (true) {
			showMenu("Welcome to the Interactive Guest List!",
					new Option("INITIALIZE DATABASE", GuestList::initializeDatabase),
					new Option("Sign In", GuestList::signIn),
					new Option("Read List", GuestList::read),
					new Option("Clear List", GuestList::clear),
					new Option("Quit", () -> System.exit(0)));
		}
	}
	
	private static void initializeDatabase() {
		try {
			db.initialize();
			System.out.println("Database initialized successfully.");
		} catch (SQLException sqle) {
			System.out.println("Database initialization failed.");
			sqle.printStackTrace();
		}
	}
	
	private static void signIn() {
		showMenu("Do you already have an ID?",
				new Option("Yes", GuestList::returnVisit),
				new Option("No", GuestList::firstVisit));
	}
	
	private static void returnVisit() {
		System.out.println("Please enter your ID.");
		Integer id = null;
		while (id == null) {
			try {
				String inputLine = prompt();
				id = Integer.parseInt(inputLine);
			} catch (NumberFormatException nfe) {
				System.out.println("Error: ID must be an integer.");
			}
		}
		try {
			Guest guest = db.getGuest(id);
			guest.recordVisit();
			db.updateGuest(guest);
			System.out.println("Return visit recorded successfully. Welcome back, " + guest + "!");
			promptToLeaveMessage(id);
		} catch (SQLException sqle) {
			System.out.println("Error in recording return visit:");
			sqle.printStackTrace();
		}
		
	}
	
	private static void promptToLeaveMessage(int id) {
		showMenu("Would you like to leave a message?",
				new Option("Yes", () -> { GuestList.leaveMessage(id); }),
				new Option("No", () -> { return; }));
	}
	
	private static void leaveMessage(int id) {
		System.out.println("Please enter your message.");
		String inputLine = prompt();
		Message message = new Message(id, inputLine);
		try {
			db.insertMessage(message);
		} catch (SQLException sqle) {
			System.out.println("Error saving message:");
			sqle.printStackTrace();
		}
		System.out.println("Message saved!");
	}
	
	private static void firstVisit() {
		System.out.println("Hello! What is your name?");
		String name = prompt();
		System.out.println("Add a note about yourself:");
		String note = prompt();
		Guest newGuest = new Guest(name, note);
		newGuest.recordVisit();
		try {
			int id = db.insertGuest(newGuest);
			System.out.println("Your first visit was recorded successfully!");
			System.out.println("Your ID is " + id + ".");
			promptToLeaveMessage(id);
		} catch (SQLException sqle) {
			System.out.println("Error recording your first visit:");
			sqle.printStackTrace();
		}
	}
	
	private static void read() {
		try {
			List<Guest> guests = db.getAllGuests();
			guests.sort(comparing(Guest::getId));
			while (true) {
				System.out.println("Guest List:");
				for (Guest guest : guests) {
					System.out.println(guest.getId() + ") " + guest.info());
				}
				System.out.println("other) RETURN TO MAIN MENU");
				String inputLine = prompt();
				int selection = Integer.parseInt(inputLine);
				boolean found = false;
				for (Guest guest : guests) {
					if (guest.getId() == selection) {
						found = true;
						System.out.println("Messages from " + guest.getName() + ":");
						List<String> messages = db.getMessages(guest.getId());
						for (String message : messages) {
							System.out.println(message);
						}
						System.out.println();
					}
				}
				if (!found)
					break;
			}
		} catch (SQLException sqle) {
			System.out.println("Error reading guest list:");
			sqle.printStackTrace();
		} catch (NumberFormatException nfe) {
			System.out.println("Error parsing integer.");
		}
	}
	
	private static void clear() {
		try {
			db.clearDatabase();
			System.out.println("Successfully cleared database!");
		} catch (SQLException sqle) {
			System.out.println("Error clearing database:");
			sqle.printStackTrace();
		}
	}

}
