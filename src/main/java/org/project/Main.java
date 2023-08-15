package org.project;


import com.opencsv.exceptions.CsvValidationException;
import javafx.scene.control.PasswordField;
import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;


public class Main extends Application implements Authentication {

    // Paths to databases (CSV files)
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking
    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";  // Path to DB for data history

    // Index of row to be overwritten (most remote in the db)
    private short dataToUpdateIndex = 0;



    // Authentication functionalities
    // Checks if username already exists
    public boolean usernameExists(String usernameInserted, String pathToUse){
        try (CSVReader reader = new CSVReader(new FileReader(pathToUse))) {
            String[] nextLine;  // Stores what is contained in the row

            while ((nextLine = reader.readNext()) != null) {  // Splits by comma
                if (nextLine[0].equals(usernameInserted)) {
                    return true; // If found
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return false; // If not found
    }

    // Checks if password is correct
    public boolean passwordCorresponds(String usernameInserted, String passwordInserted, String pathToUse){
        try (CSVReader reader = new CSVReader(new FileReader(pathToUse))) {
            String[] nextLine;  // Stores what is contained in the row

            while ((nextLine = reader.readNext()) != null) {
                // If username is related to the password inserted
                if (nextLine[0].equals(usernameInserted) && BCrypt.checkpw(passwordInserted, nextLine[1])) {
                    return true; // If it does correspond
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return false; // If it does not correspond
    }

    // Creates a new account
    public boolean registerNewUser(String pathToUse, String username, String hashedPassword, String email) throws IOException {

        if(usernameExists(username, pathToUse)) return false;  // Check no user with alias exist

        try (CSVReader reader = new CSVReader(new FileReader(pathToUse))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 0) break;  // Breaks if the line is empty (end of file)
            }
        } catch (Exception e) {  // Exception occured while reading
            System.out.println("Exception occurred");
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(pathToUse, true))) {
            String[] toRecord = {username, hashedPassword, email};  // Compose the string to be saved
            writer.writeNext(toRecord);  // Writes the string composed

            return true;
        }
    }



    // Register process validation
    // Regex validation
    public boolean checkRegexMatch(String regex, String textToMatch) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textToMatch);
        return matcher.matches();
    }

    //Validator for username (no spaces allowed)
    public boolean usernameValidator (String username){
        return !username.contains(" ");
    }

    // Validator for email fomrat (example@hello.world)
    public boolean emailValidator (String email){
        String regex = "^[A-Za-z0-9._-]+@[A-Za-z0-9-]+\\.[A-Za-z]+$";
        return checkRegexMatch(regex, email);
    }

    // Validator for password (cannot be empty)
    public boolean passwordValidator (String password){
        return !password.isEmpty();
    }

    // Global validator
    public boolean globalValidator (String username, String password, String hashedPassword, String email){
        if(usernameValidator(username) && emailValidator(email) && passwordValidator(password)) return true;
        else if (!usernameValidator(username)){
            System.out.println("Username not allowed");
            // Scene handling by Enri

        } else if (!passwordValidator(password)){
            System.out.println("Password not allowed");
            // Scene handling by Enri

        }else if (!emailValidator(email)){
            System.out.println("Email not allowed");
            // Scene handling by Enri

        }
        return false;
    }







    @Override
    public void start(Stage primaryStage) {

        // Defining the panes
        GridPane layoutLogin = new GridPane();
        GridPane layoutRegister = new GridPane();

        // Title
        Text titleLogin = new Text("Login");
        Text titleRegister = new Text("Register");
        titleLogin.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleRegister.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Labels
        Label usernameLabelLogin = new Label("Username:");
        Label passwordLabelLogin = new Label("Password:");
        Label usernameLabelRegister = new Label("Username:");
        Label passwordLabelRegister = new Label("Password:");
        Label emailLabelRegister = new Label("Email:");

        // Create a TextField for the username with a maximum length of 15 characters for the LoginScene
        TextField usernameFieldLogin = new TextField();
        usernameFieldLogin.setPromptText("Enter your username (max 15 characters)");
        usernameFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                usernameFieldLogin.setText(oldValue); // Prevent entering more than 15 characters
            }
        });
        // Create a TextField for the username with a maximum length of 15 characters for the RegisterScene
        TextField usernameFieldRegister = new TextField();
        usernameFieldRegister.setPromptText("Enter your username (max 15 characters)");
        usernameFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                usernameFieldRegister.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Create a PasswordField for the password with a maximum length of 15 characters of the LoginScene
        PasswordField passwordFieldLogin = new PasswordField();
        passwordFieldLogin.setPromptText("Enter your password (max 15 characters)");
        passwordFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                passwordFieldLogin.setText(oldValue); // Prevent entering more than 15 characters
            }
        });

        // Create a PasswordField for the password with a maximum length of 15 characters of the RegisterScene
        TextField passwordFieldRegister = new PasswordField();
        passwordFieldRegister.setPromptText("Enter your password (max 15 characters)");
        passwordFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 15) {
                passwordFieldRegister.setText(oldValue); // Prevent entering more than 15 characters
            }
        });


        TextField emailFieldRegister = new TextField();
        emailFieldRegister.setPromptText("Enter your email (max 30 characters)");
        emailFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                emailFieldRegister.setText(oldValue); // Prevent entering more than 15 characters
            }

        });


        Scene LoginScene = new Scene(layoutLogin, 500, 300);
        // Button -> login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameFieldLogin.getText();
            String password = passwordFieldLogin.getText();
            String hashedPassword = BCrypt.hashpw(passwordFieldLogin.getText(), BCrypt.gensalt());  // Just to test

            if(usernameExists(username, pathUserDB) && passwordCorresponds(username, password, pathUserDB)) {
                //switc
                WelcomePane welcomePane = new WelcomePane(primaryStage, LoginScene);
                primaryStage.setTitle("Start App");
                primaryStage.setScene(welcomePane.getScene());
                System.out.println("esiste");
            }
            else System.out.println("non esiste");

            // Authentication logic

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("PasswordInHash: " + hashedPassword);


        });

        // Button -> Register
        Button switchScenesRegister = new Button("Switch to Register");
        Button switchScenesLogin = new Button("Switch to Login");
        Button registerButton = new Button("Register");

        // Login scene
        layoutLogin.setPadding(new Insets(20));
        layoutLogin.setHgap(10);
        layoutLogin.setVgap(10);

        layoutLogin.add(titleLogin, 0, 0, 2, 1);
        layoutLogin.add(usernameLabelLogin, 0, 1);
        layoutLogin.add(usernameFieldLogin, 1, 1);
        layoutLogin.add(passwordLabelLogin, 0, 2);
        layoutLogin.add(passwordFieldLogin, 1, 2);
        layoutLogin.add(loginButton, 0, 3);
        layoutLogin.add(switchScenesRegister, 1, 3);

        //Scene LoginScene = new Scene(layoutLogin, 500, 300);

        // Register scene
        layoutRegister.setPadding(new Insets(20));
        layoutRegister.setHgap(10);
        layoutRegister.setVgap(10);
        layoutRegister.add(titleRegister, 0, 0, 2, 1);
        layoutRegister.add(usernameLabelRegister, 0, 1);
        layoutRegister.add(usernameFieldRegister, 1, 1);
        layoutRegister.add(passwordLabelRegister, 0, 2);
        layoutRegister.add(passwordFieldRegister, 1, 2);
        layoutRegister.add(emailLabelRegister, 0, 3);
        layoutRegister.add(emailFieldRegister,1,3);
        layoutRegister.add(registerButton, 3, 4);
        layoutRegister.add(switchScenesLogin,4,4);

        Scene RegisterScene = new Scene(layoutRegister, 500, 300);

        // Options of window
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(LoginScene);
        primaryStage.show();

        // Actions of the buttons
        switchScenesRegister.setOnAction(e -> {
            primaryStage.setTitle("Register Form");
            primaryStage.setScene(RegisterScene);
        });
        switchScenesLogin.setOnAction(e->{
            primaryStage.setTitle("Login Form");
            primaryStage.setScene(LoginScene);
        });

        registerButton.setOnAction(e -> {// Hides the LoginScene and shows the RegisterScene

            String username = usernameFieldRegister.getText();
            String password = passwordFieldRegister.getText();
            String hashedPassword = BCrypt.hashpw(passwordFieldRegister.getText(), BCrypt.gensalt());  // Just to test
            String email = emailFieldRegister.getText();

            if(!(globalValidator(username, password, hashedPassword, email))){
                System.out.println("globalValidator failed");
            } else {
                try {
                    registerNewUser(pathUserDB, username, hashedPassword, email);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });



    }



    public static void main(String[] args) {
        //APIData testObj = new APIData();
        /*
        testObj.fetchData(); // WARNING: this line requires API usage
        double resultOfTest = testObj.maxAge();
        System.out.println(resultOfTest);
        */

        launch(args);
    }

}