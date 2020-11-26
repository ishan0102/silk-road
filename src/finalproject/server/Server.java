/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Observable;

import com.google.gson.Gson;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

class Server extends Observable {

    private static Database db;

    public static void main(String[] args) {
        Server server = new Server();
        server.runServer();
    }

    private void runServer() {
        try {
            setUpDatabase();
            setUpNetworking();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void setUpDatabase() {
        EmbeddedDataSource dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName("ehills_users");
        dataSource.setCreateDatabase("create");
        db = new Database(dataSource);

        try {
			db.initialize();
			System.out.println("Database initialized successfully.");
		} catch (SQLException sqle) {
            if (sqle.getSQLState().equals("X0Y32")) {
                System.out.println("Database already exists");
                return;
            }
			System.out.println("Database initialization failed.");
			sqle.printStackTrace();
		}
    }

    private void setUpNetworking() throws Exception {
        @SuppressWarnings("resource")
        ServerSocket serverSock = new ServerSocket(4242);
        while(true) {
            Socket clientSocket = serverSock.accept();
            System.out.println("Connecting to... " + clientSocket);

            ClientHandler handler = new ClientHandler(this, clientSocket);
            Thread t = new Thread(handler);
            this.addObserver(handler);
            t.start();
        }
    }

    protected void processRequest(String input) {
        String output = "Error";
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        try {
            String temp = "";
            switch (message.type) {
                case "upper":
                    temp = message.input.toUpperCase();
                    break;
                case "lower":
                    temp = message.input.toLowerCase();
                    break;
                case "strip":
                    temp = message.input.replace(" ", "");
                    break;
            }
            output = "";
            for (int i = 0; i < message.number; i++) {
                output += temp;
                output += " ";
            }
            this.setChanged();
            this.notifyObservers(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}