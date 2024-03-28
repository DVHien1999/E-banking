package vn.funix.fx18922.java.dao;

import vn.funix.fx18922.java.models.Account;
import vn.funix.fx18922.java.models.BinaryFileService;
import vn.funix.fx18922.java.models.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountDao {
    private final static String FILE_NAME="accounts.dat";
    public static void save(List<Account> accounts) throws IOException {
        BinaryFileService.writeFile(FILE_NAME,accounts);
    }
    public static List<Account> list(){
        try {
            return BinaryFileService.readFile(FILE_NAME);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    public static void update(Account editAccount) throws IOException{
        List<Account> oldAccounts=list();
        boolean isExisted=oldAccounts.stream().anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));
        List<Account>updatedAccounts;
        if(!isExisted){
            updatedAccounts=new ArrayList<>(oldAccounts);
            updatedAccounts.add(editAccount);
        } else {
            updatedAccounts=new ArrayList<>();
            for (Account account:oldAccounts){
                if(account.getAccountNumber().equals(editAccount.getAccountNumber())){
                    updatedAccounts.add(editAccount);
                }else {
                    updatedAccounts.add(account);
                }
            }
        }
        save(updatedAccounts);
    }
}
