package com.training.java.transaction.tracker.service.dto;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.dao.TransactionType;
import java.util.List;

public class TransactionDtoFactory {

  public static TransactionDto make(Transaction transaction,
      List<TransactionType> transactionTypes) {
    if (transaction.getType() != null && !transactionTypes.isEmpty()) {
      return new TransactionDto(transaction.getIdentifier(), transaction.getDescription(),
          transaction.getAmount(), transaction.getDateOfTransaction(),
          transactionTypes // TODO: Discuss Overkill? Probably also is
              .stream()
              .filter(type -> type.getIdentifier() == transaction.getType())
              .findFirst()
              .map(type -> type.getDescription())
              .orElse(TransactionDto.UNDEFINED) //NEVER Hit during tests
      );
    } else {
      return new TransactionDto(transaction.getIdentifier(), transaction.getDescription(),
          transaction.getAmount(), transaction.getDateOfTransaction());
    }

  }
}
