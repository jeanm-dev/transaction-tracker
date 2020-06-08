package com.training.java.transaction.tracker.service.dto;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.dao.TransactionType;
import java.util.List;

public class TransactionDtoFactory {

  public static TransactionDto make(Transaction transaction,
      List<TransactionType> transactionTypes) {

    String transactionType =
        transaction.getType() == null ? TransactionDto.UNDEFINED : transactionTypes
            .stream()
            .filter(type -> type.getIdentifier() == transaction.getType())
            .findFirst()
            .map(type -> type.getDescription())
            .orElse(TransactionDto.UNDEFINED);

    return new TransactionDto(transaction.getIdentifier(), transaction.getDescription(),
        transaction.getAmount(), transaction.getDateOfTransaction(),
        transactionType
    );
  }
}
