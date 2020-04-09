package com.training.java.transaction.tracker.presentation.application.instructions.description;

public class InputInstructionDescription implements InstructionDescription {

    private String command;
    private String description;

    public InputInstructionDescription(String command) {
        this.command = command;
        this.description = "Allows users to input a transaction"; //TODO: Load from resource bundle
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getMenuDescription() {
        return String.format("%s - %s", command, description);
    }
}
