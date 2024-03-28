package vn.funix.fx18922.java.dao;

import vn.funix.fx18922.java.models.BinaryFileService;
import vn.funix.fx18922.java.models.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private final static String FILE_NAME="customers.dat";
    public static void save(List<Customer> customers) throws IOException{
        BinaryFileService.writeFile(FILE_NAME,customers);
    }
    public static List<Customer> list(){
        try {
            return BinaryFileService.readFile(FILE_NAME);
        } catch (IOException e) {
            System.out.println("Chua co khach hang trong danh sach");
            return new ArrayList<>();
        }
    }
}
