/**
 * XpenseSystem.java
 * 
 * Core system class for the Nkwa Real Estate Expenditure Management System.
 * This class orchestrates all subsystems including file management, alerts,
 * categories, bank accounts, expenditures, receipts, analytics, and search functionality.
 * 
 * The system manages the complete lifecycle of expenditure tracking from data loading
 * to persistence, ensuring data integrity and providing comprehensive business logic
 * for expense management operations.
 * 
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app.modules;

import app.util.*;

/**
 * XpenseSystem is the central orchestrator for the expenditure management application.
 * 
 * This class integrates all major subsystems:
 * - FileManager: Handles data persistence to/from text files
 * - AlertSystem: Manages low balance and overspending alerts
 * - CategoryManager: Manages expense categories and validation
 * - BankLedger: Handles bank account operations and tracking
 * - ExpenditureManager: Manages expenditure lifecycle and validation
 * - ReceiptHandler: Handles receipt storage and management
 * - AnalyticsModule: Provides spending analysis and reporting
 * - SearchAndSortModule: Enables data searching and sorting capabilities
 * 
 * The system ensures atomicity in operations - expenditures are only added if
 * all validations pass and account balances can be properly debited.
 */
public class XpenseSystem {
    /** Handles all file I/O operations for data persistence */
    private final FileManager fileManager;
    
    /** Manages alerts for low balances and overspending */
    private final AlertSystem alertSystem;
    
    /** Manages expense categories and category-related operations */
    private final CategoryManager categoryManager;
    
    /** Handles bank account operations and ledger maintenance */
    private final BankLedger bankLedger;
    
    /** Manages expenditure lifecycle and validation */
    private final ExpenditureManager expenditureManager;
    
    /** Handles receipt storage and management */
    private final ReceiptHandler receiptHandler;
    
    /** Provides analytics and reporting functionality */
    private final AnalyticsModule analyticsModule;
    
    /** Enables search and sorting operations on data */
    private final SearchAndSortModule searchSortModule;

    /**
     * Constructs a new XpenseSystem with specified alert thresholds.
     * 
     * Initializes all subsystems and loads existing data from persistence files.
     * The constructor performs the following operations:
     * 1. Initializes all manager and handler components
     * 2. Loads categories, bank accounts, and expenditures from files
     * 3. Resolves category references for loaded expenditures
     * 4. Associates expenditures with their respective accounts and categories
     * 5. Loads receipts and links them to expenditures
     * 
     * @param lowBalanceThreshold The balance threshold below which alerts are triggered
     * @param spendingLimitThreshold The spending amount above which alerts are triggered
     */
    public XpenseSystem(double lowBalanceThreshold, double spendingLimitThreshold) {
        // Initialize all core components
        this.fileManager = new FileManager();
        this.alertSystem = new AlertSystem(lowBalanceThreshold, spendingLimitThreshold);
        this.categoryManager = new CategoryManager();
        this.bankLedger = new BankLedger(alertSystem);
        this.expenditureManager = new ExpenditureManager();
        this.receiptHandler = new ReceiptHandler();
        this.analyticsModule = new AnalyticsModule();
        this.searchSortModule = new SearchAndSortModule();
        
        // Load all existing data from persistence files
        loadAllData();
    }

    /**
     * Loads all data from persistence files and establishes proper relationships.
     * 
     * This method performs a multi-stage loading process:
     * 1. Loads categories and adds them to the category manager
     * 2. Loads bank accounts and adds them to the bank ledger
     * 3. Loads expenditures and resolves their category references
     * 4. Associates expenditures with their bank accounts and categories
     * 5. Loads receipts and adds them to the receipt handler
     * 
     * The loading process ensures data integrity by validating that expenditures
     * reference valid bank accounts and categories before associating them.
     */
    private void loadAllData() {
        // Load categories from file and register them with the category manager
        SimpleArrayList<Category> categories = fileManager.loadCategories("categories.txt");
        for (int i = 0; i < categories.size(); i++) categoryManager.addCategory(categories.get(i));

        // Load bank accounts from file and register them with the bank ledger
        SimpleArrayList<BankAccount> accounts = fileManager.loadAccounts("accounts.txt");
        for (int i = 0; i < accounts.size(); i++) bankLedger.addAccount(accounts.get(i));

        // Load expenditures and resolve their category references
        SimpleArrayList<Expenditure> expenditures = fileManager.loadExpenditures("expenditures.txt");
        
        // Resolve category references for loaded expenditures
        // This ensures that expenditures reference actual category objects rather than temporary ones
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            // Find the actual category object by name from the loaded categories
            String categoryName = exp.getCategory().getName();
            Category actualCategory = null;
            SimpleArrayList<Category> allCategories = categoryManager.getAllCategories();
            for (int j = 0; j < allCategories.size(); j++) {
                if (allCategories.get(j).getName().equalsIgnoreCase(categoryName)) {
                    actualCategory = allCategories.get(j);
                    break;
                }
            }
            // Update expenditure to reference the actual category object
            if (actualCategory != null) {
                exp.setCategory(actualCategory);
            }
        }
        
        // Load expenditures into the expenditure manager (properly initializes ID counter)
        expenditureManager.loadExpenditures(expenditures);
        
