package app.modules;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages bank accounts and transaction logging
 * Implements efficient account management using HashMap and transaction history tracking
 */
public class BankLedger {
    private HashMap<String, BankAccount> accounts;
    private List<Transaction> transactionHistory;
    private HashMap<String, List<Transaction>> accountTransactions;
    private FileManager fileManager;
    private AlertSystem alertSystem;
    
    /**
     * Inner class to represent a transaction
     */
    private static class Transaction {
        private String accountId;
        private BigDecimal amount;
        private String type; // "DEBIT" or "CREDIT"
        private String description;
        private LocalDateTime timestamp;
        
        public Transaction(String accountId, BigDecimal amount, String type, String description) {
            this.accountId = accountId;
            this.amount = amount;
            this.type = type;
            this.description = description;
            this.timestamp = LocalDateTime.now();
        }
        
        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("[%s] %s %s: ₵%.2f - %s", 
                timestamp.format(formatter), accountId, type, amount.doubleValue(), description);
        }
        
        // Getters
        public String getAccountId() { return accountId; }
        public BigDecimal getAmount() { return amount; }
        public String getType() { return type; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    /**
     * Constructor for BankLedger
     * Initializes data structures and loads existing accounts
     */
    public BankLedger() {
        this.accounts = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
        this.accountTransactions = new HashMap<>();
        this.fileManager = new FileManager();
        this.alertSystem = null; // Will be set externally if needed
        loadAccounts();
    }
    
    /**
     * Constructor with AlertSystem for low balance notifications
     */
    public BankLedger(AlertSystem alertSystem) {
        this();
        this.alertSystem = alertSystem;
    }
    
    /**
     * Log an expenditure to the appropriate account
     * Debits the account and records the transaction
     * @param accountId the account ID
     * @param amount the expenditure amount
     * @return true if successful, false otherwise
     */
    public boolean logExpenditure(String accountId, double amount) {
        return logExpenditure(accountId, BigDecimal.valueOf(amount), "Expenditure");
    }
    
    /**
     * Log an expenditure with description
     * @param accountId the account ID
     * @param amount the expenditure amount
     * @param description the transaction description
     * @return true if successful, false otherwise
     */
    public boolean logExpenditure(String accountId, BigDecimal amount, String description) {
        if (accountId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            return false;
        }
        
        // Try to debit the account
        if (!account.debit(amount)) {
            return false; // Insufficient funds or invalid amount
        }
        
        // Record the transaction
        Transaction transaction = new Transaction(accountId, amount, "DEBIT", description);
        transactionHistory.add(transaction);
        
        // Add to account-specific transaction list
        accountTransactions.computeIfAbsent(accountId, k -> new ArrayList<>()).add(transaction);
        
        // Check for low balance and create alert if needed
        if (alertSystem != null) {
            alertSystem.checkLowFunds(accountId, account.getBalance().doubleValue());
        }
        
        // Save accounts to file
        saveAccounts();
        return true;
    }
    
    /**
     * Credit an account (add money)
     * @param accountId the account ID
     * @param amount the amount to credit
     * @param description the transaction description
     * @return true if successful, false otherwise
     */
    public boolean creditAccount(String accountId, BigDecimal amount, String description) {
        if (accountId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            return false;
        }
        
        // Credit the account
        if (!account.credit(amount)) {
            return false;
        }
        
        // Record the transaction
        Transaction transaction = new Transaction(accountId, amount, "CREDIT", description);
        transactionHistory.add(transaction);
        
        // Add to account-specific transaction list
        accountTransactions.computeIfAbsent(accountId, k -> new ArrayList<>()).add(transaction);
        
        // Save accounts to file
        saveAccounts();
        return true;
    }
    
    /**
     * Get account balance
     * @param accountId the account ID
     * @return account balance or -1 if not found
     */
    public double getBalance(String accountId) {
        BankAccount account = accounts.get(accountId);
        return account != null ? account.getBalance().doubleValue() : -1.0;
    }
    
    /**
     * Get account balance as BigDecimal
     * @param accountId the account ID
     * @return account balance or null if not found
     */
    public BigDecimal getBalanceAsBigDecimal(String accountId) {
        BankAccount account = accounts.get(accountId);
        return account != null ? account.getBalance() : null;
    }
    
    /**
     * Add a new bank account
     * @param account the bank account to add
     * @return true if successful, false otherwise
     */
    public boolean addAccount(BankAccount account) {
        if (account == null || account.getAccountNumber() == null) {
            return false;
        }
        
        // Check if account already exists
        if (accounts.containsKey(account.getAccountNumber())) {
            return false;
        }
        
        accounts.put(account.getAccountNumber(), account);
        accountTransactions.put(account.getAccountNumber(), new ArrayList<>());
        saveAccounts();
        return true;
    }
    
    /**
     * Create a new bank account
     * @param accountNumber the account number
     * @param accountName the account name
     * @param initialBalance the initial balance
     * @return true if successful, false otherwise
     */
    public boolean createAccount(String accountNumber, String accountName, BigDecimal initialBalance) {
        if (accountNumber == null || accountName == null || 
            accountNumber.trim().isEmpty() || accountName.trim().isEmpty()) {
            return false;
        }
        
        BankAccount account = new BankAccount(accountNumber, accountName);
        
        // Set initial balance if provided
        if (initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            account.credit(initialBalance);
            
            // Record initial deposit transaction
            Transaction transaction = new Transaction(accountNumber, initialBalance, "CREDIT", "Initial deposit");
            transactionHistory.add(transaction);
            accountTransactions.put(accountNumber, new ArrayList<>());
            accountTransactions.get(accountNumber).add(transaction);
        }
        
        return addAccount(account);
    }
    
    /**
     * Get all accounts
     * @return list of all bank accounts
     */
    public List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
    
    /**
     * Get account by ID
     * @param accountId the account ID
     * @return bank account or null if not found
     */
    public BankAccount getAccount(String accountId) {
        return accounts.get(accountId);
    }
    
    /**
     * Get transaction history for an account
     * @param accountId the account ID
     * @return list of transaction strings
     */
    public List<String> getTransactionHistory(String accountId) {
        List<String> history = new ArrayList<>();
        List<Transaction> transactions = accountTransactions.get(accountId);
        
        if (transactions != null) {
            for (Transaction t : transactions) {
                history.add(t.toString());
            }
        }
        
        return history;
    }
    
    /**
     * Get all transaction history
     * @return list of all transaction strings
     */
    public List<String> getAllTransactionHistory() {
        List<String> history = new ArrayList<>();
        for (Transaction t : transactionHistory) {
            history.add(t.toString());
        }
        return history;
    }
    
    /**
     * Get account summary with balance and transaction count
     * @param accountId the account ID
     * @return formatted summary string
     */
    public String getAccountSummary(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            return "Account not found: " + accountId;
        }
        
        List<Transaction> transactions = accountTransactions.get(accountId);
        int transactionCount = transactions != null ? transactions.size() : 0;
        
        return String.format("Account: %s (%s)\nBalance: ₵%.2f\nTransactions: %d", 
            account.getAccountName(), accountId, account.getBalance().doubleValue(), transactionCount);
    }
    
