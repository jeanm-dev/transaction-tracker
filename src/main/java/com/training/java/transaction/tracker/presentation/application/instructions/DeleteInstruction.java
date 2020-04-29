package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.DeleteInstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.presentation.interaction.CommandLine;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class DeleteInstruction implements Instruction {

    private final TransactionRepository transactionRepository;
    private final CommandLine commandLine;
    private InstructionDescription instructionDescription;

    public DeleteInstruction(String command, TransactionRepository transactionRepository, CommandLine commandLine) {
        this.transactionRepository = transactionRepository;
        this.commandLine = commandLine;

        instructionDescription = new DeleteInstructionDescription(command);
    }

    @Override
    public void perform() {
        // Retrieve number of transactions from the database
        int numberOfTransactions = fetchNumberOfTransactions();

        if (numberOfTransactions <= 0) {
            commandLine.printWithNewLine("No transactions available to edit!");
            return;
        }

        // Ask user which transaction they wish to delete (index)
        int indexOfSelectedTransaction = 0;
        if (numberOfTransactions > 1) {
            //Ask user which transaction they wish to edit?
            String prompt = String.format("Index in range(%s-%s):", 1, numberOfTransactions);
            indexOfSelectedTransaction = retrieveIndexOfTransaction(prompt) - 1;
        }

        // Fetch item at index
        Transaction transaction = fetchTransaction(indexOfSelectedTransaction);

        // Prompt are you sure Y/N?
        printTransactionLine(transaction);
        commandLine.printWithNewLine("Are you sure you would like delete this transaction?");
        List<String> optionList = List.of("Y", "N");
        String selectedOption = retrieveMatchingInput(optionList);

        if (optionList.indexOf(selectedOption) == 0) {
            // Delete transaction from database
            deleteTransaction(transaction);
            commandLine.printWithNewLine("Transaction was deleted successfully!");
        } else {
            commandLine.printWithNewLine("Transaction was not deleted!");
        }
    }

    private void deleteTransaction(Transaction transaction) {
        try {
            transactionRepository.remove(transaction.getIdentifier());
        } catch (SQLException exception) {
            commandLine.printWithNewLine("Unable to remove transaction!\nPlease try again!");
//            exception.printStackTrace();
        }
    }

    private Transaction fetchTransaction(int index) {
        Transaction transaction = null;
        try {
            List<Transaction> transactions = transactionRepository.fetchAll();
            transaction = transactions.get(index);
        } catch (SQLException exception) {
            commandLine.printWithNewLine("Unable to fetch number of transactions from the database");
        }
        return transaction;
    }

    private int fetchNumberOfTransactions() {
        int numberOfTransactions = -1;
        try {
            numberOfTransactions = transactionRepository.fetchAll().size();
        } catch (SQLException exception) {
            commandLine.printWithNewLine("Unable to fetch number of transactions from the database");
        }
        return numberOfTransactions;
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
        commandLine.printWithNewLine(createInputMessageForField(optionsMessage));

        String input = commandLine.readLine();

        if (expectedInputs.contains(input)) {
            return input;
        } else {
            commandLine.print("Invalid input!\nPlease select one of the following options! ");
            commandLine.printWithNewLine(optionsMessage);
            return retrieveMatchingInput(expectedInputs);
        }
    }

    private int retrieveIndexOfTransaction(String inputPrefix) {
        commandLine.printWithNewLine(createInputMessageForField(inputPrefix));
        return commandLine.readInt();
    }

    private String createInputMessageForField(String field) {
        return String.format("\n(%s): ", field);
    }

    private void printTransactionLine(Transaction transaction) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String formattedAmount = decimalFormat.format(transaction.getAmount());
        String formattedDate = dateFormatter.format(transaction.getDateOfTransaction());

        String transactionLine = String.format("%s \t %s \t %s", transaction.getDescription(), formattedAmount, formattedDate);
        commandLine.printWithNewLine(transactionLine);
        commandLine.printNewLine();
    }
    //TODO: Refactor into Reusable component - END
}
