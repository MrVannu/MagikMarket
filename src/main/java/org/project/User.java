
package org.project;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String hashedPassword;
    private String email;
    private double UserCredit;
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking


    public User() {}

    public User(String username, String password, String hashedPassword, String email, String userCredit) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    // Method to modify the userCredit value into the UserDB
    public String getUserCredit() {
        try (CSVReader reader = new CSVReader(new FileReader(pathUserDB))) {
            String[] nextLine;  // Stores what is contained in the row

            while ((nextLine = reader.readNext()) != null) {
                // If username is related to the password inserted
                if (nextLine[0].equals(username)) {
                    double userCredit = Double.parseDouble(nextLine[3]);
                    if (userCredit < 0) {
                        return "CREDITO ESAURITO";
                    } else {
                        return String.valueOf(userCredit);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        // If not found or other errors occur
        return "CREDITO ESAURITO";     }



    public void setUserCredit(Double valueToSet) {
        // Update the class parameter
        UserCredit = valueToSet;

        // Update the database parameter
        try {
            List<String[]> lines = new ArrayList<>();
            boolean updated = false; // Flag for tracking if edited

            try (CSVReader reader = new CSVReader(new FileReader(pathUserDB))) {
                String[] nextLine;

                while ((nextLine = reader.readNext()) != null) {
                    // If username is related to the password inserted
                    if (nextLine[0].equals(username)) {
                        nextLine[3] = String.valueOf(valueToSet);
                        updated = true; // flag modified
                    }
                    lines.add(nextLine);
                }
            }

            // Overwrite the file only if the line has been updated
            if (updated) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(pathUserDB))) {
                    writer.writeAll(lines);
                }
            }
        } catch (IOException | CsvValidationException ex) {
            ex.printStackTrace();
        }
    }

}