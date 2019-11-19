package com.transactions.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

import com.transactions.model.Transaction;
import com.transactions.repository.AccountRepository;
import com.transactions.model.Account;
import com.transactions.repository.TransactionRepository;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }

    public Collection<Transaction> getAllTransactions() {
        return this.transactionRepository.getAllTransactions();
    }

    synchronized public Collection<Account> getAllAccounts() {

        List<Account> result = new ArrayList<>();
        for (Account account : accountRepository.getAllAccounts()) {
            Account copyAccount = new Account(account.getId(), account.getAmount());
            result.add(copyAccount);
        }
        return result;
    }

    synchronized public Account getAccountById(String id) {
        if (accountRepository.accountExists(id)) {
            Account account = accountRepository.getAccountById(id);
            return new Account(account.getId(), account.getAmount());
        } else return new Account();
    }

    synchronized public Transaction createTransaction(
            String fromAccountId,
            String toAccountId,
            double amount
    ) {
        if (accountRepository.bothAccountsExist(fromAccountId, toAccountId)) {
            Account fromAccount = accountRepository.getAccountById(fromAccountId);
            Account toAccount = accountRepository.getAccountById(toAccountId);

            if (amount > 0 && fromAccount.getAmount() >= amount) {
                double fromAmount = fromAccount.getAmount();
                fromAccount.setAmount(fromAmount - amount);
                double toAmount = toAccount.getAmount();
                toAccount.setAmount(toAmount + amount);
                Date date = new Date();
                return transactionRepository.createTransaction(fromAccountId, toAccountId, amount, date.toString());
            }
        }
        return transactionRepository.createTransaction();
    }

    synchronized public void addAccount(Account account) {
        if (account.getAmount() >= 0) {
            accountRepository.addAccount(account);
        }
    }
}
