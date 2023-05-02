package org.example;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Ledger {
    public static Scanner scanner = new Scanner(System.in); // initialize a global scanner to avoid redundant Scanner declarations
    public static ArrayList<Transaction> transactions = getTransactions(); // initialize global array and inherit from getTransactions method

    public static ArrayList<Transaction> getTransactions() { // parse csv file, store transactions into objects, and add to arraylist
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
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
        // Sort our transactions ArrayList in ascending order before returning it
        Comparator<Transaction> compareByDate = Comparator.comparing(Transaction::getDate).reversed();
        Comparator<Transaction> compareByTime = Comparator.comparing(Transaction::getTime).reversed();
        transactions.sort(compareByDate.thenComparing(compareByTime));

        return transactions;
    }

    public static void ledgerMenu() {
        System.out.println("""
                Welcome to your Account Ledger!
                Main Menu:
                [A] - All Entries
                [D] - Deposits
                [P] - Payments
                [R] - Reports
                [H] - Home""");
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "A" -> showEntries();
            case "D" -> showDeposits();
            case "P" -> showPayments();
            case "R" -> reportsMenu();
            case "H" -> Main.homeScreen();
            default -> System.out.println("Please enter a valid option");
        }
    }

    public static void showEntries() {
        System.out.println("All Entries:");
        for (Transaction item : transactions) {
            System.out.println(
                    item.getDate() + " " +
                            item.getTime() + " " +
                            item.getDescription() + " " +
                            item.getVendor() + " $" +
                            item.getAmount()
            );
        }
        System.out.println("[X] - Exit");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("X")) {
            ledgerMenu();
        } else {
            ledgerMenu();
        }
    }

    public static void showDeposits() {
        System.out.println("All Deposits:");
        for (Transaction item : transactions) {
            if (item.getAmount() > 0) {
                System.out.println(
                        item.getDate() + " " +
                                item.getTime() + " " +
                                item.getDescription() + " " +
                                item.getVendor() + " $" +
                                item.getAmount()
                );
            }
        }
        System.out.println("[X] - Exit");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("X")) {ledgerMenu();}
        else {ledgerMenu();}
    }

    public static void showPayments() {
        System.out.println("All Payments:");
        for (Transaction item : transactions) {
            if (item.getAmount() < 0) {
                System.out.println(
                        item.getDate() + " " +
                                item.getTime() + " " +
                                item.getDescription() + " " +
                                item.getVendor() + " $" +
                                item.getAmount()
                );
            }
        }
        System.out.println("[X] - Exit");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("X")) {ledgerMenu();}
        else {ledgerMenu();}
    }

    public static void reportsMenu() {
        System.out.println("""
                Reports Menu:
                [1] - Month To Date
                [2] - Previous Month
                [3] - Year To Date
                [4] - Previous Year
                [5] - Search By Vendor
                [0] - Back""");
        LocalDate date = LocalDate.now();
        String filter = "";
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "1" -> {date = LocalDate.now().withDayOfMonth(1);
                         filter = "Month To Date";}
            case "2" -> {date = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                         filter = "Previous Month";}
            case "3" -> {date = LocalDate.now().withDayOfYear(1);
                         filter = "Year To Date";}
            case "4" -> {date = LocalDate.now().minusYears(1).withDayOfYear(1);
                         filter = "Previous Year";}
            case "5" -> {System.out.println("Enter Vendor Name:");
                         String vendor = scanner.nextLine();
                         searchByVendor(vendor);}
            case "0" -> ledgerMenu();
            default -> System.out.println("Please enter a valid option");
        }
        reportsLoops(date, filter);
    }

    public static void reportsLoops(LocalDate filterDate, String filterType) {
        int itemDateVal = 0;
        int searchBy = 0;
        System.out.println(filterType + " Transactions");

        for (Transaction item : transactions) {
            if (filterType.equals("Month To Date") || filterType.equals("Previous Month")){
                itemDateVal = item.getDate().getMonthValue();
                searchBy = filterDate.getMonthValue();
            } else {itemDateVal = item.getDate().getYear();
                    searchBy = filterDate.getYear();}

            if (itemDateVal == searchBy) {
                System.out.println(
                        item.getDate() + " " +
                                item.getTime() + " " +
                                item.getDescription() + " " +
                                item.getVendor() + " $" +
                                item.getAmount()
                );
            }
        }
        System.out.println("[X] - Exit");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("X")) {ledgerMenu();}
        else {ledgerMenu();}
    }

    public static void searchByVendor(String vendor) {
        System.out.println("All " + vendor + " Transactions:");
        for (Transaction item : transactions) {
            if (item.getVendor().equals(vendor)) {
                System.out.println(
                        item.getDate() + " " +
                                item.getTime() + " " +
                                item.getDescription() + " " +
                                item.getVendor() + " $" +
                                item.getAmount()
                );
            }
        }
        System.out.println("[X] - Exit");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("X")) {ledgerMenu();}
        else {ledgerMenu();}
    }
}