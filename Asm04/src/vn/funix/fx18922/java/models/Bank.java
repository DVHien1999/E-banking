package vn.funix.fx18922.java.models;

import vn.funix.fx18922.java.dao.CustomerDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bank {
    private static BinaryFileService<Customer> bankBinaryFileService = new BinaryFileService<>();
    private final String id;
    private final List<Customer> customers;

    public Bank(){
        this.id = String.valueOf(UUID.randomUUID());
        this.customers = new ArrayList<Customer>();
    }
    public Bank(String id) {
        this.id = id;
        this.customers = new ArrayList<Customer>();
    }

    public String getId() {
        return id;
    }
    public void addCustomer(Customer newCustomer){
        customers.add(newCustomer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public boolean isCustomerExist(String customerID){
        return CustomerDao.list().stream().anyMatch(customer -> customer.getCustomerId().equals(customerID));
    }
    public void addAccount(String customerId, Account account){
        for(Customer customer:this.customers){
            if (customer.getCustomerId().equals(customerId)) customer.addAccount(account);
            else System.out.println("Khong tim thay tai khoan voi CCCD nay");
        }
    }
    public void addAccount(Customer customer, Account account){
        customer.addAccount(account);
        System.out.println("bank add account");
    }
}
