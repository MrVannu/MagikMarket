package org.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Textfields
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        // Button - login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Authentication logic
            // Db

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        });

        // Layout della finestra
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20));
        layout.setHgap(10);
        layout.setVgap(10);

        layout.add(title, 0, 0, 2, 1);
        layout.add(usernameLabel, 0, 1);
        layout.add(usernameField, 1, 1);
        layout.add(passwordLabel, 0, 2);
        layout.add(passwordField, 1, 2);
        layout.add(loginButton, 1, 3);

        Scene scene = new Scene(layout, 300, 200);

        // Options of window
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //APIData testObj = new APIData();
        //testObj.fetchData();
        //double result = testObj.maxAge();
        //System.out.println(result);
        launch(args);
    }
}


