
package org.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String nickName;
    private String name;
    private  String surname;
    private String password;
    private Date birthDate;
    private String gender;
    private List<Product> cart;

    public User(String name, String nickName, String surname, String password, Date birthDate, String gender) {
        this.nickName = nickName;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender; 
        cart = new ArrayList<>();
    }
    public User(){
        cart = new ArrayList<>();
    }

    public boolean addToCart(Product p){
        return cart.add(p);
    }
    public boolean removeFromCart(Product p){
        return cart.add(p);
    }
    public Product getFromCart(){
        return cart.get(0);
    }
    public Product getFromCart(int index){
        return cart.get(index);
    }
    public boolean cartIsVoid(){
        if (cart == null)
            return true;
        return false;
    }

    public List<Product> getCart(){
        return cart;
    }

}
