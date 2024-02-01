package org.project;

import java.io.IOException;


public interface Authentication {

    boolean usernameExists(String usernameInserted, String pathToUse);

   boolean passwordCorresponds(String usernameInserted, String passwordInserted, String pathToUse);

   void registerNewUser(String pathToUse, String username, String hashedPassword, String email, Double userCredit) throws IOException;

   boolean checkRegexMatch(String regex, String textToMatch);

    boolean usernameValidator (String username);

    boolean emailValidator (String username);

    boolean globalValidator (String username, String password, String hashedPassword, String email);

}