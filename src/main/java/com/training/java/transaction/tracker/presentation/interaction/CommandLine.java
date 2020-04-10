package com.training.java.transaction.tracker.presentation.interaction;

import java.math.BigDecimal;
import java.util.Date;

public interface CommandLine {

    void print(String message);
    void printWithNewLine(String message);
    void printNewLine();

    String readLine();
    BigDecimal readBigDecimal() throws InvalidInputException;
    Date readDate() throws InvalidInputException;
    int readInt();
}
