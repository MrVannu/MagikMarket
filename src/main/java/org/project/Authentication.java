package org.project;

public interface Authentication {

    boolean usernameExists(String usernameInserted, String pathToUse);

   boolean passwordCorresponds(String usernameInserted, String passwordInserted, String pathToUse);

}


// to be implemented: password recovery proccess