package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {homeScreen();} // run home screen

    public static void homeScreen() { // print home menu choices
        System.out.println("""
                Welcome to your Finance Account!
                Main Menu:
                [D] - Add Deposit
                [P] - Make Payment
                [L] - Ledger
                [X] - Exit""");
        String input = scanner.nextLine();
        switch (input.toUpperCase()) { // run corresponding methods (screens) based on user input
            case "D" -> addDeposit();
            case "P" -> makePayment();
            case "L" -> Ledger.ledgerMenu();
            case "X" -> System.exit(0);
            default -> System.out.println("Please enter a valid option");
        }
    }

    public static void addDeposit() { // prompt user for details of transaction and store it in corresponding variables
        System.out.println("Enter Description:");
        String description = scanner.nextLine();
        System.out.println("Enter Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("Enter Deposit Amount:");
        double amount = scanner.nextDouble();

        // write the variables' info to the csv file with appropriate format and use current date/time
        try (FileWriter fileWriter = new FileWriter("transactions.csv", true)) {
            fileWriter.write("\n" +
                    LocalDate.now() + "|" + LocalTime.now() + "|" + description + "|" + vendor + "|" + amount
            );
            fileWriter.close();
            System.out.println("Deposit added successfully!");
        } catch (IOException e) { // throw error message when input is erroneous
            System.out.println("Error inputting data!");
        }
        homeScreen(); // run the home screen menu again when finished adding deposit
    }

    public static void makePayment() { // Same as deposit method but add a "-" sign before the amount
        System.out.println("Enter Description:");
        String description = scanner.nextLine();
        System.out.println("Enter Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("Enter Transaction Amount:");
        double amount = scanner.nextDouble();

        try (FileWriter fileWriter = new FileWriter("transactions.csv", true)) {
            fileWriter.write("\n" +
                    LocalDate.now() + "|" + LocalTime.now() + "|" + description + "|" + vendor + "|-" + amount
            );
            fileWriter.close();
            System.out.println("Payment made successfully!");
        } catch (IOException e) {
            System.out.println("Error inputting data!");
        }
        homeScreen();
    }
}