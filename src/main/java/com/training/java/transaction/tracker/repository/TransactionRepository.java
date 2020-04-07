package com.training.java.transaction.tracker.repository;

import java.sql.SQLException;
import java.util.List;

import com.training.java.transaction.tracker.domainobject.Transaction;

public interface TransactionRepository {

    void addTransaction(Transaction transaction) throws SQLException;

    void removeTransaction(Transaction transaction) throws SQLException;

    void updateTransaction(Transaction transaction) throws SQLException;

    List<Transaction> fetchAllTransactions() throws SQLException;
}
