package com.transactions.repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.transactions.model.Transaction;

public class TransactionRepository {

    private AtomicLong id;

    private final ConcurrentHashMap<Long, Transaction> transactions;

    public TransactionRepository() {
        this.id = new AtomicLong();
        this.transactions = new ConcurrentHashMap<>();
    }

    public Transaction getTransactionById(long id) {
        return this.transactions.get(id);
    }

    public Collection<Transaction> getAllTransactions() {
        return this.transactions.values();
    }

    public Transaction createTransaction(String fromAccount, String toAccount, double amount, String date) {

        long id = this.id.getAndIncrement();
        Transaction transaction = new Transaction(id, fromAccount, toAccount, amount, date);
        this.transactions.put(id, transaction);
        return transaction;
    }

    public Transaction createTransaction(){
        return new Transaction();
    }
}
