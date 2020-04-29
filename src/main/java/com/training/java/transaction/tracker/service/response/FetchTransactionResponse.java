package com.training.java.transaction.tracker.service.response;

import com.training.java.transaction.tracker.domainobject.Transaction;

public class FetchTransactionResponse extends BaseResponse {

    private final Transaction transaction;

    public FetchTransactionResponse(boolean successful, String description) {
        super(successful, description);
        transaction = null;
    }

    public FetchTransactionResponse(boolean successful, String description, Transaction transaction) {
        super(successful, description);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
