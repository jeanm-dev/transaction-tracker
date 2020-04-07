package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.domainobject.Transaction;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImplementation implements TransactionRepository {

    private Database database;

    public TransactionRepositoryImplementation(Database database) {
        this.database = database;
    }

    @Override
    public void addTransaction(Transaction transaction) throws SQLException {
        String createStatement = "INSERT INTO Transactions (description, amount, dateOfTransaction) VALUES (?, ?, ?)";
        Connection connection = database.getConnection();

        Date transactionDate = new Date(transaction.getDateOfTransaction().getTime());

        PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
        preparedStatement.setString(1, transaction.getDescription());
        preparedStatement.setBigDecimal(2, transaction.getAmount());
        preparedStatement.setDate(3, transactionDate);

        preparedStatement.execute();

        preparedStatement.close();
    }

    @Override
    public void removeTransaction(Transaction transaction) throws SQLException {
        String deleteStatement = "DELETE FROM Transactions WHERE transactionId = ?";
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
        preparedStatement.setInt(1, transaction.getIdentifier());

        preparedStatement.execute();

        preparedStatement.close();
    }

    @Override
    public void updateTransaction(Transaction transaction) throws SQLException {
        String updateStatement = "UPDATE Transactions SET description = ?, amount = ?, dateOfTransaction = ? WHERE transactionId = ?";
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(updateStatement);

        Date transactionDate = new Date(transaction.getDateOfTransaction().getTime());
        preparedStatement.setString(1, transaction.getDescription());
        preparedStatement.setBigDecimal(2, transaction.getAmount());
        preparedStatement.setDate(3, transactionDate);

        preparedStatement.setInt(4, transaction.getIdentifier());

        preparedStatement.execute();

        preparedStatement.close();
    }

    @Override
    public List<Transaction> fetchAllTransactions() throws SQLException {
        List<Transaction> transactions = null;
        String fetchStatement = "SELECT transactionId, description, amount, dateOfTransaction FROM Transactions";
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(fetchStatement);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            if (transactions == null) transactions = new ArrayList<>();

            Transaction transaction = mapToTransaction(resultSet);
            transactions.add(transaction);
        }

        preparedStatement.close();

        return transactions;
    }

    private Transaction mapToTransaction(ResultSet resultSet) throws SQLException {
        if (!resultSet.isClosed()) {

            Transaction transaction = new Transaction();

            transaction.setAmount(resultSet.getBigDecimal("amount"));
            transaction.setDescription(resultSet.getString("description"));
            transaction.setDateOfTransaction(resultSet.getDate("dateOfTransaction"));
            transaction.setIdentifier(resultSet.getInt("transactionId"));

            return transaction;
        }
        return null;
    }
}