    /**
     * Get total balance across all accounts
     * @return total balance
     */
    public BigDecimal getTotalBalance() {
        BigDecimal total = BigDecimal.ZERO;
        for (BankAccount account : accounts.values()) {
            total = total.add(account.getBalance());
        }
        return total;
    }
    
    /**
     * Check if account has sufficient funds
     * @param accountId the account ID
     * @param amount the amount to check
     * @return true if sufficient funds, false otherwise
     */
    public boolean hasSufficientFunds(String accountId, BigDecimal amount) {
        BankAccount account = accounts.get(accountId);
        return account != null && account.getBalance().compareTo(amount) >= 0;
    }
    
    /**
     * Set alert system for low balance notifications
     * @param alertSystem the alert system
     */
    public void setAlertSystem(AlertSystem alertSystem) {
        this.alertSystem = alertSystem;
    }
    
    /**
     * Load accounts from file
     */
    private void loadAccounts() {
        List<BankAccount> loaded = fileManager.loadAccounts("accounts.txt");
        if (loaded != null) {
            for (BankAccount account : loaded) {
                accounts.put(account.getAccountNumber(), account);
                accountTransactions.put(account.getAccountNumber(), new ArrayList<>());
            }
        }
    }
    
    /**
     * Save accounts to file
     */
    private void saveAccounts() {
        List<BankAccount> accountList = new ArrayList<>(accounts.values());
        fileManager.saveAccounts(accountList, "accounts.txt");
    }
}