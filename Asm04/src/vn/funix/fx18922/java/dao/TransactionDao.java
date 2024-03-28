package vn.funix.fx18922.java.dao;

import vn.funix.fx18922.java.models.Account;
import vn.funix.fx18922.java.models.BinaryFileService;
import vn.funix.fx18922.java.models.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private final static String FILE_NAME="transactions.dat";
    public static void save(List<Transaction> transactions) throws IOException {
        BinaryFileService.writeFile(FILE_NAME,transactions);
    }
    public static List<Transaction> list(){
        try {
            return BinaryFileService.readFile(FILE_NAME);
        } catch (IOException e) {
//            do nothing? catch when updating transaction for the first time while file is empty.
            return new ArrayList<>();
        }
    }
    public static void update(Transaction editTransaction) throws IOException{
//        old transaction list
        List<Transaction> oldTransactions=list();
//        new list
        List<Transaction>updatedTransactions;
            updatedTransactions = new ArrayList<>(oldTransactions);
            updatedTransactions.add(editTransaction);
            save(updatedTransactions);


    }
}
