package com.training.java.transaction.tracker.dao;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {

    private int identifier;
    private String description;
    private BigDecimal amount;
    private Date dateOfTransaction;

    public Transaction() {
    }

    public Transaction(String description, BigDecimal amount, Date dateOfTransaction) {
        this.description = description;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

}
