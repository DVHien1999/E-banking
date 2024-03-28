package vn.funix.fx18922.java.models;

import vn.funix.fx18922.java.dao.AccountDao;
import vn.funix.fx18922.java.dao.CustomerDao;
import vn.funix.fx18922.java.exception.CustomerIdNotValidException;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitalBank extends Bank {
    public Customer getCustomerById(String customerId) {
        List<Customer>customers=CustomerDao.list();
        for (Customer customer :customers ) {
            if (customer.getCustomerId().equals(customerId))
                return customer;
        }
        return null;
    }

    public void addCustomer(String customerId, String name) {
        DigitalCustomer customer = new DigitalCustomer(customerId, name);
        this.getCustomers().add(customer);
    }

    public void withdraw(Scanner scanner, String customerId) throws CustomerIdNotValidException {
        if(!checkID(customerId)){
            throw new CustomerIdNotValidException("ID khach hang khong hop le. Vui long nhap lai");
        }else{
            if(!isCustomerExist(customerId)){
                throw new CustomerIdNotValidException("Khong ton tai Khach hang nay. Vui long nhap lai");
            }else{
                Customer customer=getCustomerById(customerId);
                ((DigitalCustomer) customer).withDraw(scanner);
            }
        }
    }

    public void printTransaction(String customerId) {
        if(!checkID(customerId)){
            throw new CustomerIdNotValidException("ID khach hang khong hop le. Vui long nhap lai");
        }else{
            if(!isCustomerExist(customerId)){
                throw new CustomerIdNotValidException("Khong ton tai Khach hang nay. Vui long nhap lai");
            }else{
                Customer customer = getCustomerById(customerId);
                customer.displayInformation();
                ((DigitalCustomer) customer).printTransaction();
            }
        }
    }

    public void displayCustomers() {
       List<Customer> customersInFile= CustomerDao.list();
       if(customersInFile.isEmpty()){
           System.out.println("Hay nhap danh sach khach hang");
       }else{
           for(Customer c:customersInFile){
               c.displayInformation();
           }
       }
    }

    public void addCustomerFromFile(String fileName) throws FileNotFoundException{
        Scanner fileReader = null;
//      get Customer.txt path
        Path customerPath = FileSystems.getDefault().getPath("store", fileName);
        try {
            fileReader = new Scanner(new BufferedReader(new FileReader(customerPath.toFile())));
//      display all customer in Customer.txt file
            while (fileReader.hasNextLine()) {
                String[] data = fileReader.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                if(!checkID(id)){
                    System.out.println("ID: "+id+" khong hop le. Them khach hang "+name+" that bai");
                }else if(this.isCustomerExist(id)){
                    System.out.println("ID: "+id+ " da ton tai. Them khach hang "+name+" that bai");
                }else {
                    DigitalCustomer customer = new DigitalCustomer(name, id);
                    System.out.println("them khach hang "+name+" thanh cong. ID khach hang: "+id);
                    this.addCustomer(customer);
                }
                CustomerDao.save(this.getCustomers());
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Khong tim thay file. Nhap that bai");
        } catch (IOException e){
            System.out.println("IOexception: unable to save customers to file");
            System.out.println(e.getStackTrace());
        }

    }
//    public void updateFile(Customer customer){
//        File file=FileSystems.getDefault().getPath("store","customers.dat").toFile();
//        try (ObjectOutputStream out= new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))){
//            out.writeObject(customer);
//        }catch (FileNotFoundException e){
//            e.getMessage();
//            System.out.println("khong tim thay file");
//        }catch (IOException e){
//            System.out.println("IOException in update");
//            e.printStackTrace();
//        }
//    }

    @Override
    public void addAccount(Customer customer, Account account) {
        System.out.println("Dang them tai khoan cho khach hang"+customer.getName());
        super.addAccount(customer, account);
        try{
            CustomerDao.save(this.getCustomers());
        } catch (IOException e){
            System.out.println("IOException in addAccount");
            System.out.println(e.getMessage());
        }

    }
    public void transfers(Scanner scanner, String customerId) throws CustomerIdNotValidException {
        if(!checkID(customerId)){
            throw new CustomerIdNotValidException("ID khach hang khong hop le. Vui long nhap lai");
        }else{
            if (isCustomerExist(customerId)) {
                System.out.println("Thuc hien chuyen tien voi khach hang: " + customerId);
//                print customer information
                getCustomerById(customerId).displayInformation();
//                call customer transfers method
                getCustomerById(customerId).transfers(scanner);
            } else {
                System.out.println("Khach hang voi id: " + customerId + " khong ton tai ");
            }
        }

    }
    public boolean isAccountExist(String accountId){
        List<Account> accounts= AccountDao.list();
//        long count=accounts.stream().filter(account -> account.getAccountNumber().equals(accountId)).count();
//        if(count==1){
//            return true;
//        }else return false;
        return accounts.stream().anyMatch(account -> account.getAccountNumber().equals(accountId));
    }
    public static boolean checkID(String ID){
        Pattern pattern = Pattern.compile("[0-9]{12}");
        Matcher matcher = pattern.matcher(ID);
        return matcher.matches();
    }


}
