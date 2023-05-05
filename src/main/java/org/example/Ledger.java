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
    public static Scanner scanner = new Scanner(System.in); // Initialize a global scanner to avoid redundant Scanner declarations
    public static ArrayList<Transaction> transactions = getTransactions(); // Initialize global array and inherit transactions from getTransactions method

    public static ArrayList<Transaction> getTransactions() { // This method reads transactions from a CSV file and stores them in an ArrayList of Transaction objects
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);

            String input;
            while ((input = bufReader.readLine()) != null) { // For every line in the csv file until no more lines:
                String[] details = input.split("\\|"); // split the line into pieces to extract and store info from each piece
                LocalDate date = LocalDate.parse(details[0]);
                LocalTime time = LocalTime.parse(details[1]);
                String description = details[2];
                String vendor = details[3];
                double amount = Double.parseDouble(details[4]);

                // Create a transaction object using the pieces we extracted and populate our ArrayList of transactions
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

    public static void ledgerMenu() { // This method displays the ledger menu options for user to choose from and calls other methods based on selection
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
                case "A" -> showEntries("Entries"); // Display all transactions
                case "D" -> showEntries("Deposits"); // Display only deposits
                case "P" -> showEntries("Payments"); // Display only payments
                case "R" -> reportsMenu(); // Go to the Reports menu
                case "H" -> done = true; // Exit to Main menu
                default -> System.out.println("Please enter a valid option");
            }
        }
    }

    public static void showEntries(String filter) { // This method loops through transactions ArrayList and displays transactions based on filter from above menu
        boolean filterType = true; // Show all transactions by default
        System.out.println("All " + filter + ":");
        for (Transaction item : transactions) {
            if (filter.equals("Deposits")) {
                filterType = item.getAmount() > 0;
            } else if (filter.equals("Payments")) {
                filterType = item.getAmount() < 0;
            }

            if (filterType) { // print out corresponding transactions based on filterType
                printTransaction(item);
            }
        }
    }

    public static void reportsMenu() { // This method displays menu of queries for different reports
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
            String vendor = ""; // vendor empty by default unless option [5] is used
            String input = scanner.nextLine();
            switch (input.toUpperCase()) { // Call reportsLoop method with parameters based on user's Report choice
                case "1" -> reportsLoops(date, "Month To Date", vendor);
                case "2" -> reportsLoops(date.minusMonths(1), "Previous Month", vendor); // update month to previous month
                case "3" -> reportsLoops(date, "Year To Date", vendor);
                case "4" -> reportsLoops(date.minusYears(1), "Previous Year", vendor); // update year to previous year
                case "5" -> {
                    System.out.println("Enter Vendor Name:");
                    vendor = scanner.nextLine();
                    reportsLoops(date,"", vendor); // update vendor variable before calling reportsLoop
                }
                case "6" -> customSearch();
                case "0" -> done = true;
                default -> System.out.println("Please enter a valid option");
            }
        }
    }

    public static void reportsLoops(LocalDate filterDate, String filter, String vendor) { // one loop for all 6 menu options
        int[] itemDate = new int[2]; // initialized to 0 by default
        int[] targetDate = new int[2]; // 0 by default
        String itemVendor = ""; // blank by default
        System.out.println(filter + vendor.toUpperCase() + " Transactions:");

        for (Transaction item : transactions) {
            if (filter.equals("Month To Date") || filter.equals("Previous Month")) {
                itemDate[0] = item.getDate().getMonthValue(); itemDate[1] = item.getDate().getYear(); // set current item's values-to-be-compared variable to current month and year
                targetDate[0] = filterDate.getMonthValue(); targetDate[1] = filterDate.getYear();} // set the target date's values-to-be-compared variable to current month and year
            else if (filter.equals("Year To Date") || filter.equals("Previous Year")) {
                itemDate[1] = item.getDate().getYear(); targetDate[1] = filterDate.getYear();} // set only the year to be compared, and leave month value as 0. Same for target date
            else {itemVendor = item.getVendor().toLowerCase();} // else leave months and years as 0 for both item and target dates, so they can match, and only compare by vendor

            if (itemDate[0] == targetDate[0] && itemDate[1] == targetDate[1] && itemVendor.equals(vendor.toLowerCase())) {
                printTransaction(item);
            }
        }
    }

    public static void customSearch() { // This method allows the user to perform a custom search based on various fields
        System.out.println("Custom Search: (Press Enter To Skip Field)");
        System.out.println("Enter Start Date:");
        LocalDate startDate = getDateFromCLI(); // if no date is inputted, set field as null
        System.out.println("Enter End Date:");
        LocalDate endDate = getDateFromCLI(); // same here
        System.out.println("Enter Description:");
        String description = scanner.nextLine().toLowerCase();
        System.out.println("Enter Vendor:");
        String vendor = scanner.nextLine().toLowerCase();
        System.out.println("Enter Amount:");
        String amountValid = scanner.nextLine();
        double amount = amountValid.isEmpty() ? 0 : Double.parseDouble(amountValid); // if no amount inputted, set to 0, else set to amount entered

        for (Transaction item : transactions) { // Check if the Transaction object matches the user's search criteria
            boolean matchStartDate = startDate == null || !item.getDate().isBefore(startDate); // if field is null, set corresponding boolean variable to true
            boolean matchEndDate = endDate == null || !item.getDate().isAfter(endDate); // if null/empty condition is true, field gets ignored.
            boolean matchDescription = description.isEmpty() || item.getDescription().equalsIgnoreCase(description); // if field not null, it gets compared to current item
            boolean matchVendor = vendor.isEmpty() || item.getVendor().equalsIgnoreCase(vendor);
            boolean matchAmount = amount == 0 || item.getAmount() == amount;

            if (matchStartDate && matchEndDate && matchDescription && matchVendor && matchAmount) {
                printTransaction(item);
            }
        }
    }

    private static void printTransaction(Transaction item) { // print method to avoid redundant print calls
        System.out.println(
                        item.getDate() + " " +
                        item.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " +
                        item.getDescription() + " " +
                        item.getVendor() + " $" +
                        item.getAmount()
        );
    }

    private static LocalDate getDateFromCLI() { // if no date inputted set LocalDate variable to null, else: store date
        String input = scanner.nextLine();
        return input.equals("") ? null : LocalDate.parse(input);
    }
}