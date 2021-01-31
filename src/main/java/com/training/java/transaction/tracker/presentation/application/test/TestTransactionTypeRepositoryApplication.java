package com.training.java.transaction.tracker.presentation.application.test;

import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.data.MySQLDatabase;
import com.training.java.transaction.tracker.dao.TransactionType;
import com.training.java.transaction.tracker.presentation.application.ConfigurationLoader;
import com.training.java.transaction.tracker.repository.TransactionTypeRepository;
import com.training.java.transaction.tracker.repository.TransactionTypeRepositoryImplementation;
import java.sql.SQLException;
import java.util.List;

public class TestTransactionTypeRepositoryApplication {

  public static void main(String[] args) {
    // Load Configuration for database from Properties
    ConfigurationLoader configurationLoader = ConfigurationLoader.getInstance();

    String connectionString = configurationLoader.getConnectionString();
    String username = configurationLoader.getUsername();
    String password = configurationLoader.getPassword();

    Database database = new MySQLDatabase(connectionString, username, password);
    TransactionTypeRepository transactionTypeRepository = new TransactionTypeRepositoryImplementation(
        database);

    Long identifier = testCreateTransactionType(transactionTypeRepository);

    testUpdateTransactionType(identifier, transactionTypeRepository);

    testFetchAllTransactionTypes(transactionTypeRepository);

    testFetchTransactionTypeByDescription(transactionTypeRepository);

    testRemoveTransactionType(transactionTypeRepository);
  }

  private static void testUpdateTransactionType(
      Long identifier,
      TransactionTypeRepository transactionTypeRepository) {
    try {
      TransactionType transactionType = new TransactionType(identifier, "DESCRIPTION");
      transactionTypeRepository.update(transactionType);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void testFetchTransactionTypeByDescription(
      TransactionTypeRepository transactionTypeRepository) {
    try {
      TransactionType transactionType = transactionTypeRepository.fetchByDescription("SALARY");
      System.out
          .println(transactionType.getIdentifier() + " - " + transactionType.getDescription());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static Long testCreateTransactionType(
      TransactionTypeRepository transactionTypeRepository) {
    try {
      return transactionTypeRepository.create("INCOME");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0L;
  }

  private static void testRemoveTransactionType(
      TransactionTypeRepository transactionTypeRepository) {
    try {
      transactionTypeRepository.remove("DESCRIPTION");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void testFetchAllTransactionTypes(
      TransactionTypeRepository transactionTypeRepository) {
    try {
      List<TransactionType> transactionTypes = transactionTypeRepository.fetchAll();
      for (TransactionType transactionType :
          transactionTypes) {
        System.out
            .println(transactionType.getIdentifier() + " - " + transactionType.getDescription());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
