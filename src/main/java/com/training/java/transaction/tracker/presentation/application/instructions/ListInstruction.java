package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.domainobject.Transaction;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.ListInstructionDescription;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListInstruction implements Instruction {

    private PrintStream printStream;
    private InstructionDescription instructionDescription;
    private TransactionRepository transactionRepository;

    public ListInstruction(String command, TransactionRepository transactionRepository, PrintStream printStream) {
        this.transactionRepository = transactionRepository;
        this.printStream = printStream;

        instructionDescription = new ListInstructionDescription(command);
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
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String formattedAmount = decimalFormat.format(transaction.getAmount());
        String formattedDate =  dateFormatter.format(transaction.getDateOfTransaction());

        String transactionLine = String.format("%s \t %s \t %s", transaction.getDescription(), formattedAmount, formattedDate);
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
