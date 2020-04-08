package com.training.java.transaction.tracker.presentation.application.instructions;

import com.training.java.transaction.tracker.presentation.application.instructions.description.ExitInstructionDescription;
import com.training.java.transaction.tracker.presentation.application.instructions.description.InstructionDescription;

public class ExitInstruction implements Instruction {

    private InstructionDescription instructionDescription;

    public ExitInstruction() {
        instructionDescription = new ExitInstructionDescription("E");
    }

    @Override
    public String getInstructionMenuMessage() {
        return instructionDescription.getInstructionDescription();
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
