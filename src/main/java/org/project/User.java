
package org.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private List<Stock> cart;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        cart = new ArrayList<>();
    }


    public boolean addToCart(Stock p){
        return cart.add(p);
    }
    public boolean removeFromCart(Stock p){
        return cart.add(p);
    }
    public Stock getFromCart(){
        return cart.get(0);
    }
    public Stock getFromCart(int index){
        return cart.get(index);
    }
    public boolean cartIsVoid(){
        if (cart == null)
            return true;
        return false;
    }

    public List<Stock> getCart(){
        return cart;
    }

}
