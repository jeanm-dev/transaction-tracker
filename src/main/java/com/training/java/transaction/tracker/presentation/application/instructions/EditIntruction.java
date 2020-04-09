package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.EditInstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class EditIntruction implements Instruction {

    private final PrintStream printStream;
    private final Scanner scanner;
    private InstructionDescription instructionDescription;
    private TransactionRepository transactionRepository;

    public EditIntruction(String command, TransactionRepository transactionRepository, PrintStream printStream, Scanner scanner) {
        this.transactionRepository = transactionRepository;
        this.printStream = printStream;
        this.scanner = scanner;

        instructionDescription = new EditInstructionDescription(command);
    }

    @Override
    public void perform() {
        // Retrieve number of transactions from the database
        int numberOfTransactions = fetchNumberOfTransactions();

        if (numberOfTransactions <= 0) {
            printStream.println("No transactions available to edit!");
            return;
        }

        // Ask user which transaction they wish to edit (index)
        int indexOfSelectedTransaction = 0;
        if (numberOfTransactions > 1) {
            //Ask user which transaction they wish to edit?
            String prompt = String.format("Index in range(%s-%s):", 1, numberOfTransactions + 1);
            indexOfSelectedTransaction = retrieveIndexOfTransaction(prompt) - 1;
        }

        // Prompt user to change fields (description, amount, dateOfTransaction)
        // Fetch item at index
        Transaction transaction = fetchTransaction(indexOfSelectedTransaction);

        String newDescription = retrieveDescription(transaction.getDescription());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String currentAmount = decimalFormat.format(transaction.getAmount());
        BigDecimal newAmount = retrieveAmount(currentAmount);

        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormatter.format(transaction.getDateOfTransaction());
        Date newDateOfTransaction = retrieveDateOfTransaction(currentDate);

        // TODO: Prompt are you sure Y/N?
        printStream.println("Are you sure you would like to update this transaction?");
        List<String> optionList = List.of("Y", "N");
        String selectedOption = retrieveMatchingInput(optionList);

        if (optionList.indexOf(selectedOption) == 0) {
            // Save updates to database
            transaction.setDescription(newDescription);
            transaction.setAmount(newAmount);
            transaction.setDateOfTransaction(newDateOfTransaction);
            updateTransaction(transaction);
            printStream.println("Transaction was updated successfully!");
        } else {
            printStream.println("Updates were not saved!");
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

    private void updateTransaction(Transaction transaction) {
        try {
            transactionRepository.updateTransaction(transaction);
        } catch (SQLException e) {
            printStream.println("Unable to save updates!\nPlease try again later!");
//            e.printStackTrace();
        }
    }

    @Override
    public String getCommand() {
        return instructionDescription.getCommand();
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

    private String retrieveDescription(String inputPrefix) {
        printStream.println(createInputMessageForField(inputPrefix));
        return scanner.nextLine();
    }

    private BigDecimal retrieveAmount(String inputPrefix) {
        printStream.println(createInputMessageForField(inputPrefix));
        try {
            BigDecimal value = scanner.nextBigDecimal();

            scanner.nextLine(); //Clears input

            return value;
        } catch (Exception exception) {
            printStream.println("Invalid amount entered!\nPlease enter a value with a valid format!\n\t0.00");
            scanner.nextLine();
            return retrieveAmount(inputPrefix);
        }
    }

    private Date retrieveDateOfTransaction(String inputPrefix) {
        printStream.println(createInputMessageForField(inputPrefix));
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String input = scanner.nextLine();

        try {
            return (Date) dateFormatter.parseObject(input);
        } catch (ParseException exception) {
            printStream.println("Invalid date format! Please a date value that matches (YYYY-MM-DD)");
//            exception.printStackTrace();
            return retrieveDateOfTransaction(inputPrefix);
        }
    }

    private String createInputMessageForField(String field) {
        return String.format("\n(%s): ", field);
    }
    //TODO: Refactor into Reusable component - END
}
