package org.project;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginControl implements Authentication{
    private final String pathDataHistoryDB = "src/main/resources/dataHistoryDB.csv";  // Path to DB for data history
    // Paths to databases (CSV files)
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking

    private final double initialUserCredit = 1000;

    // Index of row to be overwritten (most remote in the db)
    private short dataToUpdateIndex = 0;

    private User userRegistered = new User("", "", "", "", -101.0);;

    private boolean[] checkField;


    public String getPathUserDB() {
        return pathUserDB;
    }

    public void setUserRegistered(User userRegistered) {
        this.userRegistered = userRegistered;
    }

    public User getUserRegistered() {
        return userRegistered;
    }

    public boolean getCheckField(int index) {
        return checkField[index];
    }

    public void setCheckField(boolean checkField, int index) {
        this.checkField[index] = checkField;
    }

    public double getInitialUserCredit() {
        return initialUserCredit;
    }

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
    public void registerNewUser(String pathToUse, String username, String hashedPassword, String email, Double credit) throws IOException {

        if(usernameExists(username, pathToUse)) return;  // Check no user with alias exist

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
        return !username.contains(" ") && (!username.isEmpty());
    }

    // Validator for email format (example@hello.world)
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
        if(usernameValidator(username) && emailValidator(email) && passwordValidator(password)) return true;

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

}
