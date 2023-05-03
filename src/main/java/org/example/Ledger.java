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
    public static ArrayList<Transaction> transactions = getTransactions(); // initialize global array and inherit info from getTransactions method

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
                Ledger Menu:
                [A] - All Entries
                [D] - Deposits
                [P] - Payments
                [R] - Reports
                [H] - Home""");
        String filter = "";
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "A" -> showEntries("Entries");
            case "D" -> showEntries("Deposits");
            case "P" -> showEntries("Payments");
            case "R" -> reportsMenu();
            case "H" -> Main.homeScreen();
            default -> System.out.println("Please enter a valid option");
        }
    }

    public static void showEntries(String filter) { // loop through transactions array and display all entries based on filter
        boolean filterStatus = true;
        System.out.println("All " + filter + ":");
        for (Transaction item : transactions) {
            if (filter.equals("Deposits")){filterStatus = item.getAmount() > 0;}
            else if (filter.equals("Payments")){filterStatus = item.getAmount() < 0;}

            if (filterStatus) {
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
        LocalDate date = LocalDate.now(); String filter = ""; String vendor = "";
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "1" -> {date = LocalDate.now(); filter = "Month To Date";}
            case "2" -> {date = LocalDate.now().minusMonths(1); filter = "Previous Month";}
            case "3" -> {date = LocalDate.now(); filter = "Year To Date";}
            case "4" -> {date = LocalDate.now().minusYears(1); filter = "Previous Year";}
            case "5" -> {System.out.println("Enter Vendor Name:"); vendor = scanner.nextLine();}
            case "0" -> ledgerMenu();
            default -> {System.out.println("Please enter a valid option"); reportsMenu();}
        }
        reportsLoops(date, filter, vendor);
    }

    public static void reportsLoops(LocalDate filterDate, String filter, String vendor) {
        int[] itemDate = new int[2];
        int[] targetDate = new int[2];
        String itemVendor = "";
        System.out.println(filter + " Transactions:");

        for (Transaction item : transactions) {
            if (filter.equals("Month To Date") || filter.equals("Previous Month")){
                itemDate[0] = item.getDate().getMonthValue(); itemDate[1] = item.getDate().getYear();
                targetDate[0] = filterDate.getMonthValue(); targetDate[1] = filterDate.getYear();}
            else if (filter.equals("Year To Date") || filter.equals("Previous Year")){
                itemDate[1] = item.getDate().getYear(); targetDate[1] = filterDate.getYear();}
            else {itemVendor = item.getVendor();}

            if (itemDate[0] == targetDate[0] && itemDate[1] == targetDate[1] && itemVendor.equals(vendor)) {
                System.out.println(
                        item.getDate() + " " +
                                item.getTime() + " " +
                                item.getDescription() + " " +
                                item.getVendor() + " $" +
                                item.getAmount()
                );
            }
        }
        System.out.println("[X] - Back");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("X")) {reportsMenu();}
        else {reportsMenu();}
    }
}