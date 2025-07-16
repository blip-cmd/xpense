package app.modules;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
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
        private Float amount;
        private String type; // "DEBIT" or "CREDIT"
        private String description;
        private LocalDateTime timestamp;

        public Transaction(String accountId, Float amount, String type, String description) {
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
                    timestamp.format(formatter), accountId, type, amount, description);
        }

        public String getAccountId() { return accountId; }
        public Float getAmount() { return amount; }
        public String getType() { return type; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    public BankLedger() {
        this.accounts = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
        this.accountTransactions = new HashMap<>();
        this.fileManager = new FileManager();
        this.alertSystem = null;
        loadAccounts();
    }

    public BankLedger(AlertSystem alertSystem) {
        this();
        this.alertSystem = alertSystem;
    }

    public boolean logExpenditure(String accountId, double amount) {
        return logExpenditure(accountId, (float) amount, "Expenditure");
    }

    public boolean logExpenditure(String accountId, Float amount, String description) {
        if (accountId == null || amount == null || amount <= 0f) {
            return false;
        }

        BankAccount account = accounts.get(accountId);
        if (account == null) {
            return false;
        }

        if (!account.debit(amount)) {
            return false;
        }

        Transaction transaction = new Transaction(accountId, amount, "DEBIT", description);
        transactionHistory.add(transaction);
        accountTransactions.computeIfAbsent(accountId, k -> new ArrayList<>()).add(transaction);

        if (alertSystem != null) {
            alertSystem.checkLowFunds(accountId, account.getBalance());
        }

        saveAccounts();
        return true;
    }

    public boolean creditAccount(String accountId, Float amount, String description) {
        if (accountId == null || amount == null || amount <= 0f) {
            return false;
        }

        BankAccount account = accounts.get(accountId);
        if (account == null) {
            return false;
        }

        if (!account.credit(amount)) {
            return false;
        }

        Transaction transaction = new Transaction(accountId, amount, "CREDIT", description);
        transactionHistory.add(transaction);
        accountTransactions.computeIfAbsent(accountId, k -> new ArrayList<>()).add(transaction);

        saveAccounts();
        return true;
    }

    public float getBalance(String accountId) {
        BankAccount account = accounts.get(accountId);
        return account != null ? account.getBalance() : -1f;
    }

    public Float getBalanceAsFloat(String accountId) {
        BankAccount account = accounts.get(accountId);
        return account != null ? account.getBalance() : null;
    }

    public boolean addAccount(BankAccount account) {
        if (account == null || account.getAccountNumber() == null) {
            return false;
        }

        if (accounts.containsKey(account.getAccountNumber())) {
            return false;
        }

        accounts.put(account.getAccountNumber(), account);
        accountTransactions.put(account.getAccountNumber(), new ArrayList<>());
        saveAccounts();
        return true;
    }

    public boolean createAccount(String accountNumber, String accountName, Float initialBalance) {
        if (accountNumber == null || accountName == null ||
                accountNumber.trim().isEmpty() || accountName.trim().isEmpty()) {
            return false;
        }

        BankAccount account = new BankAccount(accountNumber, accountName);

        if (initialBalance != null && initialBalance > 0f) {
            account.credit(initialBalance);

            Transaction transaction = new Transaction(accountNumber, initialBalance, "CREDIT", "Initial deposit");
            transactionHistory.add(transaction);
            accountTransactions.put(accountNumber, new ArrayList<>());
            accountTransactions.get(accountNumber).add(transaction);
        }

        return addAccount(account);
    }

    public List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public BankAccount getAccount(String accountId) {
        return accounts.get(accountId);
    }

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

    public List<String> getAllTransactionHistory() {
        List<String> history = new ArrayList<>();
        for (Transaction t : transactionHistory) {
            history.add(t.toString());
        }
        return history;
    }

    public String getAccountSummary(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            return "Account not found: " + accountId;
        }

        List<Transaction> transactions = accountTransactions.get(accountId);
        int transactionCount = transactions != null ? transactions.size() : 0;

        return String.format("Account: %s (%s)\nBalance: ₵%.2f\nTransactions: %d",
                account.getAccountName(), accountId, account.getBalance(), transactionCount);
    }

    public float getTotalBalance() {
        float total = 0f;
        for (BankAccount account : accounts.values()) {
            total += account.getBalance();
        }
        return total;
    }

    public boolean hasSufficientFunds(String accountId, Float amount) {
        BankAccount account = accounts.get(accountId);
        return account != null && account.getBalance() >= amount;
    }

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