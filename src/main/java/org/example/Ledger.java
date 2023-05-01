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
            case "A":
                showEntries();
                break;
            case "D":
                showDeposits();
                break;
            case "P":
                showPayments();
                break;
            case "R":
                reportsMenu();
            case "H":
                Main.homescreen();
            default:
                System.out.println("Please enter a valid option");
                break;
        }
    }

    public static void showEntries() {

    }

    public static void showDeposits() {

    }

    public static void showPayments() {

    }

    public static void reportsMenu() {

    }
}