        // Associate expenditures with their bank accounts and categories
        // Only process expenditures that have valid bank accounts and categories
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (exp.getBankAccountId() != null &&
                bankLedger.getAccount(exp.getBankAccountId()) != null &&
                categoryManager.validateCategory(exp.getCategory().getName())) 
            {
                // Add expenditure to category for category-based reporting
                categoryManager.addExpenditureToCategory(exp.getCategory().getName(), exp);
                // Add expenditure to bank account for account-based tracking
                bankLedger.getAccount(exp.getBankAccountId()).add_expenditure(exp);
            }
        }

        // Load receipts from file and register them with the receipt handler
        SimpleArrayList<Receipt> receipts = fileManager.loadReceipts("receipts.txt");
        for (int i = 0; i < receipts.size(); i++) receiptHandler.addReceipt(receipts.get(i));
    }

    /**
     * Adds a new expenditure to the system with full validation and atomicity.
     * 
     * This method performs comprehensive validation and atomic operations:
     * 1. Validates that the specified bank account exists
     * 2. Validates that the specified category exists
     * 3. Attempts to debit the bank account for the expenditure amount
     * 4. Adds the expenditure to the expenditure manager
     * 5. Associates the expenditure with its category and bank account
     * 6. Logs the transaction in the bank ledger
     * 7. Persists changes to files
     * 
     * If any step fails, the operation is rolled back to maintain data integrity.
     * 
     * @param exp The expenditure to add to the system
     * @return true if the expenditure was successfully added, false otherwise
     */
    public boolean addExpenditure(Expenditure exp) {
        // Validate that the bank account exists
        if (exp.getBankAccountId() == null || bankLedger.getAccount(exp.getBankAccountId()) == null) {
            alertSystem.addAlert("Cannot add expenditure: Bank account does not exist.", 1);
            return false;
        }
        
        // Validate that the category exists
        if (!categoryManager.validateCategory(exp.getCategory().getName())) {
            alertSystem.addAlert("Cannot add expenditure: Category does not exist.", 2);
            return false;
        }
        
        // Get the bank account and attempt to debit the expenditure amount
        BankAccount bank = bankLedger.getAccount(exp.getBankAccountId());
        if (!bank.debit(exp.getAmount())) {
            alertSystem.addAlert("Insufficient funds in account " + bank.getAccountNumber(), 1);
            return false;
        }
        
        // Attempt to add the expenditure to the expenditure manager
        boolean added = expenditureManager.addExpenditure(exp);
        if (added) {
            // Successfully added - complete all associations and persist changes
            categoryManager.addExpenditureToCategory(exp.getCategory().getName(), exp);
            bank.add_expenditure(exp);
            bankLedger.logExpenditure(bank.getAccountNumber(), exp.getAmount(), exp.getDescription());
            
            // Persist all changes to files
            fileManager.saveExpenditures(expenditureManager.getAllExpenditures(), "expenditures.txt");
            fileManager.saveAccounts(bankLedger.getAllAccounts(), "accounts.txt");
            return true;
        } else {
            // Failed to add expenditure - rollback the debit operation
            bank.credit(exp.getAmount());
            alertSystem.addAlert("Expenditure not added due to duplicate ID or invalid data.", 2);
            return false;
        }
    }

    /**
     * Adds a new bank account to the system.
     * 
     * If the account is successfully added, the changes are immediately
     * persisted to the accounts.txt file.
     * 
     * @param acct The bank account to add
     * @return true if the account was successfully added, false if it already exists
     */
    public boolean addBankAccount(BankAccount acct) {
        boolean added = bankLedger.addAccount(acct);
        if (added) fileManager.saveAccounts(bankLedger.getAllAccounts(), "accounts.txt");
        return added;
    }

    /**
     * Adds a new category to the system.
     * 
     * If the category is successfully added, the changes are immediately
     * persisted to the categories.txt file.
     * 
     * @param cat The category to add
     * @return true if the category was successfully added, false if it already exists
     */
    public boolean addCategory(Category cat) {
        boolean added = categoryManager.addCategory(cat);
        if (added) fileManager.saveCategories(categoryManager.getAllCategories(), "categories.txt");
        return added;
    }

    /**
     * Persists all current data to their respective files.
     * 
     * This method saves:
     * - All expenditures to expenditures.txt
     * - All bank accounts to accounts.txt  
     * - All categories to categories.txt
     * 
     * This is typically called when the application shuts down to ensure
     * no data is lost.
     */
    public void saveAll() {
        fileManager.saveExpenditures(expenditureManager.getAllExpenditures(), "expenditures.txt");
        fileManager.saveAccounts(bankLedger.getAllAccounts(), "accounts.txt");
        fileManager.saveCategories(categoryManager.getAllCategories(), "categories.txt");
    }

    // Getter methods for accessing subsystem data and functionality
    
    /** @return All expenditures in the system */
    public SimpleArrayList<Expenditure> getAllExpenditures() { return expenditureManager.getAllExpenditures(); }
    
    /** @return All bank accounts in the system */
    public SimpleArrayList<BankAccount> getAllBankAccounts() { return bankLedger.getAllAccounts(); }
    
    /** @return All categories in the system */
    public SimpleArrayList<Category> getAllCategories() { return categoryManager.getAllCategories(); }
    
    /** @return The alert system for managing notifications */
    public AlertSystem getAlertSystem() { return alertSystem; }
    
    /** @return The category manager for category operations */
    public CategoryManager getCategoryManager() { return categoryManager; }
    
    /** @return The bank ledger for account operations */
    public BankLedger getBankLedger() { return bankLedger; }
    
    /** @return The expenditure manager for expenditure operations */
    public ExpenditureManager getExpenditureManager() { return expenditureManager; }
    
    /** @return The analytics module for reporting and analysis */
    public AnalyticsModule getAnalyticsModule() { return analyticsModule; }
    
    /** @return The receipt handler for receipt management */
    public ReceiptHandler getReceiptHandler() { return receiptHandler; }
    
    /** @return The search and sort module for data querying */
    public SearchAndSortModule getSearchSortModule() { return searchSortModule; }
}