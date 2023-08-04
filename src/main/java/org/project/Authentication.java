package org.project;

import java.io.FileWriter;
import java.io.IOException;

public interface Authentication {

    boolean usernameExists(String usernameInserted, String pathToUse);

   boolean passwordCorresponds(String usernameInserted, String passwordInserted, String pathToUse);

   boolean registerNewUser(String pathToUse, String username, String hashedPassword, String email) throws IOException;

   boolean checkRegexMatch(String regex, String textToMatch);

    boolean usernameValidator (String username);

    boolean emailValidator (String username);

    boolean globalValidator (String username, String password, String hashedPassword, String email);

}


// to be implemented: password recovery proccess