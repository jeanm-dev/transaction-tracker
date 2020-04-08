package com.training.java.transaction.tracker.presentation.application;

import java.util.Scanner;

public class ConsoleApplication {

    private static final String STAR_DIVIDER = "**************";
    private static final String NEW_LINE = "\n";

    public static void main(String[] args) {
        printWelcomeMessage();

        Scanner scanner = new Scanner(System.in);

        Menu coordinator = new Menu(scanner, System.out);
        coordinator.start();
    }

    private static void printWelcomeMessage() {
        String welcomeMessage = String.format("%s\n%s\n%s", STAR_DIVIDER, "Welcome", STAR_DIVIDER);
        println(welcomeMessage);
    }


    private static void println(String string) {
        System.out.println(string);
    }
}
