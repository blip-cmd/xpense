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
        System.out.println(BOLD + BLUE + "    Welcome to Xpense - Project Financial Tracker" + RESET);
        System.out.println(CYAN + "===============================================" + RESET);
        boolean running = true;
        while (running) {
            System.out.println(BOLD + "\n=== MAIN MENU ===" + RESET);
            System.out.println(GREEN + "1. Add Expenditure" + RESET);
            System.out.println("2. List Expenditures");
            System.out.println("3. Add Bank Account");
            System.out.println("4. List Bank Accounts");
            System.out.println("5. Add Category");
            System.out.println("6. List Categories");
            System.out.println(YELLOW + "7. View Alerts" + RESET);
            System.out.println("8. Search & Sort");
            System.out.println(PURPLE + "9. Generate Reports" + RESET);
            System.out.println(CYAN + "10. Bank Overview" + RESET);
            System.out.println("11. Receipt Management");
            System.out.println(BLUE + "12. Help & About" + RESET);
            System.out.println(RED + "0. Exit" + RESET);
            System.out.println("----------------------------------------------");
            System.out.print(BOLD + "Select an option (0-12): " + RESET);
            String input = scanner.nextLine().trim();
            switch (input) {
                case "0": running = false; break;
                case "1": addExpenditure(); break;
                case "2": listExpenditures(); break;
                case "3": addBankAccount(); break;
                case "4": listBankAccounts(); break;
                case "5": addCategory(); break;
                case "6": listCategories(); break;
                case "7": xpense.getAlertSystem().displayAllAlerts(); waitForKeyPress(); break;
                case "8": searchAndSortMenu(); break;
                case "9": generateReportsMenu(); break;
                case "10": bankOverviewMenu(); break;
                case "11": receiptManagementMenu(); break;
                case "12": showHelpAndAbout(); break;
                default: 
                    System.out.println(RED + "❌ Invalid option. Please enter a number between 0-12." + RESET); 
                    waitForKeyPress();
                    break;
            }
        }
        System.out.println(GREEN + "✅ Exiting Xpense CLI. Goodbye!" + RESET);
        scanner.close();
    }

    private void addExpenditure() {
        System.out.println(BOLD + CYAN + "\n=== ADD NEW EXPENDITURE ===" + RESET);
        System.out.println(YELLOW + "💡 Tip: Enter 'cancel' or '0' at any time to go back" + RESET);
        
        String id = getInputWithCancel("Expenditure ID");
        if (id == null) return;
        
        String description = getInputWithCancel("Description");
        if (description == null) return;
        if (description.trim().isEmpty()) {
            System.out.println(RED + "❌ Description cannot be blank. Please provide a valid description." + RESET);
            return;
        }
        
        System.out.print("Amount (GHc): ");
        String amountStr = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(amountStr) || "0".equals(amountStr)) {
            System.out.println(RED + "❌ Operation cancelled." + RESET);
            return;
        }
        
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println(RED + "❌ Amount must be greater than zero." + RESET);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "❌ Invalid amount format. Please enter a valid number." + RESET);
            return;
        }
        
        String categoryName = getInputWithCancel("Category name");
        if (categoryName == null) return;
        
        if (!xpense.getCategoryManager().validateCategory(categoryName)) {
            System.out.println("❌ Category '" + categoryName + "' does not exist.");
            System.out.print("Would you like to create this category now? (y/n): ");
            String createChoice = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(createChoice) || "yes".equalsIgnoreCase(createChoice)) {
                createCategoryFromAdd(categoryName);
            } else {
                System.out.println("❌ Operation cancelled. Please use an existing category or create the category first.");
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
            System.out.println("❌ Bank account '" + bankAccountId + "' does not exist.");
            return;
        }
        
        Expenditure exp = new Expenditure(id, description, amount, category, LocalDateTime.now(), phase);
        exp.setBankAccountId(bankAccountId);
        boolean added = xpense.addExpenditure(exp);
        if (added) {
            System.out.println("✅ Expenditure added successfully and bank account debited.");
        } else {
            System.out.println("❌ Failed to add expenditure. Please check alerts for details.");
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
            System.out.println("✅ Category '" + categoryName + "' created successfully.");
        } else {
            System.out.println("❌ Failed to create category.");
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
                    System.out.println("❌ Invalid option. Please enter a number between 0-7."); 
                    waitForKeyPress();
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
                    System.out.println("❌ Invalid option. Please enter a number between 0-6."); 
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
                    System.out.println("❌ Invalid option. Please enter a number between 0-3."); 
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
                    System.out.println("❌ Invalid option. Please enter a number between 0-3."); 
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
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
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
            System.out.println("Invalid amount format. Please enter valid numbers.");
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
        System.out.println("ID | Description | Amount | Category | Date | Phase | Account");
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            System.out.printf("%s | %s | GHc%s | %s | %s | %s | %s\n",
                e.getId(), e.getDescription(), e.getAmount(),
                e.getCategory().getName(), e.getDateTime().toLocalDate(), 
                e.getPhase(), e.getBankAccountId());
        }
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
            System.out.println("Invalid input format. Please enter valid numbers.");
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
            System.out.println("Invalid price format. Please enter a valid number.");
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
        String expId = scanner.nextLine();
        System.out.print("Enter receipt file path: ");
        String receiptPath = scanner.nextLine();
        
        SimpleArrayList<Expenditure> expenditures = xpense.getAllExpenditures();
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (exp.getId().equals(expId)) {
                exp.setReceiptInfo(receiptPath);
                System.out.println("Receipt linked to expenditure successfully.");
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
        System.out.println("• Enter '0' at any menu to go back or exit");
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
            System.out.println("❌ Operation cancelled.");
            return null;
        }
        return input;
    }

    private boolean isValidExit(String input) {
        return "0".equals(input) || "exit".equalsIgnoreCase(input) || "back".equalsIgnoreCase(input);
    }
}