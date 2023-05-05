package org.example;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            System.out.println("File Not Found!");
            System.exit(0);
        }
        // Sort our transactions ArrayList in ascending order before returning it
        Comparator<Transaction> compareByDate = Comparator.comparing(Transaction::getDate).reversed();
        Comparator<Transaction> compareByTime = Comparator.comparing(Transaction::getTime).reversed();
        transactions.sort(compareByDate.thenComparing(compareByTime));

        return transactions;
    }

    public static void ledgerMenu() {
        boolean done = false;
        while (!done) {
            System.out.println("""
                    Ledger Menu:
                    [A] - All Entries
                    [D] - Deposits
                    [P] - Payments
                    [R] - Reports
                    [H] - Home""");
            String input = scanner.nextLine();
            switch (input.toUpperCase()) {
                case "A" -> showEntries("Entries");
                case "D" -> showEntries("Deposits");
                case "P" -> showEntries("Payments");
                case "R" -> reportsMenu();
                case "H" -> done = true;
                default -> System.out.println("Please enter a valid option");
            }
        }
    }

    public static void showEntries(String filter) { // loop through transactions array and display all entries based on filter
        boolean filterStatus = true;
        System.out.println("All " + filter + ":");
        for (Transaction item : transactions) {
            if (filter.equals("Deposits")) {
                filterStatus = item.getAmount() > 0;
            } else if (filter.equals("Payments")) {
                filterStatus = item.getAmount() < 0;
            }

            if (filterStatus) {
                printTransaction(item);
            }
        }
    }

    public static void reportsMenu() {
        boolean done = false;
        while (!done) {
            System.out.println("""
                    Reports Menu:
                    [1] - Month To Date
                    [2] - Previous Month
                    [3] - Year To Date
                    [4] - Previous Year
                    [5] - Search By Vendor
                    [6] - Custom Search
                    [0] - Back""");
            LocalDate date = LocalDate.now();
            String vendor = "";
            String input = scanner.nextLine();
            switch (input.toUpperCase()) {
                case "1" -> reportsLoops(date, "Month To Date", vendor);
                case "2" -> reportsLoops(date.minusMonths(1), "Previous Month", vendor);
                case "3" -> reportsLoops(date, "Year To Date", vendor);
                case "4" -> reportsLoops(date.minusYears(1), "Previous Year", vendor);
                case "5" -> {System.out.println("Enter Vendor Name:"); vendor = scanner.nextLine(); reportsLoops(date,"", vendor);}
                case "6" -> customSearch();
                case "0" -> done = true;
                default -> System.out.println("Please enter a valid option");
            }
        }
    }

    public static void reportsLoops(LocalDate filterDate, String filter, String vendor) {
        int[] itemDate = new int[2];
        int[] targetDate = new int[2];
        String itemVendor = "";
        System.out.println(filter + vendor.toUpperCase() + " Transactions:");

        for (Transaction item : transactions) {
            if (filter.equals("Month To Date") || filter.equals("Previous Month")) {
                itemDate[0] = item.getDate().getMonthValue(); itemDate[1] = item.getDate().getYear();
                targetDate[0] = filterDate.getMonthValue(); targetDate[1] = filterDate.getYear();}
            else if (filter.equals("Year To Date") || filter.equals("Previous Year")) {
                itemDate[1] = item.getDate().getYear(); targetDate[1] = filterDate.getYear();}
            else {itemVendor = item.getVendor().toLowerCase();}

            if (itemDate[0] == targetDate[0] && itemDate[1] == targetDate[1] && itemVendor.equals(vendor.toLowerCase())) {
                printTransaction(item);
            }
        }
    }

    public static void customSearch() {
        System.out.println("Custom Search: (Press Enter To Skip Field)");
        System.out.println("Enter Start Date:");
        LocalDate startDate = getDateFromCLI();
        System.out.println("Enter End Date:");
        LocalDate endDate = getDateFromCLI();
        System.out.println("Enter Description:");
        String description = scanner.nextLine().toLowerCase();
        System.out.println("Enter Vendor:");
        String vendor = scanner.nextLine().toLowerCase();
        System.out.println("Enter Amount:");
        String amountValid = scanner.nextLine();
        double amount = amountValid.isEmpty() ? 0 : Double.parseDouble(amountValid);

        for (Transaction item : transactions) {
            boolean matchStartDate = startDate == null || !item.getDate().isBefore(startDate);
            boolean matchEndDate = endDate == null || !item.getDate().isAfter(endDate);
            boolean matchDescription = description.isEmpty() || item.getDescription().equalsIgnoreCase(description);
            boolean matchVendor = vendor.isEmpty() || item.getVendor().equalsIgnoreCase(vendor);
            boolean matchAmount = amount == 0 || item.getAmount() == amount;

            if (matchStartDate && matchEndDate && matchDescription && matchVendor && matchAmount) {
                printTransaction(item);
            }
        }
    }

    private static void printTransaction(Transaction item) {
        System.out.println(
                        item.getDate() + " " +
                        item.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " +
                        item.getDescription() + " " +
                        item.getVendor() + " $" +
                        item.getAmount()
        );
    }

    private static LocalDate getDateFromCLI() {
        String input = scanner.nextLine();
        return input.equals("") ? null : LocalDate.parse(input);
    }
}