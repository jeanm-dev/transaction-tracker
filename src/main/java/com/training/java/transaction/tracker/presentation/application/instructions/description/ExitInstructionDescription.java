package com.training.java.transaction.tracker.presentation.application.instructions.description;

public class ExitInstructionDescription implements InstructionDescription {

    private String command;
    private String description;

    public ExitInstructionDescription(String command) {
        this.command = command;
        this.description = "Exits the application"; //TODO: Load from resource bundle
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
