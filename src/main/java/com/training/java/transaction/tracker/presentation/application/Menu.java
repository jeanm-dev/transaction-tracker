package com.training.java.transaction.tracker.presentation.application;

import com.training.java.transaction.tracker.presentation.application.instructions.ExitInstruction;
import com.training.java.transaction.tracker.presentation.application.instructions.Instruction;
import com.training.java.transaction.tracker.presentation.application.instructions.InvalidInstruction;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;
    private PrintStream printStream;

    private Map<String, Instruction> instructions;

    private static final String NEW_LINE = "\n";

    public Menu(Scanner scanner, PrintStream printStream) {
        this.scanner = scanner;
        this.printStream = printStream;

        // TODO: Build this dynamically
        this.instructions = Map.of(
                "E", new ExitInstruction()
        );
    }

    private String createInstructionMessage(Map<String, Instruction> instructions) {
        StringBuilder builder = new StringBuilder();

        builder.append(NEW_LINE);
        builder.append("Enter one of the following commands to continue:");
        builder.append(NEW_LINE);

        for (Instruction instruction: instructions.values()) {
            builder.append(NEW_LINE);
            builder.append(instruction.getInstructionMenuMessage());
            builder.append(NEW_LINE);
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
            builder.append(inputCommand);
            builder.append(",");
        }

        //Remove last comma
        builder.deleteCharAt(builder.length()-1);

        return String.format("(%s): ", builder.toString());
    }

    private Instruction determineNextOperation(String input) {
        Instruction result = null;

        for (String operationKey : instructions.keySet()) {
            if (operationKey.equalsIgnoreCase(input)) {
                result = instructions.get(operationKey);
                break;
            }
        }

        return result;
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
        String input = retrieveUserInput();

        // Match input with instruction
        Instruction nextInstruction = determineNextOperation(input);

        // Check valid next instruction
        nextInstruction = nextInstruction == null ? new InvalidInstruction(printStream) : nextInstruction;

        nextInstruction.perform();
    }
}
