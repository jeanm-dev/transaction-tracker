package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.presentation.application.instructions.description.ExitInstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;

public class ExitInstruction implements Instruction {

    private InstructionDescription instructionDescription;

    public ExitInstruction(String command) {
        instructionDescription = new ExitInstructionDescription(command);
    }

    @Override
    public String getInstructionMenuDescription() {
        return instructionDescription.getMenuDescription();
    }

    private String getInstructionResultMessage() {
        return "Exiting...";
    }

    @Override
    public void perform() {
        System.out.println(getInstructionResultMessage());
        System.exit(0);
    }
}
