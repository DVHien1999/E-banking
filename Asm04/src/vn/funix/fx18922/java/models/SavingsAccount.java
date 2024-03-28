package vn.funix.fx18922.java.models;
import vn.funix.fx18922.java.ITransfer;
import vn.funix.fx18922.java.ReportService;
import vn.funix.fx18922.java.Withdraw;
import vn.funix.fx18922.java.dao.TransactionDao;
import vn.funix.fx18922.java.dao.AccountUpdateRunnable;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingsAccount extends Account implements ReportService, Withdraw, Serializable, ITransfer {
    private final double SAVINGS_ACCOUNT_MAX_WITHDRAW=5_000_000;
    private List<Transaction> transactions;
    public SavingsAccount(String customerId,String accountNumber,double balance) {
        super(customerId,accountNumber,balance);
        transactions = new ArrayList<Transaction>();
    }

    public SavingsAccount(String customerID, String accountNumber, double balance, List<Transaction> transactions) {
        super(customerID, accountNumber, balance);
        this.transactions = transactions;
    }

    @Override
    public void log(double amount,String type, String receiveAccount) {
//        format string
        String title=String.format("\t\t BIEN LAI GIAO DICH SAVINGS");
        String strDate = getTransactionDay();
        String currentDate=String.format("NGAY G/D:%28s",strDate);
        String atmID= String.format("ATM ID:%30s","DIGITAL-BANK-ATM 2022");
        String accountNumber=String.format("SO TK:%31s",this.getAccountNumber());
        String receiveAccountNumber=String.format("SO TK:%31s",receiveAccount);
        String balanceAfter=String.format("SO DU:%30.0fđ",this.getBalance());
        String stringFee=String.format("PHI + VAT:%27s","0đ");
//print log
        System.out.println("+----------+--------------------+----------+");
        System.out.println(title);
        System.out.println(currentDate);
        System.out.println(atmID);
        System.out.println(accountNumber);
        if (type.equals("TRANSFERS")){
            System.out.println(receiveAccountNumber);
            String stringAmount=String.format("SO TIEN CHUYEN:%21.0fđ",amount);
            System.out.println(stringAmount);
        }
        if (type.equals("WITHDRAW")){
            String stringAmount=String.format("SO TIEN RUT:%24.0fđ",amount);
            System.out.println(stringAmount);
        }
        System.out.println(balanceAfter);
        System.out.println(stringFee);
        System.out.println("+----------+--------------------+----------+");
    }

    @Override
    public boolean withdraw(double amount) {
        if(isAccepted(amount)){
            this.setBalance(this.getBalance()-amount);
            Transaction transaction=new Transaction(this.getAccountNumber(),-amount,getTransactionDay(),true,"WITHDRAW");
            try {
                TransactionDao.update(transaction);
            }catch (IOException e){
                System.out.println("IOException in withdraw");
            }
            this.log(amount,"WITHDRAW",this.getAccountNumber());
            return true;
        }else return false;
    }


    @Override
    public boolean isAccepted(double amount) {
        if(amount%10000!=0){
            System.out.println("So tien phai la boi so cua 10.000");
            return false;
        }else if(getBalance()-amount<50_000){
           System.out.println("So du con lai phai hon 50.000d");
           return false;
       } else if(amount<50000){
           System.out.println("So tien phai lon hon 50000d");
           return false;
       } else if(!isPremium()&&(amount>SAVINGS_ACCOUNT_MAX_WITHDRAW)) {
            System.out.println("Tai khoan thuong khong duoc giao dich nhieu hon 5.000.000d");
            return false;
        } else {
           Date date= new Date();
           SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
           String strDate = formatter.format(date);
           return true;
       }
    }
    @Override
    public void printAccountDetail() {
        String detail= String.format("%11s |%11s |%19.0fđ",getAccountNumber(),"SAVINGS",this.getBalance());
        System.out.println(detail);
    }
    public void printAllTransaction(){
        for (Transaction t: transactions){
            t.printTransaction();
        }
    }

    public void transfers(Account receiveAccount, double amount){
        if (isAccepted(amount)){
            this.setBalance(getBalance()-amount);
            receiveAccount.setBalance(receiveAccount.getBalance()+amount);
//            update change account to file
//            try {
                ExecutorService executorService= Executors.newSingleThreadExecutor();

//                AccountDao.update(receiveAccount);
//                AccountDao.update(this);
                executorService.execute(new AccountUpdateRunnable(receiveAccount));
                executorService.execute(new AccountUpdateRunnable(this));
//            }
//            catch (IOException e) {
//                System.out.println("Exception in SavingsAccount.transfers");
//            }

//            create new transaction
            Transaction transaction= new Transaction(this.getAccountNumber(),-amount,getTransactionDay(),true,"TRANSFERS");
            Transaction receiveTransaction= new Transaction(receiveAccount.getAccountNumber(), amount,getTransactionDay(),true,"TRANSFERS");
            this.log(amount,"TRANSFERS",receiveAccount.getAccountNumber());
//            update transaction list
            try {
                TransactionDao.update(transaction);
                TransactionDao.update(receiveTransaction);
            } catch (IOException e) {
                System.out.println("Transaction IOException in saving account");
                e.printStackTrace();
            }
        }else {
            System.out.println("Chuyen tien that bai");
        }
    }
    public static String getTransactionDay(){
        Date date= new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        return  strDate;
    }
}
