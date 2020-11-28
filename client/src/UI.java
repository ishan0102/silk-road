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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UI {
    private Stage stage;
    private Client client;

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
        signInPane.setVgap(12);
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
                Message message = new Message(Message.Type.SIGNIN, user);
                client.sendToServer(message);
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
        signInPane.add(emailText, 1, 0);
        signInPane.add(passwordLabel, 0, 1);
        signInPane.add(passwordText, 1, 1);
        signInPane.add(signInButton, 1, 2);
        GridPane.setHalignment(signInButton, HPos.RIGHT);
        signInPane.add(goBackButton, 1, 3);
        GridPane.setHalignment(goBackButton, HPos.RIGHT);

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
        signUpPane.setVgap(12);
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
                Message message = new Message(Message.Type.SIGNUP, user);
                client.sendToServer(message);
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
        signUpPane.add(nameText, 1, 0);
        signUpPane.add(emailLabel, 0, 1);
        signUpPane.add(emailText, 1, 1);
        signUpPane.add(passwordLabel, 0, 2);
        signUpPane.add(passwordText, 1, 2);
        signUpPane.add(signUpButton, 1, 3);
        GridPane.setHalignment(signUpButton, HPos.RIGHT);
        signUpPane.add(goBackButton, 1, 4);
        GridPane.setHalignment(goBackButton, HPos.RIGHT);

        Scene scene = new Scene(signUpPane);
        stage.setWidth(325);
        stage.setHeight(375);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

}
