package com.training.java.transaction.tracker.presentation.application.test;

import com.training.java.transaction.tracker.dao.TransactionType;
import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.data.MySQLDatabase;
import com.training.java.transaction.tracker.presentation.application.ConfigurationLoader;
import com.training.java.transaction.tracker.repository.RepositoryBase;
import com.training.java.transaction.tracker.repository.TransactionTypeTableDescriptor;
import java.sql.SQLException;
import java.util.List;

public class TestBaseRepositoryForTransactionTypeApplication {

  public static void main(String[] args) {
    // Load Configuration for database from Properties
    ConfigurationLoader configurationLoader = ConfigurationLoader.getInstance();

    String connectionString = configurationLoader.getConnectionString();
    String username = configurationLoader.getUsername();
    String password = configurationLoader.getPassword();

    Database database = new MySQLDatabase(connectionString, username,
        password); // Replace with derby / H2 (in memory DB) / Flyway DB
    RepositoryBase<TransactionType, TransactionTypeTableDescriptor> repositoryBase = new RepositoryBase<>(
        database, new TransactionTypeTableDescriptor());

    TransactionType transactionType = testCreateTransactionType(repositoryBase);

    testUpdateTransactionType(transactionType.getIdentifier(), repositoryBase);

    testFetchAllTransactionTypes(repositoryBase);

    testRemoveTransactionType(repositoryBase, transactionType.getIdentifier());
  }

  private static void testUpdateTransactionType(
      Long identifier,
      RepositoryBase<TransactionType, TransactionTypeTableDescriptor> repositoryBase) {
    try {
      TransactionType transactionType = new TransactionType(identifier, "DESCRIPTION");
      repositoryBase.update(transactionType);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static TransactionType testCreateTransactionType(
      RepositoryBase<TransactionType, TransactionTypeTableDescriptor> repositoryBase) {
    try {
      TransactionType transactionType = new TransactionType(7L, "NEW TYPE");
      return repositoryBase.create(transactionType);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void testRemoveTransactionType(
      RepositoryBase<TransactionType, TransactionTypeTableDescriptor> repositoryBase,
      long identifier) {
    try {
      repositoryBase.remove(identifier);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void testFetchAllTransactionTypes(
      RepositoryBase<TransactionType, TransactionTypeTableDescriptor> repositoryBase) {
    try {
      List<TransactionType> transactionTypes = repositoryBase.fetchAll();
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
