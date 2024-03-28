package vn.funix.fx18922.java;

import vn.funix.fx18922.java.exception.CustomerIdNotValidException;
import vn.funix.fx18922.java.models.Customer;
import vn.funix.fx18922.java.models.DigitalBank;
import vn.funix.fx18922.java.models.DigitalCustomer;
import vn.funix.fx18922.java.models.SavingsAccount;


import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String PRODUCT_NAME="NGAN HANG SO";
    private static final String FUNIX_ID="FX18922";
    private static final String VERSION="@v4.0.0";
    private static final Scanner sc= new Scanner(System.in);
    private static final DigitalBank DGBank=new DigitalBank();
    public static void start(){
        System.out.println("+----------+--------------------+----------+");
        System.out.println("|"+PRODUCT_NAME+"| "+FUNIX_ID+VERSION+"     |");
        System.out.println("+----------+--------------------+----------+");
        System.out.println(" 1. Danh sach khach hang");
        System.out.println(" 2. Nhap danh sach khach hang");
        System.out.println(" 3. Them tai khoan ATM");
        System.out.println(" 4. Chuyen tien");
        System.out.println(" 5. Rut tien");
        System.out.println(" 6. Tra cuu lich su giao dich");
        System.out.println(" 0. Thoat                               ");
        System.out.println("+----------+--------------------+----------+");
    }

    public static void displayCustomer(){
        DGBank.displayCustomers();
    }
    public static void addCustomer(){
            try {
                System.out.println("Nhap ten file text: ");
                String fileName=sc.nextLine();
                DGBank.addCustomerFromFile(fileName);
            }catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }



    }
    public static void addATMAccount() {
//        check if customer existed then get customer by ID
        String customerId;
        do {
            System.out.println("Nhap ma so cua khach hang");
            customerId = sc.nextLine();
            if (customerId.equalsIgnoreCase("N")) System.exit(0);
            if (!DGBank.isCustomerExist(customerId)){
                System.out.println("Khong tim thay khach hang " + customerId + " vui long nhap lai hoac nhap \"N\" de dung chuong trinh");
            }
        } while (!DGBank.isCustomerExist(customerId));
        Customer customer = DGBank.getCustomerById(customerId);
//      Check account, add account
        String accountNumber;
        System.out.println("Nhap so tai khoan gom 6 chu so");
        Pattern accountNumberPattern = Pattern.compile("[0-9]{6}");
        Matcher matcher;
        do {
            accountNumber = sc.nextLine();
            matcher = accountNumberPattern.matcher(accountNumber);
            if (!matcher.matches()) System.out.println("Khong hop le. Vui long nhap lai");
//            check if this customer already has an account with this account number
            if (customer.isAccountExist(accountNumber))
                System.out.println("Đã tồn tại tài khoản với ID này. Vui long nhap lai");
        }
        while ((!matcher.matches()) || (customer.isAccountExist(accountNumber)));
//      add account balance
        System.out.println("Nhap so du tai khoan");
        double amount=0;
        while (true) {
            try {
                amount = sc.nextDouble();
                while (amount < 50000) {
                    System.out.println("so du tai khoan phai hon 50000");
                    System.out.println("Nhap so du tai khoan");
                    amount = sc.nextDouble();
                    sc.nextLine();
                }break;
            } catch (InputMismatchException e) {
                System.out.println("Khong hop le vui long nhap lai");
            }
        }
            SavingsAccount savingsAccount = new SavingsAccount(customerId, accountNumber, amount);
//        add account to customer
            DGBank.addAccount(customer, savingsAccount);
            System.out.println("Tao tai khoan thanh cong");
        }
    public static void transfers(){
        System.out.println("Nhap ma so khach hang");
        String customerID = sc.nextLine();
        while (true) {
            try {
                DGBank.transfers(sc, customerID);
                break;
            } catch (CustomerIdNotValidException e) {
                System.out.println(e.getMessage());
                customerID=sc.nextLine();
            }
        }
    }
    public static void withdraw(){
        System.out.println("Nhap ma so khach hang");
        String customerID = sc.nextLine();
        while (true) {
            try {
                DGBank.withdraw(sc,customerID);
                break;
            } catch (CustomerIdNotValidException e) {
                System.out.println(e.getMessage());
                customerID=sc.nextLine();
            }
        }
    }
    public static void printTransaction(){
        System.out.println("Nhap ma so khach hang");
        String customerID = sc.nextLine();
        while (true) {
            try {
                DGBank.printTransaction(customerID);
                break;
            } catch (CustomerIdNotValidException e) {
                System.out.println(e.getMessage());
                customerID=sc.nextLine();
            }
        }
    }
    public static void commandSelect() {
        int command=-1;
        System.out.println("Hay chon chuc nang:");
        try {
            command = sc.nextInt();
        } catch (Exception e){
            System.out.println("yeu cau nhap so");
        }
        sc.nextLine();
        while (command < 0 || command > 6) {
            System.out.println("Khong hop le vui long chon lai");
            command = sc.nextInt();
            sc.nextLine();
        }
        switch (command) {
            case 1:
                System.out.println("Ban chon hien thi danh sach khach hang:");
                displayCustomer();
                break;
            case 2:
                System.out.println("Nhap danh sach khach hang");
                addCustomer();
                break;
            case 3:
                System.out.println("Ban Them tai khoan ATM");
                addATMAccount();
                break;
            case 4:
                System.out.println("Ban chon chuyen tien");
                transfers();
                break;
            case 5:
                System.out.println("ban chon rut tien");
                withdraw();
                break;
            case 6:
                System.out.println("Ban chon xem lich su giao dich");
                printTransaction();
                break;
            case 0:
                System.exit(0);
        }
    }
    public static void main(String[] agrs){
        start();
        while(true) {
            commandSelect();
            System.out.println("+----------+--------------------+----------+");
            System.out.println(" 1. Danh sach khach hang");
            System.out.println(" 2. Nhap danh sach khach hang");
            System.out.println(" 3. Them tai khoan ATM");
            System.out.println(" 4. Chuyen tien");
            System.out.println(" 5. Rut tien");
            System.out.println(" 6. Tra cuu lich su giao dich");
            System.out.println(" 0. Thoat                                  ");
            System.out.println("+----------+--------------------+----------+");
        }
    }
}
