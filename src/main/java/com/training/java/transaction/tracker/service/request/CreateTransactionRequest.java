package com.training.java.transaction.tracker.service.request;

import java.math.BigDecimal;
import java.util.Date;

public class CreateTransactionRequest {

    //Properties should be immutable
    private final String description;
    private final BigDecimal amount;
    private final Date dateOfTransaction;

    // Insert Properties of transaction
    public CreateTransactionRequest(String description, BigDecimal amount, Date dateOfTransaction) {
        this.description = description;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

}
