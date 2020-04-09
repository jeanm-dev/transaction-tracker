package com.training.java.transaction.tracker.presentation.application.instructions.description;

public class EditInstructionDescription implements InstructionDescription {

    private String command;
    private String description;

    public EditInstructionDescription(String command) {
        this.command = command;

        description = "Edit existing transactions"; //TODO: Load description from Resources
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
