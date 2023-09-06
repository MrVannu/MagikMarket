package org.project;


import com.opencsv.exceptions.CsvValidationException;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import org.mindrot.jbcrypt.BCrypt;
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
    private double userCredit = 1000;
    private User userRegistered = new User("", "", "", "", "");;

    private boolean checkField[];



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
    public boolean registerNewUser(String pathToUse, String username, String hashedPassword, String email, Double credit) throws IOException {

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
            String[] toRecord = {username, hashedPassword, email, String.valueOf(credit)};  // Compose the string to be saved
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
        return !username.contains(" ") && (!username.equals(""));
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
        checkField = new boolean[3];
        if(usernameValidator(username) && emailValidator(email) && passwordValidator(password)) {
            userRegistered = new User (username, password, hashedPassword, email, String.valueOf(userCredit));
            return true;
        }
        else if (!usernameValidator(username)){
            System.out.println("Username not allowed");
            checkField[0]=true;

        } else if (!passwordValidator(password)){
            System.out.println("Password not allowed");
            checkField[1]=true;

        }else if (!emailValidator(email)){
            System.out.println("Email not allowed");
            checkField[2]=true;

        }
        return false;
    }


    // Method to modify the userCredit value into the UserDB
    public void setUserCredit(Double valueToSet){

    }


    @Override
    public void start(Stage primaryStage) {
        //User userRegistered = new User();

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

        // Create a emailField for the email with a maximum length of 30 characters in the RegisterScene
        TextField emailFieldRegister = new TextField();
        emailFieldRegister.setPromptText("Enter your email (max 30 characters)");
        emailFieldRegister.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 30) {
                emailFieldRegister.setText(oldValue); // Prevent entering more than 15 characters
            }

        });

        //Defining the LoginScene which is the FIRST SCENE
        Scene LoginScene = new Scene(layoutLogin, 700, 400); // <---- Dimention of the LoginPane


        // Button -> login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameFieldLogin.getText();
            String password = passwordFieldLogin.getText();
            String hashedPassword = BCrypt.hashpw(passwordFieldLogin.getText(), BCrypt.gensalt());  // Just to test


            if(usernameExists(username, pathUserDB) && usernameValidator(username) && passwordCorresponds(username, password, pathUserDB)) {
                //The user exists and the stage changes

                WelcomePane welcomePane = new WelcomePane(primaryStage, LoginScene, userRegistered);

                primaryStage.setTitle("Start App");
                primaryStage.setScene(welcomePane.getScene());
                System.out.println("Exists");
                AlertField.resetField(usernameFieldLogin,passwordFieldLogin);
            }
            else {
                // The user does not exist
                System.out.println("Does not exist");
                AlertField.showAlert("Autentication error", "User not found or incorrect credentials.");
                AlertField.invalidField(usernameFieldLogin, passwordFieldLogin);
            }

            // Authentication logic
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            System.out.println("PasswordInHash: " + hashedPassword);
        });

        // Button -> Register
        Button switchScenesRegister = new Button("Sign in");
        Button switchScenesLogin = new Button("Return to Login");
        Button registerButton = new Button("Register");

        //Temporary button for GUI test
        Button gui = new Button("GUI");
        gui.setOnAction(e->{
            WelcomePane welcomePane = new WelcomePane(primaryStage, LoginScene, userRegistered);
            primaryStage.setTitle("Start App");
            primaryStage.setScene(welcomePane.getScene());
        });

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
        layoutLogin.add(gui, 2, 4);

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
            if(!(globalValidator(username, password, hashedPassword, email))){

                // If the registration was not successful then it will follow one of the 3 cases below
                System.out.println("globalValidator failed");
                if(checkField[0]==true){ // If the username already exists then the field is red
                    AlertField.showAlert("Username error","Use another username.");
                    AlertField.invalidField(usernameFieldRegister);
                    checkField[0]=false; // Resetting the check to false
                }
                if(checkField[1]==true){// If the password is empty then the field is red
                    AlertField.showAlert("Password error","The password you inserted is not allowed or the field is empty.");
                    AlertField.invalidField(passwordFieldRegister);
                    checkField[1]=false; // Resetting the check to false
                }
                if(checkField[2]==true){// If the email does not match the regex (so is not valid) then the field is red
                    AlertField.showAlert("Email error","The email you inserted is not an email.");
                    AlertField.invalidField(emailFieldRegister);
                    checkField[2]=false; // Resetting the check to false
                }

            } else {
                // If the registration was successful
                try {

                    registerNewUser(pathUserDB, username, hashedPassword, email, userCredit); // The credentials of the user are saved into the database
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
        //APIData testObj = new APIData();
        /*
        testObj.fetchData(); // WARNING: this line requires API usage
        double resultOfTest = testObj.maxAge();
        System.out.println(resultOfTest);
        */

        launch(args);
    }

}