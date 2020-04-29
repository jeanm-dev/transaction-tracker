package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InputInstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.presentation.interaction.CommandLine;
import com.training.java.transaction.tracker.presentation.interaction.InvalidInputException;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.*;
import java.util.Date;
import java.util.List;

public class InputInstruction implements Instruction {

    private CommandLine commandLine;
    private InstructionDescription instructionDescription;
    private TransactionRepository transactionRepository;

    private List<String> inputFields;

    public InputInstruction(String command, TransactionRepository transactionRepository, CommandLine commandLine) {
        this.transactionRepository = transactionRepository;
        this.commandLine = commandLine;

        instructionDescription = new InputInstructionDescription(command);
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
            transactionRepository.create(transaction);
        } catch (SQLException exception) {
            commandLine.printWithNewLine("Unable to store transaction!");
            commandLine.printWithNewLine("Please try again!");
        }
    }

    private void printInstructions(List<String> inputFields) {
        commandLine.printWithNewLine("Please enter the following values:");

        for (String inputField : inputFields) {
            commandLine.printWithNewLine(String.format(" # %s:", inputField));
        }
    }

    //TODO: Refactor into Reusable component - START
    private String retrieveDescription() {
        commandLine.printWithNewLine(createInputMessageForField(inputFields.get(0)));
        return commandLine.readLine();
    }

    private BigDecimal retrieveAmount() {
        commandLine.printWithNewLine(createInputMessageForField(inputFields.get(1)));

        try {
            return commandLine.readBigDecimal();
        } catch (Exception exception) {
            commandLine.printWithNewLine(exception.getMessage());
            return retrieveAmount();
        }
    }

    private Date retrieveDateOfTransaction() {
        commandLine.printWithNewLine(createInputMessageForField(inputFields.get(2)));

        try {
            return commandLine.readDate();
        } catch (InvalidInputException exception) {
            commandLine.printWithNewLine(exception.getMessage());
            return retrieveDateOfTransaction();
        }
    }
    //TODO: Refactor into Reusable component - END

    private String createInputMessageForField(String field) {
        return String.format("\n(%s): ", field);
    }

    @Override
    public String getCommand() {
        return instructionDescription.getCommand();
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
        commandLine.printWithNewLine(transactionLine);
    }
}
