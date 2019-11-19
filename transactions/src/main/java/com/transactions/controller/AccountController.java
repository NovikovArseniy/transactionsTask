package com.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.transactions.service.AccountService;
import com.transactions.model.Account;
import com.transactions.model.Transaction;
import com.transactions.model.Order;

import java.util.Collection;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account/create", method = RequestMethod.POST)
    public void createAccount(@RequestBody Account account) {
        accountService.addAccount(account);
    }

    @RequestMapping(value = "/account/get", method = RequestMethod.GET)
    public Account getAccount(@RequestParam("id") String id) {
        return accountService.getAccountById(id);
    }

    @RequestMapping(value = "/account/all", method = RequestMethod.GET)
    public Collection<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @RequestMapping(value = "/order/transfer", method = RequestMethod.POST)
    public Transaction createTransaction(@RequestBody Order order) {
        return accountService.createTransaction(
                order.getFromAccount(),
                order.getToAccount(),
                order.getAmount()
        );
    }

    @RequestMapping(value = "/order/history", method = RequestMethod.GET)
    public Collection<Transaction> getAllTransaction() {
        return accountService.getAllTransactions();
    }
}
