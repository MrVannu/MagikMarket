package org.project;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import org.mindrot.jbcrypt.BCrypt;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.project.controllers.LoginController;
import org.project.model.User;
import org.project.util.AlertField;
import org.project.view.WelcomePane;
import java.io.*;
import java.util.concurrent.CompletableFuture;
import org.project.util.LoadingDialog;

public class Main extends Application {
        
    @Override
    public void start(Stage primaryStage) {
        //User userRegistered = new User();
        LoginController loginController = new LoginController();


        // Defining the panes
        GridPane layoutLogin = new GridPane();
        GridPane layoutRegister = new GridPane();

        // Define the main labels and titles
        Label titleLogin = new Label("Login");
        Label titleRegister = new Label("Register");
        Label welcomeLabel = new Label("Welcome!");
        Font customFont = Font.font("Arial",18);
        welcomeLabel.setFont(customFont);
        welcomeLabel.setPadding(new Insets(20));

        titleLogin.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleRegister.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        // Define a separator
        Separator separator = new Separator(Orientation.HORIZONTAL); // Create a horizontal separator
        separator.setStyle("-fx-background-color: #5C3190;");
        separator.setMaxWidth(250); // Maximum width

        // Define a VBox to be the upper part of the layout
        VBox upperBox = new VBox(welcomeLabel, separator);
        upperBox.setAlignment(Pos.CENTER_LEFT);

        // Define labels
        Label usernameLabelLogin = new Label("Username:");
        Label passwordLabelLogin = new Label("Password:");
        Label usernameLabelRegister = new Label("Username:");
        Label passwordLabelRegister = new Label("Password:");
        Label emailLabelRegister = new Label("Email:");

        // Create a TextField for the username with a maximum length of 15 characters for the LoginScene
        TextField usernameFieldLogin = new TextField();
        usernameFieldLogin.setPromptText("Enter your username (max 15 characters)");
        usernameFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent entering more than 15 characters
            if (newValue.length() > 15) usernameFieldLogin.setText(oldValue);
        });

        // Create a TextField for the username with a maximum length of 15 characters for the RegisterScene
        TextField usernameFieldRegister = new TextField();
        usernameFieldRegister.setPromptText("Enter your username (max 15 characters)");
        usernameFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent entering more than 15 characters
            if (newValue.length() > 15) usernameFieldRegister.setText(oldValue);
        });

        // Create a PasswordField for the password with a maximum length of 15 characters of the LoginScene
        PasswordField passwordFieldLogin = new PasswordField();
        passwordFieldLogin.setPromptText("Enter your password (max 15 characters)");
        passwordFieldLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent entering more than 15 characters
            if (newValue.length() > 15) passwordFieldLogin.setText(oldValue);
        });

        // Create a PasswordField for the password with a maximum length of 15 characters of the RegisterScene
        TextField passwordFieldRegister = new PasswordField();
        passwordFieldRegister.setPromptText("Enter your password (max 15 characters)");
        passwordFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent entering more than 15 characters
            if (newValue.length() > 15) passwordFieldRegister.setText(oldValue);
        });

        // Create a emailField for the email with a maximum length of 30 characters in the RegisterScene
        TextField emailFieldRegister = new TextField();
        emailFieldRegister.setPromptText("Enter your email (max 30 characters)");
        emailFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            // Prevent entering more than 15 characters
            if (newValue.length() > 30) emailFieldRegister.setText(oldValue);
        });

        // Define main BorderPane
        GridPane main = new GridPane();
        main.add(upperBox, 0,0);
        main.add(layoutLogin, 0,1);
        main.setAlignment(Pos.CENTER);

        // Defining the LoginScene which is the FIRST SCENE
        Scene LoginScene = new Scene(main, 700, 400); // <- Dimension of the LoginScene

        // Button -> login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {

            // Beta features --> to be implemented in the future (problems with MacOs)
            //
            // This snippet of code ensures the user is connected to the Internet as the
            // working dynamics of the application are based on the data fetched from the stock
            // market in real time.
            //
//            if (!CheckInternetConnection.isInternetReachable()) {
//                String errorNoConnectionBody = """
//                You are not currently connected to the Internet.
//
//                 Please connect to a network to use this application!""";
//
//                String errorNoConnectionTitle = "CONNECTION ERROR";
//
//                AlertField.showErrorAlert(errorNoConnectionTitle, errorNoConnectionBody);
//            } else {

                // Show the loading dialog
                LoadingDialog.showLoadingAlert();

                String username = usernameFieldLogin.getText();
                String password = passwordFieldLogin.getText();

                // Perform the authentication task asynchronously
                CompletableFuture.runAsync(() -> {
                    if (loginController.usernameExists(username, loginController.getPathUserDB()) &&
                            loginController.usernameValidator(username) &&
                            loginController.passwordCorresponds(username, password, loginController.getPathUserDB())) {

                        Platform.runLater(() -> {
                            loginController.setUserRegistered(new User(username));
                            WelcomePane welcomePane = new WelcomePane(primaryStage, LoginScene, loginController.getUserRegistered());

                            primaryStage.setTitle("Start App");
                            primaryStage.setScene(welcomePane.getScene());

                            AlertField.resetField(usernameFieldLogin, passwordFieldLogin);
                        });
                    } else {
                        // The user does not exist
                        Platform.runLater(() -> {
                            AlertField.showErrorAlert("Authentication error", "User not found or incorrect credentials.");
                            AlertField.invalidField(usernameFieldLogin, passwordFieldLogin);
                        });
                    }
                });
