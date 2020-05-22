package com.training.java.transaction.tracker.service;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.repository.TransactionRepository;
import com.training.java.transaction.tracker.service.dto.TransactionDto;
import com.training.java.transaction.tracker.service.request.CreateTransactionRequest;
import com.training.java.transaction.tracker.service.request.DeleteTransactionRequest;
import com.training.java.transaction.tracker.service.request.FetchTransactionRequest;
import com.training.java.transaction.tracker.service.request.UpdateTransactionRequest;
import com.training.java.transaction.tracker.service.response.CreateTransactionResponse;
import com.training.java.transaction.tracker.service.response.DeleteTransactionResponse;
import com.training.java.transaction.tracker.service.response.FetchTransactionResponse;
import com.training.java.transaction.tracker.service.response.UpdateTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplementationTest {

    private TransactionRepository mockRepository;
    private TransactionService serviceUnderTest;

    @BeforeEach
    public void setup() {
        mockRepository = mock(TransactionRepository.class);
        serviceUnderTest = new TransactionServiceImplementation(mockRepository);
    }

    @Test
    void createTransactionFailedWithErrorThenReturnFailedResponse() throws SQLException {
        // Given
        when(mockRepository.create(any())).thenThrow(new SQLException("Failed to create transaction exception"));

        serviceUnderTest = new TransactionServiceImplementation(mockRepository);

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
        CreateTransactionResponse response = serviceUnderTest.createTransaction(aValidTransactionRequest(stubbedTransaction));

        // Then
        assertEquals(true, response.wasSuccessful());
        assertNotNull(response.getDescription());
        assertEquals(expectedTransactionId, response.getIdentifier());
    }

    private CreateTransactionRequest aValidTransactionRequest(Transaction transaction) {
        return new CreateTransactionRequest(transaction.getDescription(), transaction.getAmount(), transaction.getDateOfTransaction());
    }

    @Test
    void fetchTransactionFailedWithExceptionThenReturnFailedResponse() throws SQLException {
        // Given
        SQLException sqlException = new SQLException("Unable to find id");
        when(mockRepository.fetchAll()).thenThrow(sqlException);

        // When
        FetchTransactionResponse response = serviceUnderTest.fetchTransaction(aFetchTransactionRequest(100));

        // Then
        assertEquals(false, response.wasSuccessful());
        assertNotNull(response.getDescription());
    }

    private FetchTransactionRequest aFetchTransactionRequest(int searchId) {
        return new FetchTransactionRequest(searchId);
    }

    @Test
    void fetchTransactionSuccessfulThenReturnTransactionSuccessfulResponse() throws SQLException {
        // Given
        int expectedTransactionId = 100;

        Transaction stubbedTransaction = newTransaction();
        stubbedTransaction.setIdentifier(expectedTransactionId);

        TransactionDto expectedTransaction = new TransactionDto(stubbedTransaction, null);

        when(mockRepository.fetchAll()).thenReturn(List.of(stubbedTransaction));

        // When
        FetchTransactionResponse response = serviceUnderTest.fetchTransaction(aFetchTransactionRequest(expectedTransactionId));

        // Then
        assertEquals(true, response.wasSuccessful());
        assertNotNull(response.getDescription());
        assertNotNull(response.getTransaction());

        TransactionDto resultTransaction = response.getTransaction();
        assertEquals(expectedTransactionId, resultTransaction.getIdentifier());
        assertEquals(expectedTransaction.getDescription(), resultTransaction.getDescription());
        assertEquals(expectedTransaction.getAmount(), resultTransaction.getAmount());
        assertEquals(expectedTransaction.getDateOfTransaction(), resultTransaction.getDateOfTransaction());
        assertEquals(expectedTransaction.getType(), resultTransaction.getType());
//        assertTrue(expectedTransaction.equals(response.getTransaction())); // TODO: Override equals method?
    }


    private Transaction newTransaction() {
        return new Transaction("Test transaction", new BigDecimal(500.25), new Date());
    }

    @Test
    void updateTransactionFailedWithExceptionThenReturnFailedUpdateResponse() throws SQLException {
        // Given
        doThrow(new SQLException("Unable to update the transaction!")).when(mockRepository).update(any()); // Example void function throw exception

        // When
        UpdateTransactionResponse response = serviceUnderTest.updateTransaction(new UpdateTransactionRequest(22, "Test 1", new BigDecimal(532.1), new Date()));

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
        UpdateTransactionResponse response = serviceUnderTest.updateTransaction(new UpdateTransactionRequest(transactionIdToUpdate, expectedDescription, expectedAmount, expectedDate));

        // Then
        assertEquals(true, response.wasSuccessful());
        assertNotNull(response.getDescription());
    }

    @Test
    void deleteTransactionFailedWithExceptionThenReturnFailedDeleteResponse() throws SQLException {
        // Given
        int transactionIdToDelete = 99;

        when(mockRepository.doesIdExist(anyInt())).thenReturn(true);
        doThrow(new SQLException("Unable to connect to database!")).when(mockRepository).remove(anyInt());

        // When
        DeleteTransactionResponse response = serviceUnderTest.deleteTransaction(aDeleteTransactionRequest(transactionIdToDelete));

        // Then
        assertEquals(false, response.wasSuccessful());
        assertNotNull(response.getDescription());
    }

    private DeleteTransactionRequest aDeleteTransactionRequest(int transactionIdToDelete) {
        return new DeleteTransactionRequest(transactionIdToDelete);
    }

    @Test
    void deleteTransactionNonExistingTransactionThenReturnFailedResponse() throws SQLException {
        int transactionIdToDelete = 33;

        when(mockRepository.doesIdExist(anyInt())).thenReturn(false);

        // When
        DeleteTransactionResponse response = serviceUnderTest.deleteTransaction(aDeleteTransactionRequest(transactionIdToDelete));

        // Then
        assertEquals(false, response.wasSuccessful());
        assertNotNull(response.getDescription());
    }

    @Test
    void deleteTransactionSuccessfulThenReturnSuccessfulResponse() throws SQLException {
        // Given
        int transactionIdToDelete = 33;

        when(mockRepository.doesIdExist(anyInt())).thenReturn(true);
        when(mockRepository.remove(anyInt())).thenReturn(true);

        // When
        DeleteTransactionResponse response = serviceUnderTest.deleteTransaction(aDeleteTransactionRequest(transactionIdToDelete));

        // Then
        assertEquals(true, response.wasSuccessful());
        assertNotNull(response.getDescription());
    }
}