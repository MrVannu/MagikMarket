package org.project.controllers;

import java.net.InetAddress;


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
