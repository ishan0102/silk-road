/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
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
        stage.setTitle("The Silk Road");
        login();
    }

    /**
     * Busy wait for a server response
     */
    public void waitForResponse() {
        while (!Client.messageReceived) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Client.messageReceived = false;
    }

    /**
     * Login screen
     */
    public void login() {
        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setVgap(12);

        Label welcome = new Label("The Silk Road");
        welcome.setFont(Font.font("Times New Roman", FontPosture.ITALIC, 30));

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

        loginPane.add(welcome, 0, 0);
        GridPane.setHalignment(welcome, HPos.CENTER);
        loginPane.add(signIn, 0, 1);
        GridPane.setHalignment(signIn, HPos.CENTER);
        loginPane.add(signUp, 0, 2);
        GridPane.setHalignment(signUp, HPos.CENTER);

        Scene scene = new Scene(loginPane);
        stage.setWidth(325);
        stage.setHeight(375);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sign in screen
     */
    public void signIn() {
        GridPane signInPane = new GridPane();
        signInPane.setAlignment(Pos.CENTER);
        signInPane.setVgap(10);
        signInPane.setHgap(5);

        Label emailLabel = new Label("Email");
        TextField emailText = new TextField("");
        Label passwordLabel = new Label("Password");
        PasswordField passwordText = new PasswordField();
        Label signInMessage = new Label();

        Button signInButton = new Button("Sign In");
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = new User(emailText.getText(), passwordText.getText());
                Message message = new Message(Message.ClientMessage.SIGNIN, user);
                User.currentUser = user;
                client.sendToServer(message);
                waitForResponse();

                signInMessage.setText(serverMessage);
                signInMessage.setTextFill(Color.rgb(220, 20, 60));
                signInPane.getChildren().remove(signInMessage);
                signInPane.add(signInMessage, 0, 5);
                GridPane.setHalignment(signInMessage, HPos.CENTER);

                if (serverMessage.equals("Login successful")) {
                    try {
                        if (user.email.equals(User.currentUser.email)) {
                            biddingScreen();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
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

    /**
     * Sign up screen
     */
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
        Label signUpMessage = new Label();

        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = new User(nameText.getText(), emailText.getText(), passwordText.getText());
                Message message = new Message(Message.ClientMessage.SIGNUP, user);
                User.currentUser = user;
                client.sendToServer(message);
                waitForResponse();

                signUpMessage.setText(serverMessage);
                Label signUpMessage = new Label(serverMessage);
                signUpMessage.setTextFill(Color.rgb(220, 20, 60));
                signUpPane.getChildren().remove(signUpMessage);
                signUpPane.add(signUpMessage, 0, 7);
                GridPane.setHalignment(signUpMessage, HPos.CENTER);

                if (serverMessage.equals("Login successful")) {
                    biddingScreen();
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

    public TabPane tabPane;
    
    /**
     * Main bidding screen with home, add item, and individual item tabs
     */
    public void biddingScreen() {
        paneList = new HashMap<String, GridPane>();
        tabList = new HashMap<String, Tab>();
        tabPane = new TabPane();

        client.sendToServer(new Message(Message.ClientMessage.GET_ITEM_INFO));
        waitForResponse();

        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        // Home tab
        Tab home = new Tab("Home", new Label("Welcome to The Silk Road!"));
        GridPane homePane = new GridPane();
        homePane.getChildren().clear();
        homePane.setAlignment(Pos.CENTER);
        homePane.setVgap(10);
        homePane.setHgap(5);

        User user = User.currentUser;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale( Locale.US )
                        .withZone( ZoneId.systemDefault() );
        String datetime = formatter.format(user.lastVisit);
        String[] dt = datetime.split(" ");
        Label welcome = new Label("Welcome to The Silk Road " + user.name + 
                    "!\nYour last visit was on " + dt[0] + " at " + dt[1] + " " + dt[2] + ".");
        welcome.setFont(new Font("Times New Roman", 40));
        
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login();
            }
        });
        
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        homePane.add(welcome, 0, 0);
        GridPane.setHalignment(welcome, HPos.CENTER);
        GridPane.setValignment(welcome, VPos.TOP);
        homePane.add(signOutButton, 0, 1);
        GridPane.setHalignment(signOutButton, HPos.CENTER);
        homePane.add(quitButton, 0, 2);
        GridPane.setHalignment(quitButton, HPos.CENTER);

        home.setContent(homePane);
        tabPane.getTabs().add(home);

        // Add item tab
        Tab addItem = new Tab("Add Item", new Label("Add a new item to the auction"));
        GridPane addItemPane = new GridPane();
        addItemPane.getChildren().clear();
        addItemPane.setAlignment(Pos.CENTER);
        addItemPane.setVgap(10);
        addItemPane.setHgap(5);

        Label addNameLabel = new Label("Item Name");
        TextField addNameText = new TextField("");
        addNameText.setPromptText("PlayStation 5");
        Label addDescriptionLabel = new Label("Description");
        TextArea addDescriptionText = new TextArea("");
        addDescriptionText.setPrefRowCount(4);
        addDescriptionText.setPrefWidth(300);
        addDescriptionText.setWrapText(true);
        addDescriptionText.setPromptText("Elite gaming console");
        Label addBidLabel = new Label("Starting Bid Price");
        TextField addBidText = new TextField("");
        addBidText.setPromptText("500.00");
        Label addBuyLabel = new Label("Automatic Buy Price");
        TextField addBuyText = new TextField();
        addBuyText.setPromptText("1000.00");

        Button addItemButton = new Button("Add Item");
        Label addItemMessage = new Label();
        addItemButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Item item = new Item(addNameText.getText(), addDescriptionText.getText(), addBidText.getText(),
                addBuyText.getText(), User.currentUser.email);
                Message message = new Message(Message.ClientMessage.ADD_ITEM, item);
                client.sendToServer(message);

                System.out.println(serverMessage);
                System.out.println(Client.messageReceived);

                waitForResponse();

                addItemMessage.setText("");
                addItemMessage.setText(serverMessage);
                if (serverMessage.equals("Item added successfully!")) {
                    addItemMessage.setTextFill(Color.rgb(0, 100, 0));
                    addNameText.clear();
                    addDescriptionText.clear();
                    addBidText.clear();
                    addBuyText.clear();
                } else {
                    addItemMessage.setTextFill(Color.rgb(220, 20, 60));
                }
                addItemPane.getChildren().remove(addItemMessage);
                addItemPane.add(addItemMessage, 0, 9);
                GridPane.setHalignment(addItemMessage, HPos.CENTER);

                if (serverMessage.equals("Item added successfully!")) {
                    client.sendToServer(new Message(Message.ClientMessage.GET_NEW_ADDITION, item));
                    waitForResponse();
                }
            }
        });

        addBuyText.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addItemButton.fire();
            }
        });

        addItemPane.add(addNameLabel, 0, 0);
        addItemPane.add(addNameText, 0, 1);
        addItemPane.add(addDescriptionLabel, 0, 2);
        addItemPane.add(addDescriptionText, 0, 3);
        addItemPane.add(addBidLabel, 0, 4);
        addItemPane.add(addBidText, 0, 5);
        addItemPane.add(addBuyLabel, 0, 6);
        addItemPane.add(addBuyText, 0, 7);
        addItemPane.add(addItemButton, 0, 8);
        GridPane.setHalignment(addItemButton, HPos.CENTER);

        addItem.setContent(addItemPane);
        tabPane.getTabs().add(addItem);
        
        // Tab for every item in the auction
        HashMap<String, Item> items = Item.itemInfo;
        for (Item item : items.values()) {
            Tab itemTab = addItemTab(item);
            updateBidInfo(item, "");
            tabPane.getTabs().add(itemTab);
        }
 
        Scene scene = new Scene(tabPane);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public HashMap<String, GridPane> paneList;
    public HashMap<String, Tab> tabList;

    /**
     * Add a new tab for an item
     * @param item item tab to be created
     * @return new Tab
     */
    public Tab addItemTab(Item item) {
        Tab itemTab = new Tab(item.getName(), new Label(item.getDescription()));
        GridPane itemPane = new GridPane();
        itemPane.getChildren().clear();
        itemPane.setAlignment(Pos.CENTER);
        itemPane.setVgap(10);
        itemPane.setHgap(5);
        
        paneList.put(item.getName(), itemPane);
        tabList.put(item.getName(), itemTab);
        return itemTab;
    }

    /**
     * Update all bidding information for clients
     * @param item item to be updated
     * @param status new status message
     */
    public void updateBidInfo(Item item, String status) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GridPane itemPane = paneList.get(item.getName());
                Tab itemTab = tabList.get(item.getName());
                itemPane.getChildren().clear();

                Label welcome = new Label("The Silk Road");
                welcome.setFont(Font.font("Times New Roman", FontPosture.ITALIC, 40));

                Label name = new Label(item.getName());
                name.setFont(new Font("Calibri", 30));
                Label description = new Label("Description: " + item.getDescription());
                description.setFont(new Font("Calibri", 20));
                Label bidPrice = new Label("");
                bidPrice.setText("Bid Price: $" + item.getBidPrice());
                bidPrice.setFont(new Font("Calibri", 20));
                TextField addBid = new TextField();
                addBid.setPromptText("Bid on this item");
                
                Button sendBid = new Button("Send Bid");
                sendBid.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        User user = User.currentUser;
                        item.setBidPrice(addBid.getText());
                        Message message = new Message(Message.ClientMessage.SEND_BID, item, user);
                        client.sendToServer(message);
                    }
                });

                addBid.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        sendBid.fire();
                    }
                });
                
                Label sendBidMessage = new Label("");
                sendBidMessage.setFont(new Font("Calibri", 18));
                sendBidMessage.setText(status);
                if (status.contains("has won this auction")) {
                    sendBidMessage.setTextFill(Color.rgb(0, 100, 0));
                } else if (status.contains("Bid has been updated")) {
                    sendBidMessage.setTextFill(Color.PURPLE);
                } else {
                    sendBidMessage.setTextFill(Color.rgb(220, 20, 60));
                }
                
                itemPane.add(welcome, 0, 0);
                GridPane.setHalignment(welcome, HPos.CENTER);
                GridPane.setValignment(welcome, VPos.TOP);
                itemPane.add(name, 0, 1);
                GridPane.setHalignment(name, HPos.CENTER);
                itemPane.add(description, 0, 2);
                itemPane.add(bidPrice, 0, 3);
                itemPane.add(addBid, 0, 4);
                itemPane.add(sendBid, 0, 5);
                GridPane.setHalignment(sendBid, HPos.CENTER);
                itemPane.add(sendBidMessage, 0, 6);
                GridPane.setHalignment(sendBidMessage, HPos.CENTER);
                itemTab.setContent(itemPane);
            }
        });
    }

}