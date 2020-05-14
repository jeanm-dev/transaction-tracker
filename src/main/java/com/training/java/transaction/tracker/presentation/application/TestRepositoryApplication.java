package com.training.java.transaction.tracker.presentation.application;

import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.data.MySQLDatabase;
import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.repository.TransactionRepository;
import com.training.java.transaction.tracker.repository.TransactionRepositoryImplementation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestRepositoryApplication {

    public static void main(String[] args) {

        // Load Configuration for database from Properties
        ConfigurationLoader configurationLoader = ConfigurationLoader.getInstance();

        String connectionString = configurationLoader.getConnectionString();
        String username = configurationLoader.getUsername();
        String password = configurationLoader.getPassword();

        Database database = new MySQLDatabase(connectionString, username, password);
        TransactionRepository repository = new TransactionRepositoryImplementation(database);

        // Fetch all transactions
        List<Transaction> transactionList = fetchAllTransactions(repository);
        printAllFetchedTransactions(transactionList);

        // Add 7 transactions
        List<Transaction> addTransactionList = createRandomTransactions(7);
        addTransactions(repository, addTransactionList);

        //Print all transactions
        transactionList = fetchAllTransactions(repository);
        printAllFetchedTransactions(transactionList);

        //Prefix all transactions
        transactionList = fetchAllTransactions(repository);
        updatePrefixTransactions(transactionList, repository);
        //Print all transactions
        transactionList = fetchAllTransactions(repository);
        printAllFetchedTransactions(transactionList);

        //Delete all transactions
        transactionList = fetchAllTransactions(repository);
        deleteTransactions(transactionList, repository);
        //Print all transactions
        transactionList = fetchAllTransactions(repository);
        printAllFetchedTransactions(transactionList);

        //Tests repository method for checking if an entry is present in the database
        testTransactionExists(repository);
    }

    private static void deleteTransactions(List<Transaction> transactions, TransactionRepository repository) {
        for (Transaction transaction : transactions) {
            try {
                repository.remove(transaction.getIdentifier());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void updatePrefixTransactions(List<Transaction> transactions, TransactionRepository repository) {
        for (Transaction transaction : transactions) {
            transaction.setDescription("Prefixed ! - " + transaction.getDescription());
            try {
                repository.update(transaction);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Transaction> fetchAllTransactions(TransactionRepository repository) {
        List<Transaction> transactions = null;
        try {
            transactions = repository.fetchAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    private static void printAllFetchedTransactions(List<Transaction> transactions) {
        if (transactions == null) {
            System.out.println("======================");
            System.out.println("No transactions found!");
            System.out.println("======================");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println("Transaction description: " + transaction.getDescription());
        }
    }

    private static void addTransactions(TransactionRepository repository, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            try {
                repository.create(transaction);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Transaction> createRandomTransactions(int count) {
        List<Transaction> transactionList = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            LocalDate localDate = LocalDate.of(2020, 3, (i + 1) % 30);

            Transaction transaction = new Transaction();
            transaction.setDescription("Description #" + i);
            transaction.setAmount(new BigDecimal(i * 100));
            transaction.setDateOfTransaction(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            transactionList.add(transaction);
        }

        return transactionList;
    }

    // Transaction exists test
    private static void testTransactionExists(TransactionRepository transactionRepository) {
        ;
        try {
            boolean b = transactionRepository.doesIdExist(100);
            System.out.println("Transaction 100 exists? " + b);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            boolean b = transactionRepository.doesIdExist(1);
            System.out.println("Transaction 1 exists? " + b);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
