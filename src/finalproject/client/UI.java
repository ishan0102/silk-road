/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.client;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UI {
    public static void startClient(Stage stage) {
        stage.setTitle("eHills");
        GridPane login = new GridPane();
        Scene scene = new Scene(login);
        stage.setScene(scene);
        stage.show();
    }
}
