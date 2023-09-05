
package org.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String hashedPassword;
    private String email;
    private double amount = 1000;


    public User(){}

    public User(String username, String password, String hashedPassword, String email, String userCredit) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double newValue){
        amount = newValue;
    }
}
