package org.example;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Ledger {
    public static ArrayList<Transaction> transactions = getTransactions();
    public static ArrayList<Transaction> getTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        // this method loads transaction objects into transactions array
        try{
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);

            String input;
            while ((input = bufReader.readLine()) != null) {
                String[] details = input.split("\\|");
                LocalDate date = LocalDate.parse(details[0]);
                LocalTime time = LocalTime.parse(details[1]);
                String description = details[2];
                String vendor = details[3];
                double amount = Double.parseDouble(details[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("File not found!");
            System.exit(0);
        }
        return transactions;
    }


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
        System.out.println("All Entries:");
        for (Transaction item : transactions){
            System.out.println(
                    item.getDate() + " " +
                    item.getTime() + " " +
                    item.getDescription() + " " +
                    item.getVendor() + " " +
                    item.getAmount()
            );
        }
    }

    public static void showDeposits() {

    }

    public static void showPayments() {

    }

    public static void reportsMenu() {

    }
}
