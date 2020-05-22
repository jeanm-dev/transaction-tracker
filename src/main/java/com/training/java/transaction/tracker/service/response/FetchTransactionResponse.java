package com.training.java.transaction.tracker.service.response;

import com.training.java.transaction.tracker.service.dto.TransactionDto;

public class FetchTransactionResponse extends BaseResponse {

    private final TransactionDto transaction;

    public FetchTransactionResponse(boolean successful, String description) {
        super(successful, description);
        transaction = null;
    }

    public FetchTransactionResponse(boolean successful, String description, TransactionDto transaction) {
        super(successful, description);
        this.transaction = transaction;
    }

    public TransactionDto getTransaction() {
        return transaction;
    }
}
