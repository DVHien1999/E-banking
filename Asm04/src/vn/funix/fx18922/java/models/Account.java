package vn.funix.fx18922.java.models;

import java.io.IOException;
import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String customerID;
    private String accountNumber;
    private double balance;
    private boolean Premium=false;

    public Account(String customerID,String accountNumber, double balance) {
        if (balance>=50000){
            this.customerID=customerID;
            this.accountNumber = accountNumber;
            this.balance = balance;
            if(balance>=10_000_000) this.Premium=true;
        } else {
            System.out.println("so du tai khoan phai hon 50000");
        }
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        if(balance>=10_000_000) this.Premium=true;
    }


    public boolean isPremium() {
        return Premium;
    }
    public void accountInfo(){
        System.out.println(this.accountNumber+" |\t\t\t\t"+balance+"đ");
    }
    public void printAccountDetail(){
        System.out.println(this.accountNumber+"| \t\t\t\t\t\t"+String.format("%.0f",this.balance)+"đ");
    }

}
