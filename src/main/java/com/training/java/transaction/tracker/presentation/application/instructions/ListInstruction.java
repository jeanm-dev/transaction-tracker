package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.ListInstructionDescription;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class ListInstruction implements Instruction {

    private PrintStream printStream;
    private InstructionDescription instructionDescription;
    private TransactionRepository transactionRepository;

    public ListInstruction(TransactionRepository transactionRepository, PrintStream printStream) {
        this.transactionRepository = transactionRepository;
        this.printStream = printStream;

        instructionDescription = new ListInstructionDescription("L");
    }

    @Override
    public void perform() {
        try {
            List<Transaction> transactions = transactionRepository.fetchAllTransactions();

            if (transactions == null || transactions.isEmpty()) {
                printNoTransactionsFoundMessage();
            } else {
                printTransactions(transactions);
            }

        } catch (SQLException exception) {
            printStream.println("Unable to fetch transactions!");
            printStream.println("Check database configuration or readable!");
        }
    }

    private void printTransactions(List<Transaction> transactions) {
        printHeading();

        for (Transaction transaction : transactions) {
            printTransactionLine(transaction);
        }

        printTransactionCount(transactions.size());
    }

    private void printHeading() {
        String headingLine = String.format("%s \t %s \t %s", "DESCRIPTION", "AMOUNT", "DATE OF TRANSACTION");
        printStream.println(headingLine);
    }

    private void printTransactionLine(Transaction transaction) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String transactionLine = String.format("%s \t %s \t %s", transaction.getDescription(), decimalFormat.format(transaction.getAmount()), transaction.getDateOfTransaction());
        printStream.println(transactionLine);
    }

    private void printTransactionCount(int count) {
        printStream.println(String.format("\nTotal number of transactions: %d", count));
    }

    private void printNoTransactionsFoundMessage() {
        printStream.println("No transactions found!");
    }


    @Override
    public String getInstructionMenuMessage() {
        return instructionDescription.getInstructionDescription();
    }
}