//            }
        });

        // Button -> Register
        Button switchScenesRegister = new Button("Sign in");
        Button switchScenesLogin = new Button("Return to Login");
        Button registerButton = new Button("Register");

        // Adjusting layout of the login GridPane
        layoutLogin.setPadding(new Insets(20)); // Defining a uniform quantity of pixels between the components of the layout and its edge
        layoutLogin.setHgap(10); // Defining a gap between columns of the grid
        layoutLogin.setVgap(15); // Defining a gap between rows of the grid
        layoutLogin.setAlignment(Pos.CENTER); // Aligning the GridPane in the CENTER

        // Adding elements to the login GridPane
        layoutLogin.add(titleLogin, 0, 2);
        layoutLogin.add(usernameLabelLogin, 0, 3);
        layoutLogin.add(usernameFieldLogin, 1, 3);
        layoutLogin.add(passwordLabelLogin, 0, 4);
        layoutLogin.add(passwordFieldLogin, 1, 4);
        layoutLogin.add(loginButton, 0, 5);
        layoutLogin.add(switchScenesRegister, 1, 5);

        // Adjusting layout of the register GridPane
        layoutRegister.setPadding(new Insets(20));
        layoutRegister.setHgap(10);
        layoutRegister.setVgap(10);
        layoutRegister.setAlignment(Pos.CENTER);

        // Adding elements to the register GridPane
        layoutRegister.add(titleRegister, 0, 0, 2, 1);
        layoutRegister.add(usernameLabelRegister, 0, 1);
        layoutRegister.add(usernameFieldRegister, 1, 1);
        layoutRegister.add(passwordLabelRegister, 0, 2);
        layoutRegister.add(passwordFieldRegister, 1, 2);
        layoutRegister.add(emailLabelRegister, 0, 3);
        layoutRegister.add(emailFieldRegister,1,3);
        layoutRegister.add(registerButton, 0, 4);
        layoutRegister.add(switchScenesLogin,1,4);

        //Defining the RegisterScene and it's dimension
        Scene RegisterScene = new Scene(layoutRegister, 700, 400); // <---- Dimension of the RegisterScene

        // Options of window
        primaryStage.setTitle("Login Form");
        primaryStage.setScene(LoginScene);
        primaryStage.show();

        // Actions of the buttons
        switchScenesRegister.setOnAction(e -> {
            primaryStage.setTitle("Register Form");
            primaryStage.setScene(RegisterScene);
            AlertField.resetField(usernameFieldLogin,passwordFieldLogin);
        });
        switchScenesLogin.setOnAction(e->{
            primaryStage.setTitle("Login Form");
            primaryStage.setScene(LoginScene);
            AlertField.resetField(usernameFieldRegister,passwordFieldRegister,emailFieldRegister);
        });

        registerButton.setOnAction(e -> {// Hides the LoginScene and shows the RegisterScene
            String username = usernameFieldRegister.getText();
            String password = passwordFieldRegister.getText();
            String hashedPassword = BCrypt.hashpw(passwordFieldRegister.getText(), BCrypt.gensalt());  // Just to test
            String email = emailFieldRegister.getText();

            // Checking that the registration credential meet the requirements
            if(!(loginController.globalValidator(username, password, hashedPassword, email))){

                // If the registration was not successful then it will follow one of the 3 cases below
                System.out.println("Authentication failed");
                if(loginController.getCheckField(0)){ // If the username already exists then the field is red
                    AlertField.showErrorAlert("Username error","Username not inserted or use another username.");
                    AlertField.invalidField(usernameFieldRegister);
                    loginController.setCheckField(false, 0); // Resetting the check to false
                }
                if(loginController.getCheckField(1)){// If the password is empty then the field is red
                    AlertField.showErrorAlert("Password error","The password you inserted is not allowed or the field is empty.");
                    AlertField.invalidField(passwordFieldRegister);
                    loginController.setCheckField(false, 1); // Reset the check to false
                }
                if(loginController.getCheckField(2)){// If the email does not match the regex (so is not valid) then the field is red
                    AlertField.showErrorAlert("Email error","The email you inserted is not an email.");
                    AlertField.invalidField(emailFieldRegister);
                    loginController.setCheckField(false, 2); // Reset the check to false
                }
            } else { // If the registration was successful
                try {
                    loginController.registerNewUser(loginController.getPathUserDB(), username, hashedPassword, email, loginController.getInitialUserCredit()); // The credentials of the user are saved into the database
                    AlertField.resetField(usernameFieldRegister,passwordFieldRegister,emailFieldRegister); // Resetting the fields
                    AlertField.showSuccessAlert("Registration successful","Your registration was successful!");
                    primaryStage.setScene(LoginScene); // Changing scene to LoginScene
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}