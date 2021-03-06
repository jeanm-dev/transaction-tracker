package com.training.java.transaction.tracker.service;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.dao.TransactionType;
import com.training.java.transaction.tracker.repository.TransactionRepository;
import com.training.java.transaction.tracker.repository.TransactionTypeRepository;
import com.training.java.transaction.tracker.service.dto.TransactionDtoFactory;
import com.training.java.transaction.tracker.service.request.CreateTransactionRequest;
import com.training.java.transaction.tracker.service.request.DeleteTransactionRequest;
import com.training.java.transaction.tracker.service.request.FetchTransactionRequest;
import com.training.java.transaction.tracker.service.request.UpdateTransactionRequest;
import com.training.java.transaction.tracker.service.response.CreateTransactionResponse;
import com.training.java.transaction.tracker.service.response.DeleteTransactionResponse;
import com.training.java.transaction.tracker.service.response.FetchTransactionResponse;
import com.training.java.transaction.tracker.service.response.UpdateTransactionResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TransactionServiceImplementation implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionTypeRepository typeRepository;

  public TransactionServiceImplementation(TransactionRepository transactionRepository,
      TransactionTypeRepository typeRepository) {
    this.transactionRepository = transactionRepository;
    this.typeRepository = typeRepository;
  }

  @Override
  public CreateTransactionResponse createTransaction(
      CreateTransactionRequest createTransactionRequest) {
    Transaction insertedTransaction = null;
    try {
      //TODO: Add Validation of properties
      String description = createTransactionRequest.getDescription();
      BigDecimal amount = createTransactionRequest.getAmount();
      Date dateOfTransaction = createTransactionRequest.getDateOfTransaction();

      Transaction transaction = new Transaction(description, amount, dateOfTransaction);
      insertedTransaction = transactionRepository.create(transaction);
    } catch (SQLException e) {
      return new CreateTransactionResponse(false, "Failed to insert transaction" + e.getMessage());
    }

    return new CreateTransactionResponse(true, "Transaction created successfully!",
        insertedTransaction.getIdentifier());
  }

  @Override
  public FetchTransactionResponse fetchTransaction(
      FetchTransactionRequest fetchTransactionRequest) {
    int transactionId = fetchTransactionRequest.getTransactionId();

    try {
      Transaction transaction = transactionRepository.fetchById(transactionId);
      if (transaction != null) {
        List<TransactionType> transactionTypes = typeRepository
            .fetchAll(); // Caching Layer? Service
        return new FetchTransactionResponse(true, "Found transaction!", TransactionDtoFactory
            .make(transaction, transactionTypes));
      } else {
        return new FetchTransactionResponse(false, "Id doesn't exist");
      }

    } catch (SQLException e) {
      return new FetchTransactionResponse(false, "Failed to fetch the transaction");
    }
  }

  @Override
  public UpdateTransactionResponse updateTransaction(
      UpdateTransactionRequest updateTransactionRequest) {

    // TODO: Validate that the transaction is in the database
    int transactionId = updateTransactionRequest.getTransactionId();

    String description = updateTransactionRequest.getDescription();
    BigDecimal amount = updateTransactionRequest.getAmount();
    Date date = updateTransactionRequest.getDateOfTransaction();

    Transaction transaction = new Transaction(description, amount, date);
    transaction.setIdentifier(transactionId);

    try {
      transactionRepository.update(transaction); // TODO: Extend to return Transaction model
      return new UpdateTransactionResponse(true, "Updated the transaction");
    } catch (SQLException e) {
      return new UpdateTransactionResponse(false, "Unable to update the transaction");
    }
  }

  @Override
  public DeleteTransactionResponse deleteTransaction(
      DeleteTransactionRequest deleteTransactionRequest) {
    // Check and return false if not present
    int transactionToDeleteId = deleteTransactionRequest.getTransactionId();

    try {
      // Check if the transaction exists
      boolean exists = transactionRepository.doesIdExist(transactionToDeleteId);

      if (exists) {
        transactionRepository.remove(transactionToDeleteId);
        return new DeleteTransactionResponse(true, "Successfully deleted the transaction!");
      } else {
        return new DeleteTransactionResponse(false,
            "Unable to delete transaction, it does not exist!");
      }
    } catch (SQLException e) {
      return new DeleteTransactionResponse(false, "Unable to delete the transaction!");
    }
  }
}
