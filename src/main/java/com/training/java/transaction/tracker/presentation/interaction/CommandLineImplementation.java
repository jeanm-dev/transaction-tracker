package com.training.java.transaction.tracker.presentation.interaction;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CommandLineImplementation implements CommandLine {

    private final Scanner scanner;
    private final PrintStream printStream;

    public CommandLineImplementation(PrintStream printStream, Scanner scanner) {

        this.printStream = printStream;
        this.scanner = scanner;
    }

    @Override
    public void print(String message) {
        printStream.print(message);
    }

    @Override
    public void printWithNewLine(String message) {
        print(message);
        printNewLine();
    }

    @Override
    public void printNewLine() {
        printStream.println();
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public BigDecimal readBigDecimal() throws InvalidInputException {
        try {
            BigDecimal value = scanner.nextBigDecimal();
            clearsCurrentInput(); // Clears the New line - \n

            return value;
        } catch (Exception exception) {
            clearsCurrentInput();
            String exceptionDescription = "Invalid amount entered!" +
                    "\nPlease enter a value with a valid format! e.g. 0.00";
            throw new InvalidInputException(exceptionDescription);
        }
    }

    private void clearsCurrentInput() {
        scanner.nextLine(); //Clears invalid input
    }

    @Override
    public Date readDate() throws InvalidInputException {
        Format dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String input = scanner.nextLine();

        try {
            return (Date) dateFormatter.parseObject(input);
        } catch (ParseException exception) {
            String exceptionDescription = "Invalid date format!" +
                    "Please a date value that matches (YYYY-MM-DD)";
//            exception.printStackTrace();
            throw new InvalidInputException(exceptionDescription);
        }
    }

    @Override
    public int readInt() {
        int returnValue = scanner.nextInt();
        scanner.nextLine(); //Clears input

        return returnValue;
    }
}
