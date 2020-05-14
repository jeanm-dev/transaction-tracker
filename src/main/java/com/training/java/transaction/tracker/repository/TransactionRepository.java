package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionRepository {

    Transaction create(Transaction transaction) throws SQLException;

    boolean remove(int transactionId) throws SQLException;

    void update(Transaction transaction) throws SQLException;

    List<Transaction> fetchAll() throws SQLException;

    boolean doesIdExist(int transactionId) throws SQLException;

    Transaction fetchById(int transactionId) throws SQLException;

    List<Transaction> fetchByDescription(String description) throws SQLException;
}
