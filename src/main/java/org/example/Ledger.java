package org.example;

import java.util.Scanner;

public class Ledger {
    public static void showLedger() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Welcome to your Account Ledger!
                Main Menu:
                [A] - All Entries
                [D] - Deposits
                [P] - Payments
                [R] - Reports
                [H] - Home
                """);
        String input = scanner.nextLine();
        switch(input.toUpperCase()){
            case "D":
                showEntries();
                break;
            case "P":
                showDeposits();
                break;
            case "L":
                showLedger();
                break;
            case "X":
                System.exit(0);
            default:
                System.out.println("Please enter a valid option");
                break;
        }
    }
}
