package com.training.java.transaction.tracker.presentation.application.instructions;

import java.io.PrintStream;

public class InvalidInstruction implements Instruction {
    private PrintStream printStream;

    public InvalidInstruction(PrintStream printStream) {
        this.printStream = printStream;
    }

    private String getInstructionResultMessage() {
        return "Invalid input!\nPlease try again!\n";
    }

    @Override
    public void perform() {
        printStream.println(getInstructionResultMessage());
    }

    @Override
    public String getInstructionMenuDescription() {
        return null;
    }
}
