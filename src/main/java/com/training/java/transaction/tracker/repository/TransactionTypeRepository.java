package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.domainobject.TransactionType;
import java.sql.SQLException;
import java.util.List;

public interface TransactionTypeRepository {

  int create(String transactionType) throws SQLException;

  boolean remove(String income) throws SQLException;

  List<TransactionType> fetchAll() throws SQLException;

  TransactionType fetchByDescription(String description) throws SQLException;

  void update(TransactionType transactionType) throws SQLException;
}
