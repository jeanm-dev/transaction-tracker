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

    private static final String TABLE_NAME = "TransactionEntries";
    private static final String TRANSACTION_ID_COLUMN = "transactionId";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String DATE_OF_TRANSACTION_COLUMN = "dateOfTransaction";

    private static final String ADD_STATEMENT = String.format("INSERT INTO %s (%s, %s, %S) VALUES (?, ?, ?)", TABLE_NAME, DESCRIPTION_COLUMN, AMOUNT_COLUMN, DATE_OF_TRANSACTION_COLUMN);
    private static final String DELETE_STATEMENT = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, TRANSACTION_ID_COLUMN);
    private static final String UPDATE_STATEMENT = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?", TABLE_NAME, DESCRIPTION_COLUMN, AMOUNT_COLUMN, DATE_OF_TRANSACTION_COLUMN, TRANSACTION_ID_COLUMN);
    private static final String SELECT_ALL_STATEMENT = String.format("SELECT %s, %s, %s, %s FROM %s", TRANSACTION_ID_COLUMN, DESCRIPTION_COLUMN, AMOUNT_COLUMN, DATE_OF_TRANSACTION_COLUMN, TABLE_NAME);

    private Database database;

    public TransactionRepositoryImplementation(Database database) {
        this.database = database;
    }

    @Override
    public void addTransaction(Transaction transaction) throws SQLException {
        Connection connection = database.getConnection();

        Date transactionDate = new Date(transaction.getDateOfTransaction().getTime());

        PreparedStatement preparedStatement = connection.prepareStatement(ADD_STATEMENT);
        preparedStatement.setString(1, transaction.getDescription());
        preparedStatement.setBigDecimal(2, transaction.getAmount());
        preparedStatement.setDate(3, transactionDate);

        preparedStatement.execute();

        preparedStatement.close();
    }

    @Override
    public void removeTransaction(Transaction transaction) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
        preparedStatement.setInt(1, transaction.getIdentifier());

        preparedStatement.execute();

        preparedStatement.close();
    }

    @Override
    public void updateTransaction(Transaction transaction) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT);

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
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STATEMENT);

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
