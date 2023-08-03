package org.project;

import com.opencsv.exceptions.CsvValidationException;
import org.mindrot.jbcrypt.BCrypt;
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
import com.opencsv.CSVReader;

import java.io.*;

public class Main extends Application implements Authentication {

    // Paths to databases
    private final String pathUserDB = "src/main/resources/userDB.csv";
    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";
    private short dataToUpdateIndex = 0;


    // Authentication functionalities
    public boolean usernameExists(String usernameInserted, String pathToUse){
        try (CSVReader reader = new CSVReader(new FileReader(pathToUse))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(usernameInserted)) {
                    return true; // If found
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return false; // Not found
    }
    public boolean passwordCorresponds(String usernameInserted, String passwordInserted, String pathToUse){
        try (CSVReader reader = new CSVReader(new FileReader(pathToUse))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(usernameInserted) && BCrypt.checkpw(passwordInserted, nextLine[1])) {
                    return true; // If found
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return false; // Not found
    }
    public boolean registerNewUser(String pathToUse, String username, String hashedPassword, String email) throws IOException {

        if(usernameExists(username, pathToUse)) {
            System.out.println("Username already exists!");
            // Display an alert to tell the user to change their username

            return false;
        }

        else{
            try (FileWriter writer = new FileWriter(pathToUse)) {
                String toRecord = username + "," + hashedPassword + "," + email;
                writer.append(toRecord);

                writer.append("\n"); // Divides data rows to be read
            }

            return true;
        }
    }


    // Method to update database data history
    public void updateDataHistory(String pathToUse, String... strings) throws IOException {
        String toWrite = "";
        for (String item : strings) {
            toWrite=item+",";
        }

        try (FileWriter writer = new FileWriter(pathToUse)) {

            if(dataToUpdateIndex>=20) dataToUpdateIndex=0;
            else{
                // Writing items in the csv
                try (BufferedReader reader = new BufferedReader(new FileReader(pathToUse))) {
                    int localCounter = 0;

                    while((reader.readLine()) != null) {
                        ++localCounter;

                        if(localCounter==dataToUpdateIndex){
                            writer.write(toWrite);
                            break;
                        }
                    }
                }
                writer.append("\n"); // Splits the data rows
                ++dataToUpdateIndex;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Title
        Text title = new Text("Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Textfields
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        // Create a TextField for the username with a maximum length of 15 characters
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username (max 15 characters)");
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                usernameField.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Create a PasswordField for the password with a maximum length of 15 characters
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password (max 15 characters)");
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                passwordField.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Button -> login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String hashedPassword = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());  // Just to test

            if(usernameExists(username, pathUserDB) && passwordCorresponds(username, password, pathUserDB)) System.out.println("esiste");
            else System.out.println("non esiste");

            // Authentication logic

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("PasswordInHash: " + hashedPassword);

        });

        // Window layout
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
        APIData testObj = new APIData();
        /*
        testObj.fetchData(); // WARNING: this line requires API usage
        double resultOfTest = testObj.maxAge();
        System.out.println(resultOfTest);
        */

        launch(args);
    }

}