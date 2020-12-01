/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Observable;

import com.google.gson.Gson;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

class Server extends Observable {

    private static Server server;
    private static Database db;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown Hook is running!");
                // ServerUtils.updateItemDB();
            }
        });
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
        if (!Files.isDirectory(Paths.get(System.getProperty("user.dir") + "/" + "ehills_users"))) {
            System.out.println("No database found, creating a new database.");
            CreateDB.createDB();
        } else {
            System.out.println("Database already exists.");
        }
        ServerUtils.initialize(server, db, dataSource);

        // Initialize local data structure for users
        try {
            ServerUtils.generateGuestList();
            ServerUtils.generateItemList();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        System.out.println(ServerUtils.guestList);
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
        Item item = message.getItem();

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
                case ADD_ITEM:
                    System.out.println("attempting to add item");
                    ServerUtils.addItem(item.getEmail(), item.getName(), item.getDescription(), item.getBidPrice(), item.getBuyPrice());
                    break;
                case GET_NEW_ADDITION:
                    System.out.println("getting new item added");
                    ServerUtils.getAddition(item);
                case GET_ITEM_INFO:
                    System.out.println("retrieving item info");
                    ServerUtils.updateClientBidding();
                    break;
                case SEND_BID:
                    System.out.println("checking bid");
                    System.out.println(user);
                    ServerUtils.checkBid(user.email, item);
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