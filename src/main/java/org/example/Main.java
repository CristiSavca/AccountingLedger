package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        homescreen();
    }

    public static void homescreen() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Welcome to your Account Ledger!
                Main Menu:
                [D] - Add Deposit
                [P] - Make Payment
                [L] - Ledger
                [X] - Exit""");
        String input = scanner.nextLine();
        switch(input.toUpperCase()){
            case "D":
                addDeposit();
                break;
            case "P":
                makePayment();
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

    public static void addDeposit() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Date: YYYY-MM-DD");
        String date = scanner.nextLine();
        System.out.println("Enter Time: HH:MM:SS");
        String time = scanner.nextLine();
        System.out.println("Enter Description:");
        String description = scanner.nextLine();
        System.out.println("Enter Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("Enter Deposit Amount:");
        double amount = scanner.nextDouble();

        try (FileWriter fileWriter = new FileWriter("transactions.csv", true)){
            fileWriter.write("\n" +
                    date + "|" +
                    time + "|" +
                    description + "|" +
                    vendor + "|" +
                    amount
                    );
            fileWriter.close();
            System.out.println("Deposit added successfully!");
        }
        catch(IOException e){
            System.out.println("Error inputting data!");
        }
        homescreen();
    }


    public static void makePayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Date: YYYY-MM-DD");
        String date = scanner.nextLine();
        System.out.println("Enter Time: HH:MM:SS");
        String time = scanner.nextLine();
        System.out.println("Enter Description:");
        String description = scanner.nextLine();
        System.out.println("Enter Vendor:");
        String vendor = scanner.nextLine();
        System.out.println("Enter Transaction Amount:");
        double amount = scanner.nextDouble();

        try (FileWriter fileWriter = new FileWriter("transactions.csv", true)){
            fileWriter.write("\n" +
                    date + "|" +
                    time + "|" +
                    description + "|" +
                    vendor + "|" + "-" +
                    amount
            );
            fileWriter.close();
            System.out.println("Payment made successfully!");
        }
        catch(IOException e){
            System.out.println("Error inputting data!");
        }
        homescreen();
    }

    public static void showLedger() {
        Ledger.showLedger();
    }
}