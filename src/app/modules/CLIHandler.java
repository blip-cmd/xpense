package app.modules;

import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Handles the command-line interface and user interaction
 * Integrates all system modules according to functional requirements
 */

public class CLIHandler {
    private AlertSystem alertSystem;
    private Scanner scanner;

    private ExpenditureManager expenditureManager;
    private CategoryManager categoryManager;
    private FileManager fileManager;
    private ReceiptHandler receiptHandler;
    private AnalyticsModule analyticsModule;
    private BankLedger bankLedger;

    // ANSI color codes for CLI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    // private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    /**
     * ALERT SYSTEM FUNCTIONALITY
     * ──────────────────────────
     * Implemented:
     *   - Add alerts (manual and automatic)
     *   - View alerts (displayAllAlerts)
     *   - Alerts for errors and events
     * Expected:
     *   - Show alerts on login
     *   - Save alerts to file when viewed
     *   - Link alerts to bank accounts (low balance, etc.)
     *   - Persist alerts for future sessions
     */

    public CLIHandler(AlertSystem alertSystem) {
        this.alertSystem = alertSystem;

        // Micheal for data models Lion For Management
        this.expenditureManager = new ExpenditureManager();
        // Link AlertSystem to BankLedger for low balance alerts
        this.bankLedger = new BankLedger(alertSystem);

        this.categoryManager = new CategoryManager();

        this.fileManager = new FileManager();

        this.receiptHandler = new ReceiptHandler();

        this.analyticsModule = new AnalyticsModule();
        this.scanner = new Scanner(System.in);

        // Load initial data
        loadInitialData();

        // Show alerts on login
        showAlertsOnLogin();
    }

