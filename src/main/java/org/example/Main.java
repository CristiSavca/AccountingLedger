package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) { // run home screen
        System.out.println("Welcome To Your Finance Account!");
        homeScreen();
    }

    public static void homeScreen() { // print home menu choices
        boolean done = false;
        while(!done) {
            System.out.println("""
                    Main Menu:
                    [D] - Add Deposit
                    [P] - Make Payment
                    [L] - Ledger
                    [X] - Exit""");
            String input = scanner.nextLine();
            switch (input.toUpperCase()) { // run corresponding methods (screens) based on user input
                case "D" -> addEntry("Deposit");
                case "P" -> addEntry("Payment");
                case "L" -> Ledger.ledgerMenu();
                case "X" -> {System.out.println("Logging Off..."); done = true;}
                default -> System.out.println("Please enter a valid option");
            }
        }
    }

    public static void addEntry(String filter) { // prompt user for details of transaction and store it in corresponding variables
            System.out.println("Enter Description:");
            String description = scanner.nextLine();
            System.out.println("Enter Vendor:");
            String vendor = scanner.nextLine();
            System.out.println("Enter Deposit Amount:");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // clear scanner buffer

            String amountSign = "";
            if (filter.equals("Payment")) {
                amountSign = "-";
            }

            // write the variables' info to the csv file with appropriate format and use current date/time
            try (FileWriter fileWriter = new FileWriter("transactions.csv", true)) {
                fileWriter.write("\n" +
                        LocalDate.now() + "|" + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "|" + description + "|" + vendor + "|" + amountSign + amount);
                fileWriter.close();
                System.out.println(filter + " added successfully!");
            } catch (IOException e) { // throw error message when input is erroneous
                System.out.println("Error inputting data!");
            }
        }
    }