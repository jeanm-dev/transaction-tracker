package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.ListInstructionDescription;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.*;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class InputInstruction implements Instruction {

    private InstructionDescription instructionDescription;
    private TransactionRepository transactionRepository;
    private PrintStream printStream;
    private Scanner scanner;

    private List<String> inputFields;

    public InputInstruction(String command, TransactionRepository transactionRepository, PrintStream printStream, Scanner scanner) {
        this.transactionRepository = transactionRepository;
        this.printStream = printStream;
        this.scanner = scanner;

        instructionDescription = new ListInstructionDescription(command);
        inputFields = List.of("Description", "Amount", "Date of Transaction");
    }

    @Override
    public void perform() {
        String description;
        Transaction transaction;
        BigDecimal amount;
        Date dateOfTransaction;

        // Print instructions
        printInstructions(inputFields);

        // Retrieve inputs
        description = retrieveDescription();
        amount = retrieveAmount();
        dateOfTransaction = retrieveDateOfTransaction();

        // Construct Transaction
        transaction = new Transaction(description, amount, dateOfTransaction);

        // DEBUG print transaction
        printTransactionLine(transaction);

        // Store transaction
        storeTransaction(transaction);
    }

    private void storeTransaction(Transaction transaction) {
        try {
            transactionRepository.addTransaction(transaction);
        } catch (SQLException exception) {
            printStream.println("Unable to store transaction!");
            printStream.println("Please try again!");
        }
    }

    private void printInstructions(List<String> inputFields) {
        printStream.println("Please enter the following values:");

        for (String inputField : inputFields) {
            printStream.println(String.format(" # %s:", inputField));
        }
    }

    private String retrieveDescription() {
        printStream.println(createInputMessageForField(inputFields.get(0)));
        return scanner.nextLine();
    }

    private BigDecimal retrieveAmount() {
        printStream.println(createInputMessageForField(inputFields.get(1)));
        try {
            BigDecimal value = scanner.nextBigDecimal();

            scanner.nextLine(); //Clears input

            return value;
        } catch (Exception exception) {
            printStream.println("Invalid amount entered!\nPlease enter a value with a valid format!\n\t0.00");
            scanner.nextLine();
            return retrieveAmount();
        }
    }

    private Date retrieveDateOfTransaction() {
        printStream.println(createInputMessageForField(inputFields.get(2)));
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String input = scanner.nextLine();

        try {
            return (Date) dateFormatter.parseObject(input);
        } catch (ParseException exception) {
            printStream.println("Invalid date format! Please a date value that matches (YYYY-MM-DD)");
            exception.printStackTrace();
            return retrieveDateOfTransaction();
        }
    }

    private String createInputMessageForField(String field) {
        return String.format("\n(%s): ", field);
    }

    @Override
    public String getInstructionMenuDescription() {
        return instructionDescription.getMenuDescription();
    }

    private void printTransactionLine(Transaction transaction) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String formattedAmount = decimalFormat.format(transaction.getAmount());
        String formattedDate = dateFormatter.format(transaction.getDateOfTransaction());

        String transactionLine = String.format("%s \t %s \t %s", transaction.getDescription(), formattedAmount, formattedDate);
        printStream.println(transactionLine);
    }
}
