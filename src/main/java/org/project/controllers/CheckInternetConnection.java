package org.project.controllers;

import java.net.InetAddress;

/*
This class is never used in the current version (3rd Feb 2024) since some problems occurred in the accesses
management on MacOs. Please see the Main.java at "[...] loginButton.setOnAction() [...]" for more information
 */
public class CheckInternetConnection {
    public static boolean isInternetReachable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(40000);
        } catch (Exception e) {
            return false;
        }
    }
}
