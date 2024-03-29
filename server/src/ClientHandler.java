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
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Observable;

class ClientHandler implements Runnable, Observer {
    private Server server;
    private Socket clientSocket;
    private BufferedReader fromClient;
    private PrintWriter toClient;

    /**
     * Handles all clients
     * @param server auction server
     * @param clientSocket specific client
     */
    protected ClientHandler(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            toClient = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send message to client
     * @param message
     */
    protected void sendToClient(Message message) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println("Sending to client: " + message);
        toClient.println(gson.toJson(message));
        toClient.flush();
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = fromClient.readLine()) != null) {
                System.out.println("From client: " + input);
                server.processRequest(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.sendToClient((Message) arg);
    }
}