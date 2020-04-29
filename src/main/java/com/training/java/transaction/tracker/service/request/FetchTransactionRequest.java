package com.training.java.transaction.tracker.service.request;

public class FetchTransactionRequest {

    private final int transactionId;

    public FetchTransactionRequest(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
