package com.training.java.transaction.tracker.presentation.application.instructions.description;

public class DeleteInstructionDescription implements InstructionDescription {

    private final String command;
    private final String description;

    public DeleteInstructionDescription(String command) {
        this.command = command;

        description = "Delete a transaction"; // TODO: Load from Resource bundle
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
