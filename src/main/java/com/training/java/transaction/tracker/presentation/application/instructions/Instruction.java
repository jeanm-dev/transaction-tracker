package com.training.java.transaction.tracker.presentation.application.instructions;


public interface Instruction {

    void perform();

    String getCommand();
    String getInstructionMenuDescription();
}
