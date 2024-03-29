/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class Client extends Application {

    private static String host = "127.0.0.1";
    private BufferedReader fromServer;
    private PrintWriter toServer;
    public static boolean messageReceived;
    private UI gui;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Set up client side threads
     * @throws Exception
     */
    private void setUpNetworking() throws Exception {
        @SuppressWarnings("resource")
        Socket socket = new Socket(host, 4242);
        System.out.println("Connecting to... " + socket);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toServer = new PrintWriter(socket.getOutputStream());
        messageReceived = false;

        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String input;
                try {
                    while ((input = fromServer.readLine()) != null) {
                        System.out.println("From server: " + input);
                        processRequest(input);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        readerThread.start();
    }

    /**
     * Handle incoming messages from server
     * @param input message to be deciphered
     */
    protected void processRequest(String input) {
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        User user = message.getUser();
        HashMap<String, Item> itemInfo = message.getItemInfo();
        Item item = message.getItem();
        if (!user.email.equals("ALL CLIENTS")) {
            try {
                if (!user.email.equals(User.currentUser.email)) {
                    return;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            User.currentUser = user;
        }

        try {
            switch (message.getServerMessageType()) {
                case SIGNIN_STATUS:
                    UI.serverMessage = message.getStatus();
                    Client.messageReceived = true;
                    break;
                case SIGNUP_STATUS:
                    UI.serverMessage = message.getStatus();
                    Client.messageReceived = true;
                    break;
                case ADD_ITEM_STATUS:
                    UI.serverMessage = message.getStatus();
                    Client.messageReceived = true;
                    break;
                case SEND_NEW_ADDITION:
                    Item.itemInfo = itemInfo;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Tab itemTab = gui.addItemTab(itemInfo.get(item.getName()));
                            gui.updateBidInfo(itemInfo.get(item.getName()), "");
                            gui.tabPane.getTabs().add(itemTab);
                        }
                    });
                case SEND_ITEM_INFO:
                    Item.itemInfo = itemInfo;
                    Client.messageReceived = true;
                    break;
                case SEND_BID_STATUS:
                    gui.updateBidInfo(item, message.getStatus());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send message to server
     * @param message
     */
    public void sendToServer(Message message) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println("Sending to server: " + message);
        toServer.println(gson.toJson(message));
        toServer.flush();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.setUpNetworking();
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setOnHiding(event -> System.exit(0));

        gui = new UI(this, primaryStage);
        gui.startGUI();
    }
}