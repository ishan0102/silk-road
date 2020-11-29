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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
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

    protected void processRequest(String input) {
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        User user = message.getUser();
        if (!user.email.equals(User.currentUser.email)) {
            return;
        }
        User.currentUser = user;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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