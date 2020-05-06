package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.domainobject.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImplementation implements TransactionRepository {

    private static final String TABLE_NAME = "TransactionEntries";
    private static final String TRANSACTION_ID_COLUMN = "transactionId";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String AMOUNT_COLUMN = "amount";
    private static final String DATE_OF_TRANSACTION_COLUMN = "dateOfTransaction";

    // Used Hardcoded strings instead - could be useful down the line to add generics with String.format() function
    private static final String ADD_STATEMENT = "INSERT INTO " + TABLE_NAME + " (" + DESCRIPTION_COLUMN + ", " + AMOUNT_COLUMN + " , " + DATE_OF_TRANSACTION_COLUMN + ") VALUES (?, ?, ?)";
    private static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE " + TRANSACTION_ID_COLUMN + " = ?;";
    private static final String UPDATE_STATEMENT = "UPDATE " + TABLE_NAME + " SET " + DESCRIPTION_COLUMN + " = ?, " + AMOUNT_COLUMN + " = ?, " + DATE_OF_TRANSACTION_COLUMN + " = ? WHERE " + TRANSACTION_ID_COLUMN + " = ?";
    private static final String SELECT_ALL_STATEMENT = "SELECT " + TRANSACTION_ID_COLUMN + ", " + DESCRIPTION_COLUMN + ", " + AMOUNT_COLUMN + ", " + DATE_OF_TRANSACTION_COLUMN + " FROM " + TABLE_NAME;
    // TODO: Try writing the most specific SQL statements you can
    // Optimise the traffic
    private static final String SELECT_ID_STATEMENT = "SELECT " + TRANSACTION_ID_COLUMN + ", " + DESCRIPTION_COLUMN + ", " + AMOUNT_COLUMN + ", " + DATE_OF_TRANSACTION_COLUMN + " FROM " + TABLE_NAME + " WHERE " + TRANSACTION_ID_COLUMN + " = ?";
    private static final String SELECT_DESCRIPTION_STATEMENT = "SELECT " + TRANSACTION_ID_COLUMN + ", " + DESCRIPTION_COLUMN + ", " + AMOUNT_COLUMN + ", " + DATE_OF_TRANSACTION_COLUMN + " FROM " + TABLE_NAME + " WHERE " + DESCRIPTION_COLUMN + " LIKE ?;";

    private Database database;

    public TransactionRepositoryImplementation(Database database) {
        this.database = database;
    }

    @Override
    public Transaction create(Transaction transaction) throws SQLException {
        Connection connection = database.getConnection();

        java.util.Date date = transaction.getDateOfTransaction();
        Date transactionDate = new Date(date.getTime());

        PreparedStatement preparedStatement = connection.prepareStatement(ADD_STATEMENT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, transaction.getDescription());
        preparedStatement.setBigDecimal(2, transaction.getAmount());
        preparedStatement.setDate(3, transactionDate, java.util.Calendar.getInstance()); //Fix time bug

        preparedStatement.execute();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            int transactionId = resultSet.getInt(1);
            transaction.setIdentifier(transactionId);
        }

        preparedStatement.close();

        return transaction;
    }

    @Override
    public boolean remove(int transactionId) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
        preparedStatement.setInt(1, transactionId);

        preparedStatement.execute();

        // Determine if changes where applied
        int updateCount = preparedStatement.getUpdateCount();

        preparedStatement.close();

        return updateCount > 0;
    }

    @Override
    public void update(Transaction transaction) throws SQLException {
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
    public List<Transaction> fetchAll() throws SQLException {
        List<Transaction> transactions = null;
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STATEMENT);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            if (transactions == null)
                transactions = new ArrayList<>();

            Transaction transaction = mapToTransaction(resultSet);
            transactions.add(transaction);
        }

        preparedStatement.close();

        return transactions;
    }

    @Override
    public boolean doesIdExist(int transactionId) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_STATEMENT); // TODO: Make SQL Statement specific
        preparedStatement.setInt(0, transactionId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            return true;
        }

        preparedStatement.close();

        return false;
    }

    @Override
    public Transaction fetchById(int transactionId) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_STATEMENT);
        preparedStatement.setInt(0, transactionId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            return mapToTransaction(resultSet);
        }

        preparedStatement.close();

        return null;
    }

    @Override
    public List<Transaction> fetchByDescription(String description) throws SQLException {
        List<Transaction> transactions = null;
        Connection connection = database.getConnection();

        String formattedDescription = "%" + description +"%";

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DESCRIPTION_STATEMENT);
        preparedStatement.setString(1, formattedDescription);

        System.out.println(preparedStatement.toString());

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            if (transactions == null)
                transactions = new ArrayList<>();

            Transaction transaction = mapToTransaction(resultSet);
            transactions.add(transaction);
        }

        preparedStatement.close();

        return transactions;
    }

    private Transaction mapToTransaction(ResultSet resultSet) throws SQLException {
        if (!resultSet.isClosed()) {

            Transaction transaction = new Transaction();

            transaction.setAmount(resultSet.getBigDecimal(AMOUNT_COLUMN));
            transaction.setDescription(resultSet.getString(DESCRIPTION_COLUMN));
            transaction.setDateOfTransaction(resultSet.getDate(DATE_OF_TRANSACTION_COLUMN));
            transaction.setIdentifier(resultSet.getInt(TRANSACTION_ID_COLUMN));

            return transaction;
        }
        return null;
    }
}