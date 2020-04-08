package com.training.java.transaction.tracker.presentation.application;

import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.data.MySQLDatabase;
import com.training.java.transaction.tracker.repository.TransactionRepository;
import com.training.java.transaction.tracker.repository.TransactionRepositoryImplementation;

import java.util.Scanner;

public class ConsoleApplication {

    private static final String STAR_DIVIDER = "**************";

    public static void main(String[] args) {
        printWelcomeMessage();

        Scanner scanner = new Scanner(System.in);

        Menu coordinator = new Menu(scanner, System.out, createTransactionRepository());
        coordinator.start();
    }

    private static void printWelcomeMessage() {
        String welcomeMessage = String.format("%s\n%s\n%s", STAR_DIVIDER, "Welcome", STAR_DIVIDER);
        println(welcomeMessage);
    }


    private static void println(String string) {
        System.out.println(string);
    }

    private static TransactionRepository createTransactionRepository() {
        //TODO: Load from Properties
        String connectionString = "jdbc:mysql://localhost:3306/TransactionTracker";
        String username = "root";
        String password = "my-secret";

        Database database = new MySQLDatabase(connectionString, username, password);
        TransactionRepository repository = new TransactionRepositoryImplementation(database);

        return repository;
    }
}
