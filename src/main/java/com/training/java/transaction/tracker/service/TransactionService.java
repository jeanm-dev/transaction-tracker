package com.training.java.transaction.tracker.service;

import com.training.java.transaction.tracker.service.request.CreateTransactionRequest;
import com.training.java.transaction.tracker.service.request.DeleteTransactionRequest;
import com.training.java.transaction.tracker.service.request.FetchTransactionRequest;
import com.training.java.transaction.tracker.service.request.UpdateTransactionRequest;
import com.training.java.transaction.tracker.service.response.CreateTransactionResponse;
import com.training.java.transaction.tracker.service.response.DeleteTransactionResponse;
import com.training.java.transaction.tracker.service.response.FetchTransactionResponse;
import com.training.java.transaction.tracker.service.response.UpdateTransactionResponse;

// CRUD: operations
public interface TransactionService {

  CreateTransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest);

  FetchTransactionResponse fetchTransaction(FetchTransactionRequest fetchTransactionRequest);

  UpdateTransactionResponse updateTransaction(UpdateTransactionRequest updateTransactionRequest);

  DeleteTransactionResponse deleteTransaction(DeleteTransactionRequest deleteTransactionRequest);

}
