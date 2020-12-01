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
    public static String emailKey;

    public static void main(String[] args) {
        launch(args);
    }

    private void setUpNetworking() throws Exception {
        @SuppressWarnings("resource")
        Socket socket = new Socket(host, 4242);
        System.out.println("Connecting to... " + socket);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toServer = new PrintWriter(socket.getOutputStream());

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

    protected void processRequest(String input) {
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        User user = message.getUser();
        HashMap<String, Item> itemInfo = message.getItemInfo();
        Item item = message.getItem();
        
        if (user == null) {
            System.out.println("user null");
            return;
        }

        if (!user.email.equals(emailKey) && !user.email.equals("ALL CLIENTS")) {
            System.out.println("not right email key");
            return;
        }
        
        try {
            switch (message.getServerMessageType()) {
                case SIGNIN_STATUS:
                    if (message.getStatus().equals("Login successful")) {
                        User.currentUser = user;
                    }
                    UI.serverMessage = message.getStatus();
                    Client.messageReceived = true;
                    break;
                case SIGNUP_STATUS:
                    if (message.getStatus().equals("Login successful")) {
                        User.currentUser = user;
                    }
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
                            System.out.println("running later 1");
                            Tab itemTab = gui.addItemTab(itemInfo.get(item.getName()));
                            gui.updateBidInfo(itemInfo.get(item.getName()), "");
                            gui.tabPane.getTabs().add(itemTab);
                        }
                    });
                    break;
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

        System.out.println("message received status should be false: " + Client.messageReceived);
    }

    public void sendToServer(Message message) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println("Sending to server: " + message);
        toServer.println(gson.toJson(message));
        toServer.flush();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        messageReceived = false;
        
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