    /**
     * Load initial data from files
     */
    private void loadInitialData() {
        try {
            // Load categories first (needed for expenditure validation)
            List<Category> categories = fileManager.loadCategories("categories.txt");
            for (Category category : categories) {
                categoryManager.addCategory(category);
            }

            // Load bank accounts
            List<BankAccount> accounts = fileManager.loadBankAccounts("accounts.txt");
            for (BankAccount account : accounts) {
                bankLedger.addAccount(account);
            }

            // Load expenditures
            List<Expenditure> expenditures = fileManager.loadExpenditures("expenditures.txt");
            for (Expenditure expenditure : expenditures) {
                expenditureManager.addExpenditure(expenditure);
            }

        } catch (Exception e) {
            alertSystem.addAlert("Error loading data: " + e.getMessage(), 1);
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Expenditure Management");
        System.out.println("2. Category Management");
        System.out.println("3. Bank Account Management");
        System.out.println("4. Receipt Management");
        System.out.println("5. Analytics & Reports");
        System.out.println("6. View Alerts");
        System.out.println("7. Help Menu");
        System.out.println("8. Exit");
        // Quick options
        System.out.println("10. Add Expenditure (Quick)");
        System.out.println("11. View All Expenditures (Quick)");
        System.out.print("Select an option: ");
    }

    public void displayMenu() {
        System.out.println("Welcome to Xpense - Project Financial Tracker");
        System.out.println("==============================================");

        boolean running = true;

        while (running) {
            showMainMenu();
            String input = getUserInput();
            if (input == null)
                continue;

            if ("exit".equalsIgnoreCase(input) || "0".equals(input)) {
                running = false;
                continue;
            }

            switch (input.toLowerCase()) {
                case "1":
                    expenditureMenu();
                    break;
                case "2":
                    categoryMenu();
                    break;
                case "3":
                    bankAccountMenu();
                    break;
                case "4":
                    receiptMenu();
                    break;
                case "5":
                    analyticsMenu();
                    break;
                case "6":
                    alertSystem.displayAllAlerts();
                    break;
                case "7":
                case "help":
                    showHelpMenu();
                    break;
                case "8":
                    running = false;
                    break;
                case "10":
                    addExpenditure();
                    break;
                case "11":
                    viewAllExpenditures();
                    break;
                default:
                    System.out.println("Invalid option. Type '7', 'help', or '0' to exit.");
            }
        }

        System.out.println("Exiting Xpense CLI. Goodbye!");
        scanner.close();
    }

    private void addDummyAlert() {
        System.out.print("Enter alert message: ");
        String message = scanner.nextLine();
        System.out.print("Enter priority (1 = high, 5 = low): ");
        int priority = Integer.parseInt(scanner.nextLine());
        alertSystem.addAlert(message, priority);
        System.out.println("[+] Alert added.");
    }

    private void showHelpMenu() {
        boolean inHelp = true;
        while (inHelp) {
            System.out.println("\n--- HELP MENU ---");
            System.out.println("1. View all system alerts");
            System.out.println("2. Add a test alert manually");
            System.out.println("3. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select a help option: ");
            String helpInput = getUserInput();
            if ("exit".equalsIgnoreCase(helpInput) || "0".equals(helpInput)) {
                inHelp = false;
                continue;
            }
            switch (helpInput) {
                case "1":
                    alertSystem.displayAllAlerts();
                    saveAlertsToFile();
                    break;
                case "2":
                    addDummyAlert();
                    break;
                case "3":
                    inHelp = false;
                    break;
                default:
                    System.out.println("Invalid help option. Please select 1, 2, 3, or 0 to exit.");
            }
        }
    }

    // ===============================
    // EXPENDITURE MANAGEMENT METHODS ( LION AND MICHEAL)
    // ===============================

    private void expenditureMenu() {
        boolean inExpenditureMenu = true;
        while (inExpenditureMenu) {
            System.out.println("\n--- EXPENDITURE MANAGEMENT ---");
            System.out.println("1. Add New Expenditure");
            System.out.println("2. View All Expenditures");
            System.out.println("3. Search Expenditures");
            System.out.println("4. Sort Expenditures");
            System.out.println("5. Edit Expenditure");
            System.out.println("6. Delete Expenditure");
            System.out.println("7. Filter by Amount Range");
            System.out.println("8. View Expenditures by Category");
            System.out.println("9. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String input = getUserInput();
            if (input == null)
                continue;
            if ("exit".equalsIgnoreCase(input) || "0".equals(input)) {
                inExpenditureMenu = false;
                continue;
            }
            switch (input) {
                case "1":
                    addExpenditure();
                    break;
                case "2":
                    viewAllExpenditures();
                    break;
                case "3":
                    searchExpenditures();
                    break;
                case "4":
                    sortExpenditures();
                    break;
                case "5":
                    editExpenditure();
                    break;
                case "6":
                    deleteExpenditure();
                    break;
                case "7":
                    filterExpendituresByAmount();
                    break;
                case "8":
                    viewExpendituresByCategory();
                    break;
                case "9":
                    inExpenditureMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please select 1-9 or 0 to exit.");
            }
        }
    }

    private void addExpenditure() {
        try {
            // ... existing code ...
            
            System.out.print(CYAN + "Enter amount: " + RESET);
            String amountStr = getUserInput();
            if ("cancel".equalsIgnoreCase(amountStr)) return;
            
            float amount; // Changed from BigDecimal
            try {
                amount = Float.parseFloat(amountStr); // Changed from new BigDecimal()
                if (amount <= 0) { // Changed from compareTo()
                    System.out.println(RED + "Amount must be positive. Operation cancelled." + RESET);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid amount format. Operation cancelled." + RESET);
                return;
            }
            
            // ... rest of method ...
        } catch (Exception e) {
            System.out.println(RED + "Error adding expenditure: " + e.getMessage() + RESET);
        }
    }

    private void viewAllExpenditures() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        if (expenditures.isEmpty()) {
            System.out.println(YELLOW + "No expenditures found." + RESET);
            return;
        }

        System.out.println(BOLD + "\n=== ALL EXPENDITURES ===" + RESET);
        System.out.printf("%-10s %-20s %-10s %-15s %-15s %-20s%n",
                "ID", "Description", "Amount", "Category", "Date", "Location");
        System.out.println("─".repeat(90));

        for (Expenditure exp : expenditures) {
            System.out.printf("%-10s %-20s ₵%-9.2f %-15s %-15s %-20s%n",
                    exp.getId(),
                    truncateString(exp.getDescription(), 20),
                    exp.getAmount().doubleValue(),
                    exp.getCategory().getName(),
                    exp.getDate().toLocalDate().toString(),
                    truncateString(exp.getPhase(), 20));
        }

        System.out.println("\nTotal Amount: ₵" + expenditureManager.getTotalAmount());
    }

    private void searchExpenditures() {
        System.out.println("\n--- SEARCH EXPENDITURES ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Description");
        System.out.println("3. Search by Category");
        System.out.println("4. Search by Date");
        System.out.print("Select search type: ");

        String searchType = getUserInput();
        if (searchType == null)
            return;

        System.out.print("Enter search term: ");
        String searchTerm = getUserInput();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            System.out.println("Invalid search term.");
            return;
        }

        String type = "";
        switch (searchType) {
            case "1":
                type = "id";
                break;
            case "2":
                type = "description";
                break;
            case "3":
                type = "category";
                break;
            case "4":
                type = "date";
                break;
            default:
                System.out.println("Invalid search type.");
                return;
        }

        List<Expenditure> results = expenditureManager.searchExpenditures(searchTerm, type);
        if (results.isEmpty()) {
            System.out.println("No expenditures found matching your search.");
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            displayExpenditureList(results);
        }
    }

    private void sortExpenditures() {
        System.out.println("\n--- SORT EXPENDITURES ---");
        System.out.println("1. Sort by Date");
        System.out.println("2. Sort by Amount");
        System.out.println("3. Sort by Category");
        System.out.println("4. Sort by Description");
        System.out.print("Select sort option: ");

        String sortOption = getUserInput();
        if (sortOption == null)
            return;

        System.out.print("Sort ascending? (y/n): ");
        String ascendingStr = getUserInput();
        boolean ascending = ascendingStr != null && ascendingStr.toLowerCase().startsWith("y");

        String sortBy = "";
        switch (sortOption) {
            case "1":
                sortBy = "date";
                break;
            case "2":
                sortBy = "amount";
                break;
            case "3":
                sortBy = "category";
                break;
            case "4":
                sortBy = "description";
                break;
            default:
                System.out.println("Invalid sort option.");
                return;
        }

        List<Expenditure> sorted = expenditureManager.sortExpenditures(sortBy, ascending);
        System.out.println("\n=== SORTED EXPENDITURES ===");
        displayExpenditureList(sorted);
    }

    private void editExpenditure() {
        System.out.print("Enter expenditure ID to edit: ");
        String id = getUserInput();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Invalid ID.");
            return;
        }

        Expenditure existing = expenditureManager.getExpenditureById(id);
        if (existing == null) {
            System.out.println("Expenditure not found.");
            return;
        }

        System.out.println("Current expenditure details:");
        System.out.println(existing.toString());

        // Create updated expenditure (simplified - in practice might update individual
        // fields)
        System.out.print("Enter new description (current: " + existing.getDescription() + "): ");
        String newDescription = getUserInput();
        if (newDescription == null || newDescription.trim().isEmpty()) {
            newDescription = existing.getDescription();
        }

        System.out.print("Enter new amount (current: " + existing.getAmount() + "): ");
        String amountStr = getUserInput();
        Float newAmount = existing.getAmount();
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                newAmount = Float.parseFloat(amountStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Using current amount.");
            }
        }

