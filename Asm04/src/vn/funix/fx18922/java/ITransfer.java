package vn.funix.fx18922.java;

import vn.funix.fx18922.java.models.Account;

public interface ITransfer {
    void transfers(Account receiveAccount,double amount);
}
