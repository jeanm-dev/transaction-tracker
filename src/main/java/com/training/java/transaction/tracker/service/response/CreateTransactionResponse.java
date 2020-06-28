package com.training.java.transaction.tracker.service.response;

public class CreateTransactionResponse extends BaseResponse {

    private final long transactionId;

    public CreateTransactionResponse(boolean successful, String description) {
        super(successful, description);
        transactionId = -1;
    }

    public CreateTransactionResponse(boolean successful, String description, long transactionId) {
        super(successful, description);
        this.transactionId = transactionId;
    }


    public long getIdentifier() {
        return transactionId;
    }
}
