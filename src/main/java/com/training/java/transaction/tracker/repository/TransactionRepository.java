package com.training.java.transaction.tracker.repository;

import java.sql.SQLException;
import java.util.List;

import com.training.java.transaction.tracker.domainobject.Transaction;

public interface TransactionRepository {

    void create(Transaction transaction) throws SQLException;

    void remove(int transactionId) throws SQLException;

    void update(Transaction transaction) throws SQLException;

    List<Transaction> fetchAll() throws SQLException;



}
