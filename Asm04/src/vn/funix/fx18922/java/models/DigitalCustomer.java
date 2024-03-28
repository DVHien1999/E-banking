package vn.funix.fx18922.java.models;

import vn.funix.fx18922.java.dao.AccountDao;
import vn.funix.fx18922.java.dao.TransactionDao;

import java.util.Scanner;
import java.util.stream.Collectors;

public class DigitalCustomer extends Customer {
    public DigitalCustomer(String name, String customerId) {
        super(name, customerId);
    }

    @Override
    public void displayInformation() {
        String status=this.isPremium()?"Premium":"Normal";
        String balanceSum= String.format("%.0f",this.getBalanceSum());
        System.out.println(this.getCustomerId()+" |\t\t"+this.getName()+" | "+status+" | "+balanceSum+"đ");
        int index=1;
            for (Account a: this.getAccounts()){
                System.out.print(index);
                a.printAccountDetail();
                index++;
            }
        }

    public void withDraw(Scanner scanner){

        displayInformation();
        System.out.println("Nhap so tai khoan can rut tien");
        String accountNumber=scanner.nextLine();
        while (!thisUserAccount(accountNumber)){
            System.out.println("Khong ton tai tai khoan nay. Vui long nhap lai");
            accountNumber= scanner.nextLine();
        }
        System.out.println("Nhap so tien can rut");
        double amount= scanner.nextDouble();
        while (amount<50000){
            System.out.println("So tien rut phai hon 50000d. Vui long nhap lai");
            amount=scanner.nextDouble();
        }
        SavingsAccount account= (SavingsAccount) getAccountByID(accountNumber);
        account.withdraw(amount);

    }
    public void printTransaction(){
        for(Account account:AccountDao.list().stream().filter(account -> account.getCustomerID().equals(this.getCustomerId())).collect(Collectors.toList())){
            TransactionDao.list().stream().filter(transaction -> transaction.getAccountNumber().equals(account.getAccountNumber())).forEach(Transaction::printTransaction);
        }

    }

}
