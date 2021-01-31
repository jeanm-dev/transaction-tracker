package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.ListInstructionDescription;
import com.training.java.transaction.tracker.presentation.interaction.CommandLine;
import com.training.java.transaction.tracker.repository.RepositoryBase;
import com.training.java.transaction.tracker.repository.TransactionTableDescriptor;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListInstruction implements Instruction {

    private final CommandLine commandLine;
    private final InstructionDescription instructionDescription;
    private final RepositoryBase<Transaction, TransactionTableDescriptor> transactionRepository;

    public ListInstruction(String command, RepositoryBase<Transaction, TransactionTableDescriptor> transactionRepository, CommandLine commandLine) {
        this.transactionRepository = transactionRepository;
        this.commandLine = commandLine;

        instructionDescription = new ListInstructionDescription(command);
    }

    @Override
    public void perform() {
        try {
            List<Transaction> transactions = transactionRepository.fetchAll();

            if (transactions == null || transactions.isEmpty()) {
                printNoTransactionsFoundMessage();
            } else {
                printTransactions(transactions);
            }

        } catch (SQLException exception) {
            commandLine.printWithNewLine("Unable to fetch transactions!");
            commandLine.printWithNewLine("Check database configuration or readable!");
        }
    }

    @Override
    public String getCommand() {
        return instructionDescription.getCommand();
    }

    private void printTransactions(List<Transaction> transactions) {
        printHeading();

        for (Transaction transaction : transactions) {
            printTransactionLine(transaction);
        }

        printTransactionCount(transactions.size());
    }

    private void printHeading() {
        String headingLine = String.format("%s \t %s \t %s \t %s", "DESCRIPTION", "AMOUNT", "DATE OF TRANSACTION", "TYPE OF TRANSACTION");
        commandLine.printWithNewLine(headingLine);
    }

    private void printTransactionLine(Transaction transaction) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String formattedAmount = decimalFormat.format(transaction.getAmount());
        String formattedDate =  dateFormatter.format(transaction.getDateOfTransaction());
        Long type = transaction.getType();

        String transactionLine = String.format("%s \t %s \t %s \t %s", transaction.getDescription(), formattedAmount, formattedDate, type == null ? "Undefined" : type);
        commandLine.printWithNewLine(transactionLine);
    }

    private void printTransactionCount(int count) {
        commandLine.printWithNewLine(String.format("\nTotal number of transactions: %d", count));
    }

    private void printNoTransactionsFoundMessage() {
        commandLine.printWithNewLine("No transactions found!");
    }


    @Override
    public String getInstructionMenuDescription() {
        return instructionDescription.getMenuDescription();
    }
}
