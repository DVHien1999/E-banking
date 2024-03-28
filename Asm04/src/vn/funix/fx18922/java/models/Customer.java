package vn.funix.fx18922.java.models;

import vn.funix.fx18922.java.dao.AccountDao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Account> account;

    public Customer(String name, String customerId) {
        super(name, customerId);
        this.account = new ArrayList<>();
    }


    public List<Account> getAccount() {
//        List<Account>accounts=AccountDao.list().stream().filter();
        return this.account;
    }

    public boolean isPremium() {
        for (Account a : AccountDao.list().stream().filter(account1 -> account1.getCustomerID().equals(this.getCustomerId())).collect(Collectors.toList())) {
            if (a.isPremium()) return true;
        }
        return false;
    }

    public void addAccount(Account account) {
        if (!isAccountExist(account.getAccountNumber())){
            this.account.add(account);
        }else {
            System.out.println("đã tồn tại tài khoản với id này");
        }
//        update account
        try {
            AccountDao.update(account);
        } catch (IOException e) {
            System.out.println("IOException in addAccount");
            System.out.println(e.getMessage());
        }
    }

    public boolean isAccountExist(String accountNumber) {
        long count=AccountDao.list().stream().filter(account1 -> account1.getAccountNumber().equals(accountNumber)).count();
        if(count==1){
            return true;
        }else return false;
    }
    public double getBalanceSum(){
        double sum=0;
        List<Account> Accounts=AccountDao.list().stream().filter(account1 -> account1.getCustomerID().equals(this.getCustomerId())).collect(Collectors.toList());
        for (Account a : Accounts){
            sum+=a.getBalance();
        }
        return sum;
    }
    public void displayInformation(){
        String status=this.isPremium()?"Premium":"Normal";
        String balanceSum= String.format("%.0f",this.getBalanceSum());
        System.out.println(this.getCustomerId()+" |\t\t"+this.getName()+" | "+status+" | "+balanceSum+"đ");
        int index=1;
//        print each account detail with this customer in accounts.dat file
        for (Account a: this.getAccounts()){
            System.out.print(index+"\t    ");
            a.printAccountDetail();
            index++;
        }
    }
    public Account getAccountByID(String accountNumber){
        List<Account> accounts=AccountDao.list();
        for(Account a:accounts){
            if(a.getAccountNumber().equals(accountNumber)){
                return a;
            }
        }
        return null;
    }
    public void transfers(Scanner scanner){
        String accountId;
        System.out.println("Nhap ma tai khoan dung de chuyen tien:");
        accountId= scanner.nextLine();
//        check if this customer's has this account
        while (true){
            if (!thisUserAccount(accountId)){
                System.out.println("Khong ton tai tai khoan nay vui long nhap lai: ");
                accountId= scanner.nextLine();
            }else break;
        }
        SavingsAccount account=(SavingsAccount)getAccountByID(accountId);
        account.printAccountDetail();
        System.out.println("Nhap ma tai khoan nhan tien: ");
//        check if receive account existed
        String receiveAccountId=scanner.nextLine();
        while ((!isAccountExist(receiveAccountId))||(receiveAccountId.equals(accountId))){
            if(!isAccountExist(receiveAccountId)){
                System.out.println("Khong ton tai tai khoan nay vui long nhap lai: ");
                receiveAccountId= scanner.nextLine();
            }
            if(receiveAccountId.equals(accountId)){
                System.out.println("Khong duoc gui trong cung tai khoan. Vui long nhap lai");
                receiveAccountId= scanner.nextLine();
            }
        }
        String finalAccountId = receiveAccountId;
//        search the receiving account in accounts.dat file
        List<Account> tempList=AccountDao.list().stream().filter(account1 -> account1.getAccountNumber().equals(finalAccountId)).collect(Collectors.toList());
        Account receiveAccount=tempList.get(0);
        System.out.println("Xac nhan tai khoan nhan tien la: "+receiveAccount.getAccountNumber());
//      check if amount is greater than 50000
        System.out.println("Nhap so tien can chuyen");
        double amount=scanner.nextDouble();
        while(amount<50000){
                System.out.println("So tien can chuyen phai lon hon 50000. Vui long nhap lai: ");
                amount= scanner.nextDouble();
            scanner.nextLine();
            }
        scanner.nextLine();
//        call account transfers method
        System.out.println("Bam Y de xac nhan chuyen tien");
        String accepted= scanner.nextLine();
        if (accepted.equalsIgnoreCase("Y")){
            account.transfers(receiveAccount, amount);
        } else {
            System.out.println("Huy giao dich");
        }
    }

    public List<Account> getAccounts(){
        List<Account> accounts= AccountDao.list().stream().filter(account1 -> account1.getCustomerID().equals(this.getCustomerId())).collect(Collectors.toList());
        return accounts;
    }
    public boolean thisUserAccount(String accountNumber) {
        long count=AccountDao.list().stream().filter(account1 -> account1.getCustomerID().equals(this.getCustomerId())).filter(account1 -> account1.getAccountNumber().equals(accountNumber)).count();
        if(count==1){
            return true;
        }else return false;
    }
}
