package vn.funix.fx18922.java.dao;

import vn.funix.fx18922.java.models.Account;

import java.io.IOException;

public class AccountUpdateRunnable implements Runnable{
    private Account account;

    public AccountUpdateRunnable(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        try {
            AccountDao.update(account);
        } catch (IOException e) {
            System.out.println("IOException in runnable");
        }
    }
}