        Expenditure updated = new Expenditure(
                existing.getId(),
                newDescription,
                newAmount,
                existing.getCategory(),
                existing.getDate(),
                existing.getLocation(),
                existing.getPhase(),
                existing.getBankAccountId());

        if (expenditureManager.updateExpenditure(updated)) {
            System.out.println("_/ Expenditure updated successfully!");
        } else {
            System.out.println("X Failed to update expenditure.");
        }
    }

    private void deleteExpenditure() {
        System.out.print("Enter expenditure ID to delete: ");
        String id = getUserInput();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Invalid ID.");
            return;
        }

        Expenditure existing = expenditureManager.getExpenditureById(id);
        if (existing == null) {
            System.out.println("Expenditure not found.");
            return;
        }

        System.out.println("Expenditure to delete:");
        System.out.println(existing.toString());
        System.out.print("Are you sure you want to delete this expenditure? (y/n): ");

        String confirm = getUserInput();
        if (confirm != null && confirm.toLowerCase().startsWith("y")) {
            if (expenditureManager.deleteExpenditure(id)) {
                System.out.println("✓ Expenditure deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete expenditure.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private void filterExpendituresByAmount() {
        System.out.print("Enter minimum amount: ");
        String minStr = getUserInput();
        System.out.print("Enter maximum amount: ");
        String maxStr = getUserInput();

        try {
            Float min = Float.parseFloat(minStr);
            Float max = Float.parseFloat(maxStr);

            List<Expenditure> filtered = expenditureManager.filterByAmount(min, max);
            if (filtered.isEmpty()) {
                System.out.println("No expenditures found in the specified range.");
            } else {
                System.out.println("\n=== FILTERED EXPENDITURES (₵" + min + " - ₵" + max + ") ===");
                displayExpenditureList(filtered);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format.");
        }
    }

    private void viewExpendituresByCategory() {
        System.out.print("Enter category name: ");
        String categoryName = getUserInput();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            System.out.println("Invalid category name.");
            return;
        }

        List<Expenditure> filtered = expenditureManager.getExpendituresByCategory(categoryName);
        if (filtered.isEmpty()) {
            System.out.println("No expenditures found for category: " + categoryName);
        } else {
            System.out.println("\n=== EXPENDITURES IN CATEGORY: " + categoryName.toUpperCase() + " ===");
            displayExpenditureList(filtered);
        }
    }

    private void displayExpenditureList(List<Expenditure> expenditures) {
        System.out.printf("%-10s %-20s %-10s %-15s %-15s %-20s%n",
                "ID", "Description", "Amount", "Category", "Date", "Location");
        System.out.println("─".repeat(90));

        for (Expenditure exp : expenditures) {
            System.out.printf("%-10s %-20s ₵%-9.2f %-15s %-15s %-20s%n",
                    exp.getId(),
                    truncateString(exp.getDescription(), 20),
                    exp.getAmount().doubleValue(),
                    exp.getCategory().getName(),
                    exp.getDate().toLocalDate().toString(),
                    truncateString(exp.getPhase(), 20));
        }
    }

    // ===============================
    // CATEGORY MANAGEMENT METHODS
    // ===============================

    private void categoryMenu() {
        boolean inCategoryMenu = true;
        while (inCategoryMenu) {
            System.out.println("\n--- CATEGORY MANAGEMENT ---");
            System.out.println("1. Add New Category");
            System.out.println("2. View All Categories");
            System.out.println("3. Search Categories");
            System.out.println("4. Update Category");
            System.out.println("5. Delete Category");
            System.out.println("6. Category Statistics");
            System.out.println("7. All Category Statistics");
            System.out.println("8. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String input = getUserInput();
            if (input == null)
                continue;
            if ("exit".equalsIgnoreCase(input) || "0".equals(input)) {
                inCategoryMenu = false;
                continue;
            }
            switch (input) {
                case "1":
                    addCategory();
                    break;
                case "2":
                    viewAllCategories();
                    break;
                case "3":
                    searchCategories();
                    break;
                case "4":
                    updateCategory();
                    break;
                case "5":
                    deleteCategory();
                    break;
                case "6":
                    viewCategoryStatistics();
                    break;
                case "7":
                    viewAllCategoryStatistics();
                    break;
                case "8":
                    inCategoryMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please select 1-8 or 0 to exit.");
            }
        }
    }

    private void addCategory() {
        try {
            System.out.print("Enter category ID: ");
            String id = getUserInput();
            if (id == null || id.trim().isEmpty()) {
                System.out.println("Invalid ID. Operation cancelled.");
                return;
            }

            System.out.print("Enter category name: ");
            String name = getUserInput();
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Invalid name. Operation cancelled.");
                return;
            }

            System.out.print("Enter category description: ");
            String description = getUserInput();
            if (description == null)
                description = "";

            System.out.print("Enter category color: ");
            String color = getUserInput();
            if (color == null)
                color = "blue";

            Category category = new Category(id, name, description, color);

            if (categoryManager.addCategory(category)) {
                System.out.println("✓ Category added successfully!");
                alertSystem.addAlert("New category added: " + name, 4);
            } else {
                System.out.println("✗ Failed to add category. Name might already exist.");
            }

        } catch (Exception e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    private void viewAllCategories() {
        List<Category> categories = categoryManager.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }

        System.out.println("\n=== ALL CATEGORIES ===");
        System.out.printf("%-10s %-20s %-30s %-15s%n",
                "ID", "Name", "Description", "Color");
        System.out.println("─".repeat(75));

        for (Category cat : categories) {
            System.out.printf("%-10s %-20s %-30s %-15s%n",
                    cat.getId(),
                    truncateString(cat.getName(), 20),
                    truncateString(cat.getDescription(), 30),
                    cat.getColor());
        }

        System.out.println("\nTotal Categories: " + categoryManager.getCategoryCount());
    }

    private void searchCategories() {
        System.out.print("Enter search term for category name: ");
        String searchTerm = getUserInput();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            System.out.println("Invalid search term.");
            return;
        }

        List<Category> results = categoryManager.searchCategories(searchTerm);
        if (results.isEmpty()) {
            System.out.println("No categories found matching: " + searchTerm);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            System.out.printf("%-10s %-20s %-30s %-15s%n",
                    "ID", "Name", "Description", "Color");
            System.out.println("─".repeat(75));

            for (Category cat : results) {
                System.out.printf("%-10s %-20s %-30s %-15s%n",
                        cat.getId(),
                        truncateString(cat.getName(), 20),
                        truncateString(cat.getDescription(), 30),
                        cat.getColor());
            }
        }
    }

    private void updateCategory() {
        System.out.print("Enter current category name to update: ");
        String oldName = getUserInput();
        if (oldName == null || oldName.trim().isEmpty()) {
            System.out.println("Invalid category name.");
            return;
        }

        if (!categoryManager.validateCategory(oldName)) {
            System.out.println("Category not found: " + oldName);
            return;
        }

        System.out.print("Enter new category ID: ");
        String newId = getUserInput();
        System.out.print("Enter new category name: ");
        String newName = getUserInput();
        System.out.print("Enter new category description: ");
        String newDescription = getUserInput();
        System.out.print("Enter new category color: ");
        String newColor = getUserInput();

        Category newCategory = new Category(
                newId != null ? newId : "UPD001",
                newName != null ? newName : oldName,
                newDescription != null ? newDescription : "",
                newColor != null ? newColor : "blue");

        if (categoryManager.updateCategory(oldName, newCategory)) {
            System.out.println("✓ Category updated successfully!");
        } else {
            System.out.println("✗ Failed to update category.");
        }
    }

    private void deleteCategory() {
        System.out.print("Enter category name to delete: ");
        String categoryName = getUserInput();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            System.out.println("Invalid category name.");
            return;
        }

        if (!categoryManager.validateCategory(categoryName)) {
            System.out.println("Category not found: " + categoryName);
            return;
        }

        System.out.print("Are you sure you want to delete category '" + categoryName + "'? (y/n): ");
        String confirm = getUserInput();

        if (confirm != null && confirm.toLowerCase().startsWith("y")) {
            if (categoryManager.deleteCategory(categoryName)) {
                System.out.println("✓ Category deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete category. It might be in use by expenditures.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private void viewCategoryStatistics() {
        System.out.print("Enter category name: ");
        String categoryName = getUserInput();
        if (categoryName == null || categoryName.trim().isEmpty()) {
            System.out.println("Invalid category name.");
            return;
        }

        String stats = categoryManager.getCategoryStatistics(categoryName);
        System.out.println("\n=== CATEGORY STATISTICS ===");
        System.out.println(stats);
    }

    private void viewAllCategoryStatistics() {
        String allStats = categoryManager.getAllCategoryStatistics();
        System.out.println("\n=== ALL CATEGORY STATISTICS ===");
        System.out.println(allStats);
    }

    // ===============================
    // BANK ACCOUNT MANAGEMENT METHODS (LION and MICHEAL)
    // ===============================

    private void bankAccountMenu() {
        boolean inBankMenu = true;
        while (inBankMenu) {
            System.out.println("\n--- BANK ACCOUNT MANAGEMENT ---");
            System.out.println("1. View All Accounts");
            System.out.println("2. Add New Account");
            System.out.println("3. View Account Details");
            System.out.println("4. Account Summary");
            System.out.println("5. Check Low Balance Alerts");
            System.out.println("6. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String input = getUserInput();
            if (input == null)
                continue;
            if ("exit".equalsIgnoreCase(input) || "0".equals(input)) {
                inBankMenu = false;
                continue;
            }
            switch (input) {
                case "1":
                    viewAllAccounts();
                    break;
                case "2":
                    addBankAccount();
                    break;
                case "3":
                    viewAccountDetails();
                    break;
                case "4":
                    viewAccountSummary();
                    break;
                case "5":
                    checkLowBalanceAlerts();
                    break;
                case "6":
                    inBankMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please select 1-6 or 0 to exit.");
            }
        }
    }

    private void viewAllAccounts() {
        List<BankAccount> accounts = bankLedger.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No bank accounts found.");
            return;
        }

        System.out.println("\n=== ALL BANK ACCOUNTS ===");
        System.out.printf("%-15s %-25s %-15s %-15s%n",
                "Account ID", "Account Name", "Balance", "Status");
        System.out.println("─".repeat(70));

        for (BankAccount account : accounts) {
            String status = account.getBalance() < 100f ? "LOW" : "OK";
            System.out.printf("%-15s %-25s ₵%-14.2f %-15s%n",
                    account.getAccountId(),
                    truncateString(account.getAccountName(), 25),
                    account.getBalance(),
                    status);
        }
    }

    private void addBankAccount() {
        try {
            System.out.print("Enter account ID: ");
            String accountId = getUserInput();
            if (accountId == null || accountId.trim().isEmpty()) {
                System.out.println("Invalid account ID.");
                return;
            }

            System.out.print("Enter account name: ");
            String accountName = getUserInput();
            if (accountName == null || accountName.trim().isEmpty()) {
                System.out.println("Invalid account name.");
                return;
            }

            System.out.print("Enter initial balance: ");
            String balanceStr = getUserInput();
            Float balance;
            try {
                balance = Float.parseFloat(balanceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid balance format.");
                return;
            }

            BankAccount account = new BankAccount(accountId, accountName, balance);

            if (bankLedger.addAccount(account)) {
                System.out.println("✓ Bank account added successfully!");
                alertSystem.addAlert("New bank account added: " + accountName, 4);
            } else {
                System.out.println("✗ Failed to add bank account. ID might already exist.");
            }

        } catch (Exception e) {
            System.out.println("Error adding bank account: " + e.getMessage());
        }
    }

    private void viewAccountDetails() {
        System.out.print("Enter account ID: ");
        String accountId = getUserInput();
        if (accountId == null || accountId.trim().isEmpty()) {
            System.out.println("Invalid account ID.");
            return;
        }

        BankAccount account = bankLedger.getAccount(accountId);
        if (account == null) {
            System.out.println("Account not found: " + accountId);
            return;
        }

        System.out.println("\n=== ACCOUNT DETAILS ===");
        System.out.println("Account ID: " + account.getAccountId());
        System.out.println("Account Name: " + account.getAccountName());
        System.out.println("Current Balance: ₵" + account.getBalance());
        System.out.println("Account Status: "
                + (account.getBalance() < 100f ? "LOW BALANCE" : "OK"));
    }

    private void viewAccountSummary() {
        System.out.println("\n=== ACCOUNT SUMMARY ===");
        Float    totalBalance = bankLedger.getTotalBalance();
        System.out.println("Total Balance Across All Accounts: ₵" + totalBalance);
        System.out.println("Number of Accounts: " + bankLedger.getAllAccounts().size());

        // Show accounts with low balance
        List<BankAccount> lowBalanceAccounts = new ArrayList<>();
        for (BankAccount account : bankLedger.getAllAccounts()) {
            if (account.getBalance() < 100f) {
                lowBalanceAccounts.add(account);
                // Link alert system to bank accounts for low balance
                alertSystem.checkLowFunds(account.getAccountId(), account.getBalance());
            }
        }

        if (!lowBalanceAccounts.isEmpty()) {
            System.out.println("\nAccounts with Low Balance:");
            for (BankAccount account : lowBalanceAccounts) {
                System.out.println("  - " + account.getAccountName() + ": ₵" + account.getBalance());
            }
        }
    }

    private void checkLowBalanceAlerts() {
        List<BankAccount> accounts = bankLedger.getAllAccounts();
        boolean hasLowBalance = false;

        System.out.println("\n=== LOW BALANCE CHECK ===");
        for (BankAccount account : accounts) {
            if (account.getBalance() < 100f) {
                System.out.println("LOW BALANCE: " + account.getAccountName() +
                        " (ID: " + account.getAccountId() + ") - Balance: ₵" + account.getBalance());
                // Generate alert for low balance
                alertSystem.checkLowFunds(account.getAccountId(), account.getBalance());
                hasLowBalance = true;
            }
        }

        if (!hasLowBalance) {
            System.out.println("All accounts have sufficient balance.");
        }
    }

    // ===============================
    // RECEIPT MANAGEMENT METHODS
    // ===============================

    private void receiptMenu() {
        boolean inReceiptMenu = true;
        while (inReceiptMenu) {
            System.out.println("\n--- RECEIPT MANAGEMENT ---");
            System.out.println("1. Upload Receipt");
            System.out.println("2. View All Receipts");
            System.out.println("3. Process Receipt Queue");
            System.out.println("4. Link Receipt to Expenditure");
            System.out.println("5. Receipt History");
            System.out.println("6. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String input = getUserInput();
            if (input == null)
                continue;
            if ("exit".equalsIgnoreCase(input) || "0".equals(input)) {
                inReceiptMenu = false;
                continue;
            }
            switch (input) {
                case "1":
                    uploadReceipt();
                    break;
                case "2":
                    viewAllReceipts();
                    break;
                case "3":
                    processReceiptQueue();
                    break;
                case "4":
                    linkReceiptToExpenditure();
                    break;
                case "5":
                    viewReceiptHistory();
                    break;
                case "6":
                    inReceiptMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please select 1-6 or 0 to exit.");
            }
        }
    }

    private void uploadReceipt() {
        try {
            System.out.print("Enter receipt ID: ");
            String receiptId = getUserInput();
            if (receiptId == null || receiptId.trim().isEmpty()) {
                System.out.println("Invalid receipt ID.");
                return;
            }

            System.out.print("Enter associated expenditure code (optional): ");
            String expenseCode = getUserInput();
            if (expenseCode == null)
                expenseCode = "";

            System.out.print("Enter file path or description: ");
            String filePath = getUserInput();
            if (filePath == null)
                filePath = "receipt_" + receiptId + ".jpg";

            Receipt receipt = new Receipt(receiptId, expenseCode, filePath, LocalDateTime.now());
            receiptHandler.addReceipt(receipt);

            System.out.println("✓ Receipt uploaded successfully!");
            alertSystem.addAlert("New receipt uploaded: " + receiptId, 4);

        } catch (Exception e) {
            System.out.println("Error uploading receipt: " + e.getMessage());
        }
    }

    private void viewAllReceipts() {
        List<Receipt> receipts = receiptHandler.getAllReceipts();
        if (receipts.isEmpty()) {
            System.out.println("No receipts found.");
            return;
        }

        System.out.println("\n=== ALL RECEIPTS ===");
        System.out.printf("%-15s %-20s %-30s %-20s%n",
                "Receipt ID", "Expense Code", "File Path", "Timestamp");
        System.out.println("─".repeat(85));

        for (Receipt receipt : receipts) {
            System.out.printf("%-15s %-20s %-30s %-20s%n",
                    receipt.getReceiptId(),
                    truncateString(receipt.getExpenseCode(), 20),
                    truncateString(receipt.getFilePath(), 30),
                    receipt.getTimestamp().toLocalDate().toString());
        }
    }

    private void processReceiptQueue() {
        System.out.println("\n=== RECEIPT QUEUE PROCESSING ===");
        Receipt nextReceipt = receiptHandler.getNextReceiptForProcessing();
        if (nextReceipt == null) {
            System.out.println("No receipts in processing queue.");
            return;
        }

        System.out.println("Processing receipt: " + nextReceipt.getReceiptId());
        System.out.println("File: " + nextReceipt.getFilePath());
        System.out.println("Associated Expense: " + nextReceipt.getExpenseCode());

        System.out.print("Mark as processed? (y/n): ");
        String confirm = getUserInput();
        if (confirm != null && confirm.toLowerCase().startsWith("y")) {
            receiptHandler.markReceiptProcessed(nextReceipt.getReceiptId());
            System.out.println("✓ Receipt marked as processed.");
        }
    }

    private void linkReceiptToExpenditure() {
        System.out.print("Enter receipt ID: ");
        String receiptId = getUserInput();
        System.out.print("Enter expenditure ID: ");
        String expenditureId = getUserInput();

        if (receiptId != null && expenditureId != null) {
            receiptHandler.linkReceiptToExpenditure(receiptId, expenditureId);
            System.out.println("✓ Receipt linked to expenditure.");
        }
    }

    private void viewReceiptHistory() {
        List<Receipt> history = receiptHandler.getReceiptHistory();
        if (history.isEmpty()) {
            System.out.println("No receipt history found.");
            return;
        }

        System.out.println("\n=== RECEIPT HISTORY ===");
        for (Receipt receipt : history) {
            System.out.println("Receipt ID: " + receipt.getReceiptId() +
                    " | Expense: " + receipt.getExpenseCode() +
                    " | Date: " + receipt.getTimestamp().toLocalDate());
        }
    }

    // ===============================
    // ANALYTICS & REPORTING METHODS
    // ===============================

    private void analyticsMenu() {
        boolean inAnalyticsMenu = true;
        while (inAnalyticsMenu) {
            System.out.println("\n--- ANALYTICS & REPORTS ---");
            System.out.println("1. Monthly Burn Rate");
            System.out.println("2. Cost Analysis Report");
            System.out.println("3. Affordability Insights");
            System.out.println("4. Category Breakdown");
            System.out.println("5. Spending Trends");
            System.out.println("6. Summary Statistics");
            System.out.println("7. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String input = getUserInput();
            if (input == null)
                continue;
            if ("exit".equalsIgnoreCase(input) || "0".equals(input)) {
                inAnalyticsMenu = false;
                continue;
            }
            switch (input) {
                case "1":
                    showMonthlyBurnRate();
                    break;
                case "2":
                    showCostAnalysis();
                    break;
                case "3":
                    showAffordabilityInsights();
                    break;
                case "4":
                    showCategoryBreakdown();
                    break;
                case "5":
                    showSpendingTrends();
                    break;
                case "6":
                    showSummaryStatistics();
                    break;
                case "7":
                    inAnalyticsMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please select 1-7 or 0 to exit.");
            }
        }
    }

    private void showMonthlyBurnRate() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        double monthlyBurn = analyticsModule.calculateMonthlyBurn(expenditures);

        System.out.println("\n=== MONTHLY BURN RATE ===");
        System.out.printf("Average Monthly Spending: ₵%.2f%n", monthlyBurn);
        System.out.printf("Daily Average: ₵%.2f%n", monthlyBurn / 30);
        System.out.printf("Weekly Average: ₵%.2f%n", monthlyBurn / 4);
    }

    private void showCostAnalysis() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        String analysis = analyticsModule.generateCostAnalysis(expenditures);

        System.out.println("\n=== COST ANALYSIS REPORT ===");
        System.out.println(analysis);
    }

    private void showAffordabilityInsights() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        Float totalBalance = bankLedger.getTotalBalance();

        String insights = analyticsModule.generateAffordabilityInsights(expenditures, totalBalance);

        System.out.println("\n=== AFFORDABILITY INSIGHTS ===");
        System.out.println(insights);
    }

    private void showCategoryBreakdown() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        String breakdown = analyticsModule.generateCategoryBreakdown(expenditures);

        System.out.println("\n=== CATEGORY BREAKDOWN ===");
        System.out.println(breakdown);
    }

    private void showSpendingTrends() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        String trends = analyticsModule.generateSpendingTrends(expenditures);

        System.out.println("\n=== SPENDING TRENDS ===");
        System.out.println(trends);
    }

    @SuppressWarnings("deprecation")
    private void showSummaryStatistics() {
        List<Expenditure> expenditures = expenditureManager.getAllExpenditures();
        float totalSpent = expenditureManager.getTotalAmount();
        Float totalBalance = bankLedger.getTotalBalance();

        // Use Unicode for Cedi: \u20B5
        System.out.println("\n=== SUMMARY STATISTICS ===");
        System.out.println("Total Expenditures: " + expenditures.size());
        System.out.println("Total Amount Spent: \u20B5" + totalSpent);
        System.out.println("Total Bank Balance: \u20B5" + totalBalance);
        System.out.println("Number of Categories: " + categoryManager.getCategoryCount());
        System.out.println("Number of Bank Accounts: " + bankLedger.getAllAccounts().size());

        if (!expenditures.isEmpty()) {
            float avgExpenditure = totalSpent / expenditures.size();
            System.out.println("Average Expenditure: \u20B5" + avgExpenditure);
        }
    }

    // ===============================
    // HELP AND UTILITY METHODS
    // ===============================

    private void showDetailedHelpMenu() {
        boolean inHelp = true;
        while (inHelp) {
            System.out.println("\n--- HELP MENU ---");
            System.out.println("1. About Xpense Financial Tracker");
            System.out.println("2. Expenditure Management Help");
            System.out.println("3. Category Management Help");
            System.out.println("4. Bank Account Help");
            System.out.println("5. Receipt Management Help");
            System.out.println("6. Analytics Help");
            System.out.println("7. Data File Information");
            System.out.println("8. System Requirements");
            System.out.println("9. Return to Main Menu");
            System.out.println("0. Exit");
            System.out.print("Select a help option: ");

            String helpInput = getUserInput();
            if ("exit".equalsIgnoreCase(helpInput) || "0".equals(helpInput)) {
                inHelp = false;
                continue;
            }
            switch (helpInput) {
                case "1":
                    showAboutInfo();
                    break;
                case "2":
                    showExpenditureHelp();
                    break;
                case "3":
                    showCategoryHelp();
                    break;
                case "4":
                    showBankAccountHelp();
                    break;
                case "5":
                    showReceiptHelp();
                    break;
                case "6":
                    showAnalyticsHelp();
                    break;
                case "7":
                    showDataFileInfo();
                    break;
                case "8":
                    showSystemRequirements();
                    break;
                case "9":
                    inHelp = false;
                    break;
                default:
                    System.out.println("Invalid help option. Please select 1-9 or 0 to exit.");
            }
        }
    }

    private void showAboutInfo() {
        System.out.println("\n=== ABOUT XPENSE FINANCIAL TRACKER ===");
        System.out.println("Xpense is a comprehensive personal finance management system");
        System.out.println("developed for DCIT308 Data Structures and Algorithms II.");
        System.out.println("");
        System.out.println("Features:");
        System.out.println("• Expenditure tracking and management");
        System.out.println("• Category-based expense organization");
        System.out.println("• Bank account balance monitoring");
        System.out.println("• Receipt management and processing");
        System.out.println("• Financial analytics and reporting");
        System.out.println("• Alert system for low balances and spending limits");
        System.out.println("");
        System.out.println("Developed by Group 68 - University of Ghana");
    }

    private void showExpenditureHelp() {
        System.out.println("\n=== EXPENDITURE MANAGEMENT HELP ===");
        System.out.println("• Add Expenditure: Create new expense entries");
        System.out.println("• View All: Display all recorded expenditures");
        System.out.println("• Search: Find expenses by ID, description, category, or date");
        System.out.println("• Sort: Organize expenditures by date, amount, or category");
        System.out.println("• Edit: Modify existing expenditure details");
        System.out.println("• Delete: Remove expenditures (with confirmation)");
        System.out.println("• Filter: Find expenses within specific amount ranges");
        System.out.println("• Category View: See all expenses in a specific category");
    }

    private void showCategoryHelp() {
        System.out.println("\n=== CATEGORY MANAGEMENT HELP ===");
        System.out.println("• Add Category: Create new expense categories");
        System.out.println("• View All: Display all available categories");
        System.out.println("• Search: Find categories by name or description");
        System.out.println("• Update: Modify existing category information");
        System.out.println("• Delete: Remove categories (if not in use)");
        System.out.println("• Statistics: View spending statistics per category");
        System.out.println("• Validation: Categories must exist before adding expenditures");
    }

    private void showBankAccountHelp() {
        System.out.println("\n=== BANK ACCOUNT HELP ===");
        System.out.println("• View Accounts: Display all bank accounts and balances");
        System.out.println("• Add Account: Create new bank account entries");
        System.out.println("• Account Details: View detailed information for specific accounts");
        System.out.println("• Summary: Overview of all accounts and total balance");
        System.out.println("• Low Balance Alerts: Check for accounts below threshold");
        System.out.println("• Balance Tracking: Automatic updates when expenditures are added");
    }

    private void showReceiptHelp() {
        System.out.println("\n=== RECEIPT MANAGEMENT HELP ===");
        System.out.println("• Upload Receipt: Store receipt information and file paths");
        System.out.println("• View All: Display all uploaded receipts");
        System.out.println("• Process Queue: Review receipts in order (FIFO)");
        System.out.println("• Link to Expenditure: Connect receipts to corresponding expenses");
        System.out.println("• History: View chronological receipt records");
        System.out.println("• File Paths: Can be actual file paths or descriptions");
    }

    private void showAnalyticsHelp() {
        System.out.println("\n=== ANALYTICS & REPORTING HELP ===");
        System.out.println("• Monthly Burn Rate: Calculate average monthly spending");
        System.out.println("• Cost Analysis: Analyze spending patterns and trends");
        System.out.println("• Affordability Insights: Get spending recommendations");
        System.out.println("• Category Breakdown: See spending distribution by category");
        System.out.println("• Spending Trends: Track spending patterns over time");
        System.out.println("• Summary Statistics: Overview of all financial data");
    }

    private void showDataFileInfo() {
        System.out.println("\n=== DATA FILE INFORMATION ===");
        System.out.println("Data is stored in pipe-separated text files:");
        System.out.println("");
        System.out.println("expenditures.txt: code|amount|date|phase|category|accountId");
        System.out.println("categories.txt: name|description|color");
        System.out.println("accounts.txt: accountId|accountName|balance");
        System.out.println("receipts.txt: receiptId|expenseCode|filePath|timestamp");
        System.out.println("");
        System.out.println("Files are automatically saved when changes are made.");
        System.out.println("Backup files are created in the backup/ directory.");
    }

    private void showSystemRequirements() {
        System.out.println("\n=== SYSTEM REQUIREMENTS ===");
        System.out.println("• Java 8 or higher");
        System.out.println("• Command-line interface");
        System.out.println("• Read/write access to data directory");
        System.out.println("• No external dependencies required");
        System.out.println("• Uses only Java standard library");
        System.out.println("");
        System.out.println("Data Structures Used:");
        System.out.println("• ArrayList: Dynamic expenditure storage");
        System.out.println("• HashMap: Fast key-value lookups");
        System.out.println("• TreeMap: Sorted time-based analytics");
        System.out.println("• Stack: Receipt processing (LIFO)");
        System.out.println("• Queue: Receipt workflow (FIFO)");
        System.out.println("• HashSet: Unique category collections");
    }

    private void showAlertsOnLogin() {
        if (alertSystem.hasAlerts()) {
            System.out.println("\n=== SYSTEM ALERTS ===");
            alertSystem.displayAllAlerts();
            saveAlertsToFile();
        }
    }

    private void saveAlertsToFile() {
        try {
            // Save alerts to alerts.txt (append mode)
            java.io.FileWriter writer = new java.io.FileWriter("alerts.txt", true);
            // For simplicity, just write a header and note that alerts were viewed
            writer.write("Alerts viewed at: " + java.time.LocalDateTime.now() + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving alerts: " + e.getMessage());
        }
    }

    private String getUserInput() {
        String input = null;
        try {
            if (scanner.hasNextLine()) {
                input = scanner.nextLine().trim();
                if ("cancel".equalsIgnoreCase(input)) {
                    throw new CancelInputException();
                }
            }
        } catch (CancelInputException e) {
            // Custom exception to handle cancel
            return "cancel";
        } catch (Exception e) {
            return null;
        }
        return input;
    }

    // Custom exception for cancel
    private static class CancelInputException extends RuntimeException {}

    private String truncateString(String str, int maxLength) {
        if (str == null)
            return "";
        if (str.length() <= maxLength)
            return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}