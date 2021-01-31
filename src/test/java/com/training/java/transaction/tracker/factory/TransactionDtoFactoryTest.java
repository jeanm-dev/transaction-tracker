package com.training.java.transaction.tracker.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.dao.TransactionType;
import com.training.java.transaction.tracker.service.dto.TransactionDto;
import com.training.java.transaction.tracker.service.dto.TransactionDtoFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TransactionDtoFactoryTest {

  @Test
  void testGivenFactoryAndMakeInvokedWithTransactionThenReturnTransactionDto() {
    // Given
    Transaction transaction = newTransaction();

    // When
    TransactionDto resultTransaction = TransactionDtoFactory.make(transaction, List.of());

    // Then
    assertNotNull(resultTransaction);
  }

  @Test
  void testGivenFactoryWithNoTypesAndMakeInvokedWithTransactionThenReturnTransactionDtoWithUnspecifiedType() {
    // Given
    Transaction transaction = newTransaction();
    String expectedType = TransactionDto.UNDEFINED;

    // When
    TransactionDto resultTransaction = TransactionDtoFactory.make(transaction, List.of());

    // Then
    assertEquals(expectedType, resultTransaction.getType());
  }

  @Test
  void testGivenFactoryWithTypesAndMakeInvokedWithTransactionThatHasNoTypeThenReturnTransactionDtoWithUnspecifiedType() {
    // Given
    List<TransactionType> transactionTypes = List.of(new TransactionType(1L, "First Type"));
    Transaction transaction = newTransaction();
    String expectedType = TransactionDto.UNDEFINED;

    // When
    TransactionDto resultTransaction = TransactionDtoFactory.make(transaction, transactionTypes);

    // Then
    assertEquals(expectedType, resultTransaction.getType());
  }

  @Test
  void testGivenFactoryWithTypesAndMakeInvokedWithTransactionThatHasMatchingTypeThenReturnTransactionDto() {
    // Given
    Long matchingTypeId = 1L;
    String expectedType = "Matching Type";
    TransactionType matchingType = new TransactionType(matchingTypeId, expectedType);

    List<TransactionType> transactionTypes = List.of(matchingType);
    Transaction transaction = newTransaction();
    transaction.setType(matchingTypeId);

    // When
    TransactionDto resultTransaction = TransactionDtoFactory.make(transaction, transactionTypes);

    // Then
    assertEquals(expectedType, resultTransaction.getType());
  }

  @Test
  void testGivenFactoryWithTypesAndMakeInvokedWithTransactionThatDoesNotMatchTypesThenReturnTransactionDtoWithUnspecifiedType() {
    // Given
    Long nonMatchingTypeId = 2L;
    String expectedType = TransactionDto.UNDEFINED;
    TransactionType notMatchingType = new TransactionType(1L, "Type");

    List<TransactionType> transactionTypes = List.of(notMatchingType);
    Transaction transaction = newTransaction();
    transaction.setType(nonMatchingTypeId);

    // When
    TransactionDto resultTransaction = TransactionDtoFactory.make(transaction, transactionTypes);

    // Then
    assertEquals(expectedType, resultTransaction.getType());
  }

  private Transaction newTransaction() {
    return new Transaction("Test transaction", new BigDecimal(500.25), new Date());
  }
}
