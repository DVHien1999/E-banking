package vn.funix.fx18922.java.models;

import java.io.Serializable;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String accountNumber;
    private double amount;
    private String time;
    private boolean status;
    private String type;

    public Transaction(String accountNumber, double amount, String time, boolean status,String type) {
        this.id = String.valueOf(UUID.randomUUID());
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.time = time;
        this.status=status;
        this.type=type;
    }
    public void printTransaction(){
        String strTransaction=String.format("[GD]%8s |%12s |%-17.0fđ| %13s",this.accountNumber,this.type,this.amount,this.time);
        System.out.println(strTransaction);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getId() {
        return id;
    }
}
