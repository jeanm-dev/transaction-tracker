package com.training.java.transaction.tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.dao.TransactionType;
import com.training.java.transaction.tracker.repository.deprecated.TransactionRepository;
import com.training.java.transaction.tracker.repository.TransactionTypeRepository;
import com.training.java.transaction.tracker.service.dto.TransactionDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionServiceImplementationTest {

  private TransactionRepository mockRepository;
  private TransactionTypeRepository mockTypeRepository;
  private TransactionService serviceUnderTest;

  @BeforeEach
  public void setup() {
    mockRepository = mock(TransactionRepository.class);
    mockTypeRepository = mock(TransactionTypeRepository.class);
    serviceUnderTest = new TransactionServiceImplementation(mockRepository, mockTypeRepository);
  }

  @Test
  void createTransactionFailedWithErrorThenReturnFailedResponse() throws SQLException {
    // Given
    when(mockRepository.create(any()))
        .thenThrow(new SQLException("Failed to create transaction exception"));

    serviceUnderTest = new TransactionServiceImplementation(mockRepository, mockTypeRepository);

    // When
    CreateTransactionResponse response = serviceUnderTest.createTransaction(newCreateRequest());

    // Then
    assertEquals(false, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }

  private CreateTransactionRequest newCreateRequest() {
    return new CreateTransactionRequest("test", new BigDecimal(500), new Date());
  }


  @Test
  void createTransactionSuccessfulThenReturnSuccessResponseWithExpectedId() throws SQLException {
    // Given
    Transaction stubbedTransaction = newTransaction();

    int expectedTransactionId = 99;
    stubbedTransaction.setIdentifier(expectedTransactionId);

    when(mockRepository.create(any())).thenReturn(stubbedTransaction);

    // When
    CreateTransactionResponse response = serviceUnderTest
        .createTransaction(aValidTransactionRequest(stubbedTransaction));

    // Then
    assertEquals(true, response.wasSuccessful());
    assertNotNull(response.getDescription());
    assertEquals(expectedTransactionId, response.getIdentifier());
  }

  private CreateTransactionRequest aValidTransactionRequest(Transaction transaction) {
    return new CreateTransactionRequest(transaction.getDescription(), transaction.getAmount(),
        transaction.getDateOfTransaction());
  }

  @Test
  void fetchTransactionFailedWithExceptionThenReturnFailedResponse() throws SQLException {
    // Given
    SQLException sqlException = new SQLException("Unable to find id");
    when(mockRepository.fetchById(anyLong())).thenThrow(sqlException);

    // When
    FetchTransactionResponse response = serviceUnderTest
        .fetchTransaction(aFetchTransactionRequest(100L));

    // Then
    assertEquals(false, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }

  private FetchTransactionRequest aFetchTransactionRequest(Long searchId) {
    return new FetchTransactionRequest(searchId);
  }

  @Test
  void fetchTransactionIsInvokedWithUnknownIdThenReturnFailedResponse() throws SQLException {
    // Given
    when(mockRepository.fetchById(anyLong())).thenReturn(null);

    // When
    FetchTransactionResponse response = serviceUnderTest
        .fetchTransaction(aFetchTransactionRequest(200L));

    // Then
    assertEquals(false, response.wasSuccessful());
    assertNotNull(response.getDescription());
    assertNull(response.getTransaction());
  }

  @Test
  void fetchTransactionSuccessfulWithMatchingTypeThenReturnTransactionWithExpectedTypeInSuccessfulResponse()
      throws SQLException {
    // Given
    Long expectedTransactionId = 100L;
    Long expectedTransactionType = 22L;
    String expectedType = "Matching Type";

    Transaction stubbedTransaction = newTransaction();
    stubbedTransaction.setIdentifier(expectedTransactionId);
    stubbedTransaction.setType(expectedTransactionType);

    TransactionType transactionType = new TransactionType(expectedTransactionType, expectedType);

    when(mockRepository.fetchById(anyLong())).thenReturn(stubbedTransaction);
    when(mockTypeRepository.fetchAll()).thenReturn(List.of(transactionType));

    // When
    FetchTransactionResponse response = serviceUnderTest
        .fetchTransaction(aFetchTransactionRequest(expectedTransactionId));

    // Then
    assertEquals(true, response.wasSuccessful());
    assertNotNull(response.getDescription());
    assertNotNull(response.getTransaction());

    // MARK: Value Mappings
    TransactionDto resultTransaction = response.getTransaction();
    assertNotNull(resultTransaction);
    assertEquals(expectedTransactionId, resultTransaction.getIdentifier());
    assertEquals(stubbedTransaction.getDescription(), resultTransaction.getDescription());
    assertEquals(stubbedTransaction.getAmount(), resultTransaction.getAmount());
    assertEquals(stubbedTransaction.getDateOfTransaction(),
        resultTransaction.getDateOfTransaction());
    assertEquals(expectedType, resultTransaction.getType());
  }

  @Test
  void fetchTransactionSuccessfulUnmatchedTypeThenReturnTransactionWithUndefinedTypeInSuccessfulResponse()
      throws SQLException {
    // Given
    Long expectedTransactionId = 100L;
    String expectedType = TransactionDto.UNDEFINED;

    Transaction stubbedTransaction = newTransaction();
    stubbedTransaction.setIdentifier(expectedTransactionId);

    when(mockRepository.fetchById(anyLong())).thenReturn(stubbedTransaction);
    when(mockTypeRepository.fetchAll()).thenReturn(null);

    // When
    FetchTransactionResponse response = serviceUnderTest
        .fetchTransaction(aFetchTransactionRequest(expectedTransactionId));

    // Then
    assertEquals(true, response.wasSuccessful());
    assertNotNull(response.getDescription());
    assertNotNull(response.getTransaction());

    // MARK: Value Mappings
    TransactionDto resultTransaction = response.getTransaction();
    assertNotNull(resultTransaction);
    assertEquals(expectedTransactionId, resultTransaction.getIdentifier());
    assertEquals(stubbedTransaction.getDescription(), resultTransaction.getDescription());
    assertEquals(stubbedTransaction.getAmount(), resultTransaction.getAmount());
    assertEquals(stubbedTransaction.getDateOfTransaction(),
        resultTransaction.getDateOfTransaction());
    assertEquals(resultTransaction.getType(), expectedType);
  }


  private Transaction newTransaction() {
    return new Transaction("Test transaction", new BigDecimal(500.25), new Date());
  }

  @Test
  void updateTransactionFailedWithExceptionThenReturnFailedUpdateResponse() throws SQLException {
    // Given
    doThrow(new SQLException("Unable to update the transaction!")).when(mockRepository)
        .update(any()); // Example void function throw exception

    // When
    UpdateTransactionResponse response = serviceUnderTest.updateTransaction(
        new UpdateTransactionRequest(22, "Test 1", new BigDecimal(532.1), new Date()));

    // Then
    assertEquals(false, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }

  @Test
  void updateTransactionSuccessfulThenReturnSuccessfulResponse() {
    // Given
    String expectedDescription = "test";
    BigDecimal expectedAmount = new BigDecimal(333.33);
    Date expectedDate = new Date();
    int transactionIdToUpdate = 22;

    // When
    UpdateTransactionResponse response = serviceUnderTest.updateTransaction(
        new UpdateTransactionRequest(transactionIdToUpdate, expectedDescription, expectedAmount,
            expectedDate));

    // Then
    assertEquals(true, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }

  @Test
  void deleteTransactionFailedWithExceptionThenReturnFailedDeleteResponse() throws SQLException {
    // Given
    Long transactionIdToDelete = 99L;

    when(mockRepository.doesIdExist(anyLong())).thenReturn(true);
    doThrow(new SQLException("Unable to connect to database!")).when(mockRepository)
        .remove(anyLong());

    // When
    DeleteTransactionResponse response = serviceUnderTest
        .deleteTransaction(aDeleteTransactionRequest(transactionIdToDelete));

    // Then
    assertEquals(false, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }

  private DeleteTransactionRequest aDeleteTransactionRequest(Long transactionIdToDelete) {
    return new DeleteTransactionRequest(transactionIdToDelete);
  }

  @Test
  void deleteTransactionNonExistingTransactionThenReturnFailedResponse() throws SQLException {
    Long transactionIdToDelete = 33L;

    when(mockRepository.doesIdExist(anyLong())).thenReturn(false);

    // When
    DeleteTransactionResponse response = serviceUnderTest
        .deleteTransaction(aDeleteTransactionRequest(transactionIdToDelete));

    // Then
    assertEquals(false, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }

  @Test
  void deleteTransactionSuccessfulThenReturnSuccessfulResponse() throws SQLException {
    // Given
    Long transactionIdToDelete = 33L;

    when(mockRepository.doesIdExist(anyLong())).thenReturn(true);
    when(mockRepository.remove(anyLong())).thenReturn(true);

    // When
    DeleteTransactionResponse response = serviceUnderTest
        .deleteTransaction(aDeleteTransactionRequest(transactionIdToDelete));

    // Then
    assertEquals(true, response.wasSuccessful());
    assertNotNull(response.getDescription());
  }
}