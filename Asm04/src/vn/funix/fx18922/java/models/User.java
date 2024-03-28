package vn.funix.fx18922.java.models;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String customerId;
    public User(String name, String customerId) {
        this.name = name;
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
