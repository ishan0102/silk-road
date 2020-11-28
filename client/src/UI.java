/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UI {
    private Stage stage;
    private Client client;
    public static String serverMessage;

    public UI(Client client, Stage stage) {
        this.stage = stage;
        this.client = client;
    }

    public void startGUI() {
        stage.setTitle("eHills");
        login();
    }

    public void login() {
        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setVgap(12);

        Button signIn = new Button("Sign In");
        signIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signIn();
            }
        });

        Button signUp = new Button("Sign Up");
        signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signUp();
            }
        });

        loginPane.add(signIn, 0, 0);
        GridPane.setHalignment(signIn, HPos.CENTER);
        loginPane.add(signUp, 0, 1);
        GridPane.setHalignment(signUp, HPos.CENTER);

        Scene scene = new Scene(loginPane);
        stage.setWidth(325);
        stage.setHeight(375);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public void signIn() {
        GridPane signInPane = new GridPane();
        signInPane.setAlignment(Pos.CENTER);
        signInPane.setVgap(10);
        signInPane.setHgap(5);

        Label emailLabel = new Label("Email");
        TextField emailText = new TextField("");
        Label passwordLabel = new Label("Password");
        PasswordField passwordText = new PasswordField();
        
        Button signInButton = new Button("Sign In");
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = new User(emailText.getText(), passwordText.getText());
                Message message = new Message(Message.ClientMessage.SIGNIN, user);
                User.currentUser = user;
                client.sendToServer(message);
                
                try { // TODO: get rid of thread sleep and figure out how to do this properly
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Label signInMessage = new Label(serverMessage);
                System.out.println(serverMessage);
                signInPane.add(signInMessage, 0, 5);
                GridPane.setHalignment(signInMessage, HPos.CENTER);

                if (serverMessage.equals("Login successful")) {
                    bidding();
                }
            }
        });

        passwordText.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                signInButton.fire();
            }
        });

        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login();
            }
        });

        signInPane.add(emailLabel, 0, 0);
        signInPane.add(emailText, 0, 1);
        signInPane.add(passwordLabel, 0, 2);
        signInPane.add(passwordText, 0, 3);
        signInPane.add(signInButton, 0, 4);
        GridPane.setHalignment(signInButton, HPos.RIGHT);
        signInPane.add(goBackButton, 0, 4);
        GridPane.setHalignment(goBackButton, HPos.LEFT);

        Scene scene = new Scene(signInPane);
        stage.setWidth(325);
        stage.setHeight(375);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public void signUp() {
        GridPane signUpPane = new GridPane();
        signUpPane.setAlignment(Pos.CENTER);
        signUpPane.setVgap(10);
        signUpPane.setHgap(5);

        Label nameLabel = new Label("Name");
        TextField nameText = new TextField("");
        Label emailLabel = new Label("Email");
        TextField emailText = new TextField("");
        Label passwordLabel = new Label("Password");
        PasswordField passwordText = new PasswordField();

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = new User(nameText.getText(), emailText.getText(), passwordText.getText());
                Message message = new Message(Message.ClientMessage.SIGNUP, user);
                User.currentUser = user;
                client.sendToServer(message);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Label signUpMessage = new Label(serverMessage);
                System.out.println(serverMessage);
                signUpPane.add(signUpMessage, 0, 7);
                GridPane.setHalignment(signUpMessage, HPos.CENTER);

                if (serverMessage.equals("Login successful")) {
                    bidding();
                }
            }
        });

        passwordText.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                signUpButton.fire();
            }
        });

        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login();
            }
        });

        signUpPane.add(nameLabel, 0, 0);
        signUpPane.add(nameText, 0, 1);
        signUpPane.add(emailLabel, 0, 2);
        signUpPane.add(emailText, 0, 3);
        signUpPane.add(passwordLabel, 0, 4);
        signUpPane.add(passwordText, 0, 5);
        signUpPane.add(signUpButton, 0, 6);
        GridPane.setHalignment(signUpButton, HPos.RIGHT);
        signUpPane.add(goBackButton, 0, 6);
        GridPane.setHalignment(goBackButton, HPos.LEFT);

        Scene scene = new Scene(signUpPane);
        stage.setWidth(325);
        stage.setHeight(375);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public void bidding() {
        GridPane biddingPane = new GridPane();
        biddingPane.getChildren().clear();
        biddingPane.setAlignment(Pos.CENTER);
        biddingPane.setVgap(10);
        biddingPane.setHgap(5);

        

    

        Scene scene = new Scene(biddingPane);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

}
