package com.transactions.repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.transactions.model.Account;


public class AccountRepository {

    private final ConcurrentHashMap<String, Account> accounts;

    public AccountRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    public Collection<Account> getAllAccounts() {
        return this.accounts.values();
    }

    public void addAccount(Account account) {
        accounts.putIfAbsent(account.getId(), account);
    }

    public boolean accountExists(String id){
        if (accounts.containsKey(id)) return true;
        else return false;
    }

    public boolean bothAccountsExist(String id_1, String id_2){
        if (accounts.containsKey(id_1) && accounts.containsKey(id_2)){
            return true;
        } else return false;
    }
}
