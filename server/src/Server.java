/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Observable;

import com.google.gson.Gson;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

class Server extends Observable {

    private static Server server;
    private static Database db;

    public static void main(String[] args) {
        server = new Server();
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
        ServerUtils.initialize(server, db, dataSource);
        System.out.println("Database initialized successfully.");

        // Initialize local data structure for users
        try {
            ServerUtils.getGuestList();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        System.out.println(ServerUtils.userEmails);
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
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        User user = message.getUser();

        try {
            switch (message.getClientMessageType()) {
                case SIGNIN:
                    System.out.println("attempting sign in");
                    ServerUtils.signIn(user.email, user.password);
                    break;
                case SIGNUP:
                    System.out.println("attempting sign up");
                    ServerUtils.signUp(user.name, user.email, user.password);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(Message message) {
        this.setChanged();
        this.notifyObservers(message);
    }
}