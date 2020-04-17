package com.training.java.transaction.tracker.presentation.application;

import com.training.java.transaction.tracker.presentation.application.instructions.*;
import com.training.java.transaction.tracker.presentation.interaction.CommandLine;
import com.training.java.transaction.tracker.repository.TransactionRepository;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    private final CommandLine commandLine;
    private Scanner scanner;
    private PrintStream printStream;
    private TransactionRepository transactionRepository;
    private Map<String, Instruction> instructions;

    private static final String NEW_LINE = "\n";

    public Menu(Scanner scanner, PrintStream printStream, TransactionRepository transactionRepository, CommandLine commandLine) {
        this.scanner = scanner;
        this.printStream = printStream;
        this.commandLine = commandLine;
        this.transactionRepository = transactionRepository;

        instructions = new HashMap<>();
        registerInstructions();
    }

    private void registerInstructions() {
        registerInstruction(new ExitInstruction("X"));
        registerInstruction(new ListInstruction("L", transactionRepository, commandLine));
        registerInstruction(new InputInstruction("I", transactionRepository, commandLine));
        registerInstruction(new EditIntruction("E", transactionRepository, commandLine));
        registerInstruction(new DeleteInstruction("D", transactionRepository, commandLine));
    }

    private void registerInstruction(Instruction instruction) {
        instructions.put(instruction.getCommand(), instruction);
    }

    private String createInstructionMessage(Map<String, Instruction> instructions) {
        StringBuilder builder = new StringBuilder();

        builder.append(NEW_LINE)
                .append("Enter one of the following commands to proceed:")
                .append(NEW_LINE);

        for (Instruction instruction : instructions.values()) {
            String message = instruction.getInstructionMenuDescription();
            if (message != null) {
                builder.append(NEW_LINE)
                        .append(" # ")
                        .append(message);
            }
        }

        return builder.toString();
    }

    private void displayMenu() {
        // Output menu
        printStream.println(createInstructionMessage(instructions));

    }

    private String retrieveUserInput() {
        printStream.println(createInputMessage());
        return scanner.nextLine();
    }

    private String createInputMessage() {

        StringBuilder builder = new StringBuilder();
        for (String inputCommand : instructions.keySet()) {
            if (instructions.get(inputCommand).getInstructionMenuDescription() != null) {
                builder.append(inputCommand)
                        .append(",");
            }
        }

        //Remove last comma
        builder.deleteCharAt(builder.length() - 1);

        return String.format("\n(%s): ", builder.toString());
    }

    //TODO: Do java-doc
    private Instruction determineNextOperation(String input) throws Exception {
        if (instructions.containsKey(input)) {
            return instructions.get(input);
        } else {
            throw new Exception("Invalid input!\nPlease try again!\n");
        }
    }


    public void start() {
        while (true) {
            menuLoop();
        }
    }

    private void menuLoop() {
        // Display menu options
        displayMenu();

        // Fetch input from input stream
        String input = retrieveUserInput().toUpperCase();

        // Match input with instruction
        try {
            determineNextOperation(input).perform();
        } catch (Exception e) {
            commandLine.printWithNewLine(e.getMessage());
        }

    }
}
