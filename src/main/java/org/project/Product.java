package org.project;

public class Product {
    private double price;
    private String name;
    private String id;
    private String description;
    private boolean available;

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isAvailable() {
        return available;
    }
    /*private double discount(){
        return 0.0;
    }*/
    public boolean equals(Product obj) {
        return this.getId().equals(obj.getId());
    }
}
