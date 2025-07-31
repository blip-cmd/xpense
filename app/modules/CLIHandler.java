package app.modules;

import app.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class CLIHandler {
    // ANSI Color codes for CLI enhancement
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD = "\u001B[1m";
    
    private final XpenseSystem xpense;
    private final Scanner scanner;

    public CLIHandler(XpenseSystem xpenseSystem) {
        this.xpense = xpenseSystem;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println(CYAN + "===============================================" + RESET);
        System.out.println(BOLD + BLUE + "    Welcome to Xpense - Your Project's Financial Tracker" + RESET);
        System.out.println(CYAN + "===============================================" + RESET);
        boolean running = true;
        while (running) {
            System.out.println(BOLD + "\n=== MAIN MENU ===" + RESET);
            System.out.println(GREEN + "1. Add Expenditure" + RESET);
            System.out.println("2. List Expenditures");
            System.out.println("3. View Expenditure Details");
            System.out.println("4. Add Bank Account");
            System.out.println("5. List Bank Accounts");
            System.out.println("6. Add Category");
            System.out.println("7. List Categories");
            System.out.println(YELLOW + "8. View Alerts" + RESET);
            System.out.println("9. Search & Sort");
            System.out.println(PURPLE + "10. Generate Reports" + RESET);
            System.out.println(CYAN + "11. Bank Overview" + RESET);
            System.out.println("12. Receipt Management");
            System.out.println(BLUE + "13. Help & About" + RESET);
            System.out.println(RED + "0. Exit" + RESET);
            System.out.println("----------------------------------------------");
            System.out.print(BOLD + "Select an option (0-13): " + RESET);
            String input = scanner.nextLine().trim();
            switch (input) {
                case "0": running = false; break;
                case "1": addExpenditure(); break;
                case "2": listExpenditures(); break;
                case "3": viewExpenditureDetails(); break;
                case "4": addBankAccount(); break;
                case "5": listBankAccounts(); break;
                case "6": addCategory(); break;
                case "7": listCategories(); break;
                case "8": xpense.getAlertSystem().displayAllAlerts(); waitForKeyPress(); break;
                case "9": searchAndSortMenu(); break;
                case "10": generateReportsMenu(); break;
                case "11": bankOverviewMenu(); break;
                case "12": receiptManagementMenu(); break;
                case "13": showHelpAndAbout(); break;
                default: 
                    displayMenuOptionHelp("0-13");
                    break;
            }
        }
        System.out.println(GREEN + "Exiting Xpense CLI. Goodbye!" + RESET);
        scanner.close();
    }

    private void addExpenditure() {
        System.out.println(BOLD + CYAN + "\n=== ADD NEW EXPENDITURE ===" + RESET);
        System.out.println(YELLOW + "Tip: Enter 'cancel' or '0' at any time to go back" + RESET);
        
        String description = getInputWithCancel("Description");
        if (description == null) return;
        if (description.trim().isEmpty()) {
            System.out.println(RED + "Description cannot be blank. Please provide a valid description." + RESET);
            return;
        }
        
        System.out.print("Amount (GHc): ");
        String amountStr = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(amountStr) || "0".equals(amountStr)) {
            System.out.println(RED + "Operation cancelled." + RESET);
            return;
        }
        
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println(RED + "Amount must be greater than zero." + RESET);
                return;
            }
        } catch (NumberFormatException e) {
            displayAmountFormatHelp();
            return;
        }
        
        String categoryName = getInputWithCancel("Category name");
        if (categoryName == null) return;
        
        if (!xpense.getCategoryManager().validateCategory(categoryName)) {
            displayCategoryFormatHelp();
            System.out.print("Would you like to create this category now? (y/n): ");
            String createChoice = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(createChoice) || "yes".equalsIgnoreCase(createChoice)) {
                createCategoryFromAdd(categoryName);
            } else {
                System.out.println("Operation cancelled. Please use an existing category or create the category first.");
                return;
            }
        }
        
        Category category = null;
        SimpleArrayList<Category> cats = xpense.getAllCategories();
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getName().equalsIgnoreCase(categoryName)) {
                category = cats.get(i);
                break;
            }
        }
        
        String phase = getInputWithCancel("Phase");
        if (phase == null) return;
        if (phase.trim().isEmpty()) {
            phase = "active"; // Default phase
            System.out.println("ℹ️ Using default phase: active");
        }
        
        String bankAccountId = getInputWithCancel("Bank Account ID");
        if (bankAccountId == null) return;
        
        if (xpense.getBankLedger().getAccount(bankAccountId) == null) {
            displayAccountIdFormatHelp();
            return;
        }
        
        Expenditure exp = new Expenditure(description, amount, category, LocalDateTime.now(), phase);
        exp.setBankAccountId(bankAccountId);
        boolean added = xpense.addExpenditure(exp);
        if (added) {
            // Get the generated ID from the expenditure
            String generatedId = exp.getId();
            System.out.println("Expenditure added successfully with ID: " + generatedId + " and bank account debited.");
        } else {
            System.out.println("Failed to add expenditure. Please check alerts for details.");
        }
        waitForKeyPress();
    }
    
    private void createCategoryFromAdd(String categoryName) {
        System.out.println("\n=== CREATE NEW CATEGORY ===");
        String categoryId = "CAT_" + System.currentTimeMillis(); // Auto-generate ID
        System.out.print("Category description: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = "Auto-created category";
        
        System.out.print("Category color (or press Enter for default 'blue'): ");
        String color = scanner.nextLine().trim();
        if (color.isEmpty()) color = "blue";
        
        Category newCategory = new Category(categoryId, categoryName, description, color);
        boolean added = xpense.addCategory(newCategory);
        if (added) {
            System.out.println("Category '" + categoryName + "' created successfully.");
        } else {
            System.out.println("Failed to create category.");
        }
    }

    private void listExpenditures() {
        SimpleArrayList<Expenditure> exps = xpense.getAllExpenditures();
        System.out.println("\n=== ALL EXPENDITURES ===");
        if (exps.size() == 0) {
            System.out.println("No expenditures found.");
            waitForKeyPress();
            return;
        }
        
        System.out.println("ID | Description | Amount (GHc) | Category | Date | Phase | Account");
        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i < exps.size(); i++) {
            Expenditure e = exps.get(i);
            System.out.printf("%s | %s | GHc %.2f | %s | %s | %s | %s\n",
                e.getId(), 
                truncateString(e.getDescription(), 15), 
                e.getAmount(),
                e.getCategory().getName(), 
                e.getDateTime().toLocalDate(), 
                e.getPhase(), 
                e.getBankAccountId() != null ? e.getBankAccountId() : "N/A");
        }
        waitForKeyPress();
    }

    private void viewExpenditureDetails() {
        System.out.print("Enter expenditure ID to view details: ");
        String id = scanner.nextLine().trim();
        
        if (id.isEmpty()) {
            displayExpenditureIdFormatHelp();
            waitForKeyPress();
            return;
        }
        
        SimpleArrayList<Expenditure> expenditures = xpense.getAllExpenditures();
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (exp.getId().equalsIgnoreCase(id)) {
                displayExpenditureDetails(exp);
                waitForKeyPress();
                return;
            }
        }
        System.out.println("Expenditure with ID '" + id + "' not found.");
        waitForKeyPress();
    }
    
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    private void addBankAccount() {
        System.out.print("Account ID: ");
        String id = scanner.nextLine();
        System.out.print("Account Name: ");
        String name = scanner.nextLine();
        System.out.print("Initial Balance: ");
        BigDecimal bal = new BigDecimal(scanner.nextLine());
        BankAccount acct = new BankAccount(id, name, bal);
        boolean added = xpense.addBankAccount(acct);
        if (added) System.out.println("Bank account added.");
        else System.out.println("Failed to add bank account.");
    }

    private void listBankAccounts() {
        SimpleArrayList<BankAccount> acts = xpense.getAllBankAccounts();
        System.out.println("ID | Name | Balance");
        for (int i = 0; i < acts.size(); i++) {
            BankAccount b = acts.get(i);
            System.out.printf("%s | %s | %s\n", b.getAccountNumber(), b.getAccountName(), b.getBalance());
        }
    }

    private void addCategory() {
        System.out.print("Category ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Color: ");
        String color = scanner.nextLine();
        Category c = new Category(id, name, desc, color);
        boolean added = xpense.addCategory(c);
        if (added) System.out.println("Category added.");
        else System.out.println("Failed to add category.");
    }

    private void listCategories() {
        SimpleArrayList<Category> cats = xpense.getAllCategories();
        System.out.println("ID | Name | Description | Color");
        for (int i = 0; i < cats.size(); i++) {
            Category c = cats.get(i);
            System.out.printf("%s | %s | %s | %s\n", c.getId(), c.getName(), c.getDescription(), c.getColor());
        }
    }

    private void searchAndSortMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== SEARCH & SORT MENU ===");
            System.out.println("1. Sort by Category (Alphabetical)");
            System.out.println("2. Sort by Date (Chronological)");
            System.out.println("3. Search by Time Range");
            System.out.println("4. Search by Category");
            System.out.println("5. Search by Cost Range");
            System.out.println("6. Search by Bank Account");
            System.out.println("7. Search by Phase");
            System.out.println("0. Back to Main Menu");
            System.out.println("----------------------------------------------");
            System.out.print("Select an option (0-7): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "0": running = false; break;
                case "1": sortByCategory(); waitForKeyPress(); break;
                case "2": sortByDate(); waitForKeyPress(); break;
                case "3": searchByTimeRange(); waitForKeyPress(); break;
                case "4": searchByCategory(); waitForKeyPress(); break;
                case "5": searchByCostRange(); waitForKeyPress(); break;
                case "6": searchByBankAccount(); waitForKeyPress(); break;
                case "7": searchByPhase(); waitForKeyPress(); break;
                default: 
                    displayMenuOptionHelp("0-7");
                    break;
            }
        }
    }

    private void generateReportsMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== GENERATE REPORTS MENU ===");
            System.out.println("1. Cost Analysis Report");
            System.out.println("2. Monthly Burn Rate");
            System.out.println("3. Weekly Burn Rate");
            System.out.println("4. Profitability Forecast");
            System.out.println("5. Building Material Cost Analysis");
            System.out.println("6. Phase Analysis Report");
            System.out.println("0. Back to Main Menu");
            System.out.println("----------------------------------------------");
            System.out.print("Select an option (0-6): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "0": running = false; break;
                case "1": generateCostAnalysis(); waitForKeyPress(); break;
                case "2": generateMonthlyBurnReport(); waitForKeyPress(); break;
                case "3": generateWeeklyBurnReport(); waitForKeyPress(); break;
                case "4": generateProfitabilityForecast(); waitForKeyPress(); break;
                case "5": generateBuildingMaterialAnalysis(); waitForKeyPress(); break;
                case "6": generatePhaseAnalysis(); waitForKeyPress(); break;
                default: 
                    System.out.println("Invalid option. Please enter a number between 0-6."); 
                    waitForKeyPress();
                    break;
            }
        }
    }

    private void bankOverviewMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== BANK OVERVIEW MENU ===");
            System.out.println("1. View All Account Balances");
            System.out.println("2. View Account Expenditure History");
            System.out.println("3. Account Summary Report");
            System.out.println("0. Back to Main Menu");
            System.out.println("----------------------------------------------");
            System.out.print("Select an option (0-3): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "0": running = false; break;
                case "1": viewAllBalances(); waitForKeyPress(); break;
                case "2": viewAccountHistory(); waitForKeyPress(); break;
                case "3": generateAccountSummary(); waitForKeyPress(); break;
                default: 
                    System.out.println("Invalid option. Please enter a number between 0-3."); 
                    waitForKeyPress();
                    break;
            }
        }
    }

    private void receiptManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== RECEIPT MANAGEMENT MENU ===");
            System.out.println("1. Add Receipt");
            System.out.println("2. View All Receipts");
            System.out.println("3. Link Receipt to Expenditure");
            System.out.println("0. Back to Main Menu");
            System.out.println("----------------------------------------------");
            System.out.print("Select an option (0-3): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "0": running = false; break;
                case "1": addReceipt(); waitForKeyPress(); break;
                case "2": viewAllReceipts(); waitForKeyPress(); break;
                case "3": linkReceiptToExpenditure(); waitForKeyPress(); break;
                default: 
                    System.out.println("Invalid option. Please enter a number between 0-3."); 
                    waitForKeyPress();
                    break;
            }
        }
    }

    // Search and Sort Implementation Methods
    private void sortByCategory() {
        SimpleArrayList<Expenditure> sorted = xpense.getSearchSortModule()
            .sortByCategoryAlphabetical(xpense.getAllExpenditures());
        displayExpenditures(sorted, "SORTED BY CATEGORY (A-Z)");
    }

    private void sortByDate() {
        SimpleArrayList<Expenditure> sorted = xpense.getSearchSortModule()
            .sortByDateChronological(xpense.getAllExpenditures());
        displayExpenditures(sorted, "SORTED BY DATE (CHRONOLOGICAL)");
    }

    private void searchByTimeRange() {
        try {
            System.out.print("Start date (YYYY-MM-DD): ");
            String startStr = scanner.nextLine();
            System.out.print("End date (YYYY-MM-DD): ");
            String endStr = scanner.nextLine();
            
            LocalDate startDate = LocalDate.parse(startStr);
            LocalDate endDate = LocalDate.parse(endStr);
            
            SimpleArrayList<Expenditure> results = xpense.getSearchSortModule()
                .searchByTimeRange(xpense.getAllExpenditures(), startDate, endDate);
            displayExpenditures(results, "SEARCH RESULTS: " + startStr + " to " + endStr);
        } catch (Exception e) {
            displayDateFormatHelp();
        }
    }

    private void searchByCategory() {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        
        SimpleArrayList<Expenditure> results = xpense.getSearchSortModule()
            .searchByCategory(xpense.getAllExpenditures(), categoryName);
        displayExpenditures(results, "SEARCH RESULTS: Category '" + categoryName + "'");
    }

    private void searchByCostRange() {
        try {
            System.out.print("Minimum amount: ");
            BigDecimal minAmount = new BigDecimal(scanner.nextLine());
            System.out.print("Maximum amount: ");
            BigDecimal maxAmount = new BigDecimal(scanner.nextLine());
            
            SimpleArrayList<Expenditure> results = xpense.getSearchSortModule()
                .searchByCostRange(xpense.getAllExpenditures(), minAmount, maxAmount);
            displayExpenditures(results, "SEARCH RESULTS: GHc" + minAmount + " to GHc" + maxAmount);
        } catch (Exception e) {
            displayAmountFormatHelp();
        }
    }

    private void searchByBankAccount() {
        System.out.print("Enter bank account ID: ");
        String accountId = scanner.nextLine();
        
        SimpleArrayList<Expenditure> results = xpense.getSearchSortModule()
            .searchByBankAccount(xpense.getAllExpenditures(), accountId);
        displayExpenditures(results, "SEARCH RESULTS: Account '" + accountId + "'");
    }

    private void searchByPhase() {
        System.out.print("Enter phase: ");
        String phase = scanner.nextLine();
        
        SimpleArrayList<Expenditure> results = xpense.getSearchSortModule()
            .searchByPhase(xpense.getAllExpenditures(), phase);
        displayExpenditures(results, "SEARCH RESULTS: Phase '" + phase + "'");
    }

    private void displayExpenditures(SimpleArrayList<Expenditure> expenditures, String title) {
        System.out.println("\n=== " + title + " ===");
        if (expenditures.size() == 0) {
            System.out.println("No expenditures found.");
            return;
        }
        System.out.println("ID | Description | Amount | Category | Date | Phase | Account | Receipt");
        System.out.println("---|-------------|--------|----------|------|-------|---------|--------");
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            String receiptDisplay = (e.getReceiptInfo() != null && !e.getReceiptInfo().trim().isEmpty()) 
                ? e.getReceiptInfo() : "No receipt";
            System.out.printf("%s | %s | GHc%s | %s | %s | %s | %s | %s\n",
                e.getId(), e.getDescription(), e.getAmount(),
                e.getCategory().getName(), e.getDateTime().toLocalDate(), 
                e.getPhase(), e.getBankAccountId(), receiptDisplay);
        }
    }

    /**
     * Display detailed view of a single expenditure including receipt information
     */
    private void displayExpenditureDetails(Expenditure expenditure) {
        System.out.println("\n=== EXPENDITURE DETAILS ===");
        System.out.println("ID: " + expenditure.getId());
        System.out.println("Description: " + expenditure.getDescription());
        System.out.println("Amount: GHc" + expenditure.getAmount());
        System.out.println("Category: " + expenditure.getCategory().getName());
        System.out.println("Date: " + expenditure.getDateTime().toLocalDate());
        System.out.println("Time: " + expenditure.getDateTime().toLocalTime());
        System.out.println("Phase: " + expenditure.getPhase());
        System.out.println("Bank Account: " + expenditure.getBankAccountId());
        
        String receiptInfo = expenditure.getReceiptInfo();
        if (receiptInfo != null && !receiptInfo.trim().isEmpty()) {
            System.out.println("Receipt: " + receiptInfo);
            System.out.println("Receipt Status: Linked");
        } else {
            System.out.println("Receipt: No receipt linked");
            System.out.println("Receipt Status: Not linked");
        }
        System.out.println("================================");
    }

    // Report Generation Methods
    private void generateCostAnalysis() {
        String analysis = xpense.getAnalyticsModule().generateCostAnalysis(xpense.getAllExpenditures());
        System.out.println("\n" + analysis);
    }

    private void generateMonthlyBurnReport() {
        BigDecimal monthlyBurn = xpense.getAnalyticsModule().calculateMonthlyBurn(xpense.getAllExpenditures());
        System.out.println("\n=== MONTHLY BURN RATE ===");
        System.out.println("Monthly Burn Rate: GHc " + monthlyBurn);
    }

    private void generateWeeklyBurnReport() {
        BigDecimal weeklyBurn = xpense.getAnalyticsModule().calculateWeeklyBurn(xpense.getAllExpenditures());
        System.out.println("\n=== WEEKLY BURN RATE ===");
        System.out.println("Weekly Burn Rate: GHc " + weeklyBurn);
    }

    private void generateProfitabilityForecast() {
        try {
            System.out.print("Enter projected revenue: GHc");
            BigDecimal revenue = new BigDecimal(scanner.nextLine());
            System.out.print("Enter forecast period (months): ");
            int months = Integer.parseInt(scanner.nextLine());
            
            String forecast = xpense.getAnalyticsModule()
                .generateProfitabilityForecast(xpense.getAllExpenditures(), revenue, months);
            System.out.println("\n" + forecast);
        } catch (Exception e) {
            displayAmountFormatHelp();
        }
    }

    private void generateBuildingMaterialAnalysis() {
        try {
            System.out.print("Enter target house price: GHc");
            BigDecimal housePrice = new BigDecimal(scanner.nextLine());
            
            String analysis = xpense.getAnalyticsModule()
                .analyzeBuildingMaterialCosts(xpense.getAllExpenditures(), housePrice);
            System.out.println("\n" + analysis);
        } catch (Exception e) {
            displayAmountFormatHelp();
        }
    }

    private void generatePhaseAnalysis() {
        String analysis = xpense.getAnalyticsModule().generatePhaseAnalysis(xpense.getAllExpenditures());
        System.out.println("\n" + analysis);
    }

    // Bank Overview Methods
    private void viewAllBalances() {
        SimpleArrayList<BankAccount> accounts = xpense.getAllBankAccounts();
        System.out.println("\n=== ALL ACCOUNT BALANCES ===");
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount account = accounts.get(i);
            System.out.printf("%s | %s | GHc%s\n", 
                account.getAccountNumber(), account.getAccountName(), account.getBalance());
            totalBalance = totalBalance.add(account.getBalance());
        }
        System.out.println("Total Balance Across All Accounts: GHc" + totalBalance);
    }

    private void viewAccountHistory() {
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine();
        
        BankAccount account = xpense.getBankLedger().getAccount(accountId);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        SimpleArrayList<Expenditure> accountExpenditures = account.getExpenditures();
        displayExpenditures(accountExpenditures, "EXPENDITURE HISTORY: " + accountId);
    }

    private void generateAccountSummary() {
        SimpleArrayList<BankAccount> accounts = xpense.getAllBankAccounts();
        System.out.println("\n=== ACCOUNT SUMMARY REPORT ===");
        
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount account = accounts.get(i);
            SimpleArrayList<Expenditure> accountExps = account.getExpenditures();
            BigDecimal totalSpent = BigDecimal.ZERO;
            
            for (int j = 0; j < accountExps.size(); j++) {
                totalSpent = totalSpent.add(accountExps.get(j).getAmount());
            }
            
            System.out.printf("Account: %s (%s)\n", account.getAccountNumber(), account.getAccountName());
            System.out.printf("  Current Balance: GHc%s\n", account.getBalance());
            System.out.printf("  Total Spent: GHc%s\n", totalSpent);
            System.out.printf("  Number of Expenditures: %d\n\n", accountExps.size());
        }
    }

    // Receipt Management Methods
    private void addReceipt() {
        System.out.print("Receipt ID: ");
        String receiptId = scanner.nextLine();
        System.out.print("Expense Code: ");
        String expenseCode = scanner.nextLine();
        System.out.print("File Path: ");
        String filePath = scanner.nextLine();
        
        Receipt receipt = new Receipt(receiptId, expenseCode, filePath, LocalDateTime.now());
        xpense.getReceiptHandler().addReceipt(receipt);
        System.out.println("Receipt added successfully.");
    }

    private void viewAllReceipts() {
        SimpleArrayList<Receipt> receipts = xpense.getReceiptHandler().getAllReceipts();
        System.out.println("\n=== ALL RECEIPTS ===");
        System.out.println("Receipt ID | Expense Code | File Path | Timestamp");
        for (int i = 0; i < receipts.size(); i++) {
            Receipt r = receipts.get(i);
            System.out.printf("%s | %s | %s | %s\n", 
                r.getReceiptId(), r.getExpenseCode(), r.getFilePath(), r.getTimestamp());
        }
    }

    private void linkReceiptToExpenditure() {
        System.out.print("Enter expenditure ID: ");
        String expId = scanner.nextLine().trim();
        System.out.print("Enter receipt file path: ");
        String receiptPath = scanner.nextLine().trim();
        
        if (expId.isEmpty()) {
            System.out.println("Invalid expenditure ID.");
            return;
        }
        
        if (receiptPath.isEmpty()) {
            System.out.println("Invalid receipt path.");
            return;
        }
        
        SimpleArrayList<Expenditure> expenditures = xpense.getAllExpenditures();
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (exp.getId().equals(expId)) {
                exp.setReceiptInfo(receiptPath);
                // Save the updated data to file
                xpense.saveAll();
                System.out.println("Receipt linked to expenditure successfully and saved to file.");
                return;
            }
        }
        System.out.println("Expenditure not found.");
    }

    // Utility methods for improved UX
    private void waitForKeyPress() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void showHelpAndAbout() {
        System.out.println("\n===============================================");
        System.out.println("    XPENSE - PROJECT FINANCIAL TRACKER");
        System.out.println("===============================================");
        System.out.println("Version: 1.0");
        System.out.println("Developer: Nkwa Real Estate Team");
        System.out.println("Purpose: Track construction project expenditures");
        System.out.println();
        System.out.println("=== HELP & NAVIGATION TIPS ===");
        System.out.println("- Enter '0' at any menu to go back or exit");
        System.out.println("• Use 'cancel' to cancel current operation");
        System.out.println("• All amounts are in Ghana Cedis (GHc)");
        System.out.println("• Dates are automatically recorded");
        System.out.println("• Bank accounts are automatically debited");
        System.out.println("• Categories must exist before adding expenditures");
        System.out.println();
        System.out.println("=== QUICK ACCESS ===");
        System.out.println("Main Menu Option 1: Add Expenditure");
        System.out.println("Main Menu Option 2: View All Expenditures");
        System.out.println("Main Menu Option 7: Check Alerts");
        System.out.println("Main Menu Option 9: Generate Reports");
        System.out.println();
        System.out.println("For technical support, check the documentation.");
        waitForKeyPress();
    }

    private boolean promptToContinueOrCancel(String operation) {
        System.out.println("\nOptions:");
        System.out.println("1. Continue with " + operation);
        System.out.println("0. Cancel and go back");
        System.out.print("Choose (0/1): ");
        String choice = scanner.nextLine().trim();
        return "1".equals(choice);
    }

    private String getInputWithCancel(String prompt) {
        System.out.print(prompt + " (or 'cancel'/'0' to go back): ");
        String input = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(input) || "0".equals(input)) {
            System.out.println("Operation cancelled.");
            return null;
        }
        return input;
    }

    private boolean isValidExit(String input) {
        return "0".equals(input) || "exit".equalsIgnoreCase(input) || "back".equalsIgnoreCase(input);
    }

    // Format help methods for better user experience
    private void displayAmountFormatHelp() {
        System.out.println(RED + "X Invalid amount format!" + RESET);
        System.out.println(YELLOW + "Please enter amounts in one of these formats:" + RESET);
        System.out.println("   • " + GREEN + "1500" + RESET + " (for GHc 1,500)");
        System.out.println("   • " + GREEN + "1500.00" + RESET + " (for GHc 1,500.00)");
        System.out.println("   • " + GREEN + "250.50" + RESET + " (for GHc 250.50)");
        System.out.println("   • " + GREEN + "75" + RESET + " (for GHc 75)");
        System.out.println(CYAN + "Examples: 1000, 1500.50, 250, 75.25" + RESET);
        System.out.println(RED + "X Don't include: Currency symbols (GHc, ¢), commas, or letters" + RESET);
        waitForKeyPress();
    }

    private void displayDateFormatHelp() {
        System.out.println(RED + "X Invalid date format!" + RESET);
        System.out.println(YELLOW + "Please enter dates in YYYY-MM-DD format:" + RESET);
        System.out.println("   • " + GREEN + "2025-07-31" + RESET + " (July 31, 2025)");
        System.out.println("   • " + GREEN + "2025-01-15" + RESET + " (January 15, 2025)");
        System.out.println("   • " + GREEN + "2025-12-25" + RESET + " (December 25, 2025)");
        System.out.println(CYAN + "Format: YYYY-MM-DD (Year-Month-Day)" + RESET);
        System.out.println(RED + "X Don't use: DD/MM/YYYY, MM-DD-YYYY, or other formats" + RESET);
        waitForKeyPress();
    }

    private void displayAccountIdFormatHelp() {
        System.out.println(RED + "X Invalid account ID format!" + RESET);
        System.out.println(YELLOW + "Please enter account IDs in these formats:" + RESET);
        System.out.println("   • " + GREEN + "ACC001" + RESET + " (3 letters + 3 numbers)");
        System.out.println("   • " + GREEN + "CAL001" + RESET + " (Bank initials + numbers)");
        System.out.println("   • " + GREEN + "UMB002" + RESET + " (Another example)");
        System.out.println(CYAN + "Examples from your system:" + RESET);
        listExistingAccountIds();
        waitForKeyPress();
    }

    private void displayCategoryFormatHelp() {
        System.out.println(RED + "X Category doesn't exist!" + RESET);
        System.out.println(YELLOW + "Available categories in your system:" + RESET);
        SimpleArrayList<Category> categories = xpense.getAllCategories();
        if (categories.size() == 0) {
            System.out.println("   • " + RED + "No categories found. Please create a category first." + RESET);
        } else {
            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                System.out.println("   • " + GREEN + cat.getName() + RESET + " (" + cat.getDescription() + ")");
            }
        }
        System.out.println(CYAN + "Enter the exact category name from the list above" + RESET);
        System.out.println(BLUE + "Or type the category name to create a new one" + RESET);
        waitForKeyPress();
    }

    private void displayMenuOptionHelp(String validRange) {
        System.out.println(RED + "X Invalid menu option!" + RESET);
        System.out.println(YELLOW + "Please enter a number from " + validRange + RESET);
        System.out.println(CYAN + "Examples: " + RESET);
        if (validRange.contains("0-13")) {
            System.out.println("   • " + GREEN + "1" + RESET + " - Add Expenditure");
            System.out.println("   • " + GREEN + "2" + RESET + " - List Expenditures");
            System.out.println("   • " + GREEN + "8" + RESET + " - View Alerts");
            System.out.println("   • " + GREEN + "0" + RESET + " - Exit");
        } else if (validRange.contains("0-7")) {
            System.out.println("   • " + GREEN + "1" + RESET + " - Sort by Category");
            System.out.println("   • " + GREEN + "3" + RESET + " - Search by Time Range");
            System.out.println("   • " + GREEN + "0" + RESET + " - Back to Main Menu");
        }
        waitForKeyPress();
    }

    private void listExistingAccountIds() {
        SimpleArrayList<BankAccount> accounts = xpense.getAllBankAccounts();
        if (accounts.size() == 0) {
            System.out.println("   • " + RED + "No accounts found. Please create an account first." + RESET);
        } else {
            for (int i = 0; i < accounts.size(); i++) {
                BankAccount acc = accounts.get(i);
                System.out.println("   • " + GREEN + acc.getAccountNumber() + RESET + " (" + acc.getAccountName() + ")");
            }
        }
    }

    private void displayExpenditureIdFormatHelp() {
        System.out.println(RED + "X Invalid expenditure ID!" + RESET);
        System.out.println(YELLOW + "Available expenditure IDs in your system:" + RESET);
        SimpleArrayList<Expenditure> expenditures = xpense.getAllExpenditures();
        if (expenditures.size() == 0) {
            System.out.println("   • " + RED + "No expenditures found. Please add an expenditure first." + RESET);
        } else {
            System.out.println(CYAN + "Recent expenditure IDs:" + RESET);
            int max = Math.min(5, expenditures.size()); // Show last 5
            for (int i = expenditures.size() - max; i < expenditures.size(); i++) {
                Expenditure exp = expenditures.get(i);
                System.out.println("   • " + GREEN + exp.getId() + RESET + " - " + truncateString(exp.getDescription(), 25));
            }
            if (expenditures.size() > 5) {
                System.out.println("   ... and " + (expenditures.size() - 5) + " more (use option 2 to view all)");
            }
        }
        System.out.println(BLUE + "Enter the exact ID from the list above" + RESET);
    }
}