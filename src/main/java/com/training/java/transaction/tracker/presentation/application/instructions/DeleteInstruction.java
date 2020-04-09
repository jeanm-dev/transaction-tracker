package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.DeleteInstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DeleteInstruction implements Instruction {

    private final TransactionRepository transactionRepository;
    private final PrintStream printStream;
    private final Scanner scanner;
    private InstructionDescription instructionDescription;

    public DeleteInstruction(String command, TransactionRepository transactionRepository, PrintStream printStream, Scanner scanner) {
        this.transactionRepository = transactionRepository;
        this.printStream = printStream;
        this.scanner = scanner;

        instructionDescription = new DeleteInstructionDescription(command);
    }

    @Override
    public void perform() {
        // Retrieve number of transactions from the database
        int numberOfTransactions = fetchNumberOfTransactions();

        if (numberOfTransactions <= 0) {
            printStream.println("No transactions available to edit!");
            return;
        }

        // Ask user which transaction they wish to delete (index)
        int indexOfSelectedTransaction = 0;
        if (numberOfTransactions > 1) {
            //Ask user which transaction they wish to edit?
            String prompt = String.format("Index in range(%s-%s):", 1, numberOfTransactions + 1);
            indexOfSelectedTransaction = retrieveIndexOfTransaction(prompt) - 1;
        }

        // Fetch item at index
        Transaction transaction = fetchTransaction(indexOfSelectedTransaction);

        // Prompt are you sure Y/N?
        printStream.println("Are you sure you would like to update this transaction?");
        List<String> optionList = List.of("Y", "N");
        String selectedOption = retrieveMatchingInput(optionList);

        if (optionList.indexOf(selectedOption) == 0) {
            // Delete transaction from database
            deleteTransaction(transaction);
            printStream.println("Transaction was deleted successfully!");
        } else {
            printStream.println("Transaction was not deleted!");
        }
    }

    private void deleteTransaction(Transaction transaction) {
        try {
            transactionRepository.removeTransaction(transaction);
        } catch (SQLException exception) {
            printStream.println("Unable to remove transaction!\nPlease try again!");
//            exception.printStackTrace();
        }
    }

    private Transaction fetchTransaction(int index) {
        Transaction transaction = null;
        try {
            List<Transaction> transactions = transactionRepository.fetchAllTransactions();
            transaction = transactions.get(index);
        } catch (SQLException exception) {
            printStream.println("Unable to fetch number of transactions from the database");
        }
        return transaction;
    }

    private int fetchNumberOfTransactions() {
        int numberOfTransactions = -1;
        try {
            numberOfTransactions = transactionRepository.fetchAllTransactions().size();
        } catch (SQLException exception) {
            printStream.println("Unable to fetch number of transactions from the database");
        }
        return numberOfTransactions;
    }

    @Override
    public String getInstructionMenuDescription() {
        return instructionDescription.getMenuDescription();
    }

    //TODO: Refactor into Reusable component - START
    private String createInputMessageMatchingInputs(List<String> expectedInputs) {
        StringBuilder builder = new StringBuilder();

        for (String input : expectedInputs) {
            builder.append(input);
            builder.append("/");
        }
        builder.deleteCharAt(builder.length() - 1);

        return String.format("%s", builder.toString());
    }

    private String retrieveMatchingInput(List<String> expectedInputs) {
        String optionsMessage = createInputMessageMatchingInputs(expectedInputs);
        printStream.println(createInputMessageForField(optionsMessage));

        String input = scanner.nextLine();

        if (expectedInputs.contains(input)) {
            return input;
        } else {
            printStream.print("Invalid input!\nPlease select one of the following options! ");
            printStream.println(optionsMessage);
            return retrieveMatchingInput(expectedInputs);
        }
    }

    private int retrieveIndexOfTransaction(String inputPrefix) {
        printStream.println(createInputMessageForField(inputPrefix));

        int returnValue = scanner.nextInt();
        scanner.nextLine(); //Clears input

        return returnValue;
    }

    private String createInputMessageForField(String field) {
        return String.format("\n(%s): ", field);
    }
    //TODO: Refactor into Reusable component - END
}
