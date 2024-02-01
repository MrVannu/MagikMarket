package org.project;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private final String pathUserDB = "src/main/resources/userDB.csv";  // Path to DB for users tracking


    public User() {}
    public User(String username) {
        this.username = username;
    }



    public String getUsername() {
        return username;
    }

    // Get the userCredit value into the UserDB
    public String getUserCredit() {
        try (CSVReader reader = new CSVReader(new FileReader(pathUserDB))) {
            String[] nextLine;  // Stores what is contained in the row

            while ((nextLine = reader.readNext()) != null) {
                // If username is related to the password inserted
                if (nextLine[0].equals(username)) return nextLine[3];
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("User not found into the DB");
    }

    public void setUserCredit(Double valueToSet) {
        // In case of user has not enough credit
        if(valueToSet < 0) throw new NoCreditException();

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