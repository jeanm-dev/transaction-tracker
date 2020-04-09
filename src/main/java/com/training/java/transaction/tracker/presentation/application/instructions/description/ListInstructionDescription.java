package com.training.java.transaction.tracker.presentation.application.instructions.description;

public class ListInstructionDescription implements InstructionDescription {

    private String command;
    private String description;

    public ListInstructionDescription(String command) {
        this.command = command;
        this.description = "Displays a list of all the stored transactions";
    }

    @Override
    public String getMenuDescription() {
        return String.format("%s - %s", command, description);
    }
}
