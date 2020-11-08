package com.training.java.transaction.tracker.presentation.application.test;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.data.MySQLDatabase;
import com.training.java.transaction.tracker.presentation.application.ConfigurationLoader;
import com.training.java.transaction.tracker.repository.RepositoryBase;
import com.training.java.transaction.tracker.repository.TransactionTableDescriptor;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TestBaseRepositoryForTransactionsApplication {

  public static void main(String[] args) {
    // Load Configuration for database from Properties
    ConfigurationLoader configurationLoader = ConfigurationLoader.getInstance();

    String connectionString = configurationLoader.getConnectionString();
    String username = configurationLoader.getUsername();
    String password = configurationLoader.getPassword();

    Database database = new MySQLDatabase(connectionString, username, password); // Replace with derby / H2 (in memory DB) / Flyway DB
    RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase = new RepositoryBase<>(
        database, new TransactionTableDescriptor());

    // Test isValid function
    testIsValidTransaction(repositoryBase);

    // Test insert transaction
    Transaction insertedTransaction = testInsertTransaction(repositoryBase);

    // Test insert invalid transaction
    testInsertTransactionWithInvalidType(repositoryBase);

    // Test transaction entry exists
    testTransactionExists(repositoryBase, insertedTransaction);

    // Test retrieving transaction exists
    testFetchTransactionById(repositoryBase, insertedTransaction);

    // Test update values of transaction
    testUpdateTransactionById(repositoryBase, insertedTransaction);

    // Test delete transaction works
    testDeleteExistingTransaction(repositoryBase, insertedTransaction);

    // Test fetch and print all transactions in the database
    testCanFetchAllTransactionsAndOutput(repositoryBase);
  }

  private static void testCanFetchAllTransactionsAndOutput(RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase) {
    try {
      List<Transaction> transactionList = repositoryBase.fetchAll();
      System.out.println("Number of transactions: " + transactionList.size());
      for(int i = 0; i < transactionList.size(); i++) {
        System.out.println("Transaction " + i + ": " + transactionList.get(i).toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testUpdateTransactionById(RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase, Transaction insertedTransaction) {
    try {
      Transaction result = repositoryBase.fetchById(insertedTransaction.getIdentifier());
      System.out.println(
          "Contains transaction with id (" + insertedTransaction.getIdentifier() + ") : " + result.toString());
      result.setDescription("To be deleted!");
      result.setDateOfTransaction(new Date());
      result.setAmount(new BigDecimal(200));

      repositoryBase.update(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testFetchTransactionById(
      RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase, Transaction insertedTransaction) {
    try {
      Transaction result = repositoryBase.fetchById(insertedTransaction.getIdentifier());
      System.out.println(
          "Contains transaction with id (" + insertedTransaction.getIdentifier() + ") : " + result.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testDeleteExistingTransaction(
      RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase,
      Transaction insertedTransaction) {
    long transactionId = insertedTransaction.getIdentifier();
    try {
      boolean result = repositoryBase.remove(transactionId);
      System.out.println("Successfully " + result + " removed/deleted transaction with id: " + transactionId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void testIsValidTransaction(
      RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase) {

    Transaction invalidTransaction = new Transaction();
    System.out.println("Is valid transaction: " + repositoryBase.isValid(invalidTransaction));
  }

  private static Transaction testInsertTransaction(
      RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase) {

    Transaction insertTransaction = makeInsertTransaction();
    System.out.println("Is valid transaction: " + repositoryBase.isValid(insertTransaction));
    try {
      Transaction result = repositoryBase.create(insertTransaction);
      System.out.println("Successfully inserted transaction: " + result);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void testInsertTransactionWithInvalidType(
      RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase) {

    Transaction insertTransaction = makeInsertTransaction();
    insertTransaction.setType(Integer.MAX_VALUE);
    System.out.println("Is valid transaction: " + repositoryBase.isValid(insertTransaction));
    try {
      Transaction result = repositoryBase.create(insertTransaction);
      System.out.println("Successfully inserted transaction: " + result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testTransactionExists(
      RepositoryBase<Transaction, TransactionTableDescriptor> repositoryBase,
      Transaction insertedTransaction) {

    try {
      boolean result = repositoryBase.doesIdExist(insertedTransaction.getIdentifier());
      System.out.println(
          "Contains transaction with id (" + insertedTransaction.getIdentifier() + ") : " + (result
              ? "Yes" : "No"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Transaction makeInsertTransaction() {
    // Test insert function
    Transaction insertTransaction = new Transaction();

    insertTransaction.setAmount(new BigDecimal(400));
    insertTransaction.setDescription("Test Insert Transaction");
    insertTransaction.setDateOfTransaction(new Date());

    return insertTransaction;
  }
}
