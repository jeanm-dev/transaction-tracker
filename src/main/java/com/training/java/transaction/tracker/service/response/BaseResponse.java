package com.training.java.transaction.tracker.service.response;

public abstract class BaseResponse {

    private final boolean successful;
    private final String description;

    public BaseResponse(boolean successful, String description) {
        this.successful = successful;
        this.description = description;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    public String getDescription() {
        return description;
    }
}
