# Accounting Ledger Application

This is a Java CLI application built for Capstone Project 1.
With this application, you can track all financial transactions for a business or personal use.


## The application includes several screens:
1) The Home Screen which prompts the user with options to Add Deposit, Make Payments, display the Ledger screen, or Exit the application.

2) The Ledger screen displays all entries with the newest entries first and allows the user to filter entries by All, Deposits, Payments, or Reports.

3) The Reports screen offers pre-defined reports, including Month to Date, Previous Month, Year to Date, Previous Year, and Search by Vendor. If the user enters a value for a search field, the application filters on that field.

4) The Custom Search feature, a challenge option inside the Reports screen where the user is prompted to enter search values for Start Date, End Date, Description, Vendor, and Amount. The application filters on the search fields entered by the user.


# Screenshots of some Screens:

<img width="211" alt="image" src="https://user-images.githubusercontent.com/58373811/236402586-ebca72c1-9e9c-4ed4-a5da-ef29b1c1b70c.png">

<img width="129" alt="image" src="https://user-images.githubusercontent.com/58373811/236402734-8c5ddf13-b68b-4c8d-8608-514409f0f837.png">

<img width="171" alt="image" src="https://user-images.githubusercontent.com/58373811/236402784-ecb46ce0-ce16-4678-9010-132a9d372abe.png">

<img width="643" alt="image" src="https://user-images.githubusercontent.com/58373811/236405312-49e11951-6635-4f9c-876a-6c294eea75bf.png">

<img width="638" alt="image" src="https://user-images.githubusercontent.com/58373811/236403844-fadf3384-b05d-4578-afe3-a9ac3581622e.png">


# Interesting Code:
```
public static void reportsLoops(LocalDate filterDate, String filter, String vendor) { // one loop for all 6 menu options
        int[] itemDate = new int[2]; // initialized to 0 by default
        int[] targetDate = new int[2]; // 0 by default
        String itemVendor = ""; // blank by default
        if (!vendor.equals("")){filter = vendor;}
        System.out.printf("%50s %s\n\n", filter, "Transactions:");

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
        System.out.println();
    }
```

The code above is interesting because it handles all 5 report queries: Month To Date, Prev Month, Year To Date, Prev Year,
and Search By Vendor. It does this by using parameters passed in from the user's choice in the Reports Menu to set the values of the variables that will be compared in the for-loop. The values that are to be compared from the current item in the loop and the values that are to be compared from the target date or vendor are set based on the conditionals of the filter we passed in as a parameter, and in this way, we only print out the matching entries for the fields that we need, while "ignoring" the other fields.
