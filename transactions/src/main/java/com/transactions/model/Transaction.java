package com.transactions.model;

import java.io.Serializable;
import java.util.Objects;


public class Transaction implements Serializable {

    private long id;

    private String fromAccount;
    private String toAccount;

    private double amount;

    private String date;

    public Transaction() { }

    public Transaction(
            long id,
            String fromAccount,
            String toAccount,
            double amount,
            String date
    ) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getId() == that.getId() &&
                Double.compare(that.getAmount(), getAmount()) == 0 &&
                Objects.equals(getFromAccount(), that.getFromAccount()) &&
                Objects.equals(getToAccount(), that.getToAccount()) &&
                Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFromAccount(), getToAccount(), getAmount(), getDate());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
