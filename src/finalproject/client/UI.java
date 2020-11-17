/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UI {
    public Stage stage;

    public UI(Stage stage) {
        this.stage = stage;
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

        Text text = new Text("Sign In Window");

        Button goBack = new Button("Go Back");
        goBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login();
            }
        });

        signInPane.add(text, 0, 0);
        GridPane.setHalignment(text, HPos.CENTER);
        signInPane.add(goBack, 0, 1);
        GridPane.setHalignment(goBack, HPos.CENTER);

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

        Text text = new Text("Sign Up Window");

        Button goBack = new Button("Go Back");
        goBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login();
            }
        });

        signUpPane.add(text, 0, 0);
        GridPane.setHalignment(text, HPos.CENTER);
        signUpPane.add(goBack, 0, 1);
        GridPane.setHalignment(goBack, HPos.CENTER);

        Scene scene = new Scene(signUpPane);
        stage.setWidth(325);
        stage.setHeight(375);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

}
