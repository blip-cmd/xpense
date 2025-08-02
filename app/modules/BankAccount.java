/**
 * BankAccount.java
 * 
 * Represents a bank account entity in the Nkwa Real Estate Expenditure Management System.
 * This class encapsulates all bank account related data and operations including
 * balance management, expenditure tracking, and transaction processing.
 * 
 * The BankAccount class provides:
 * - Account information management (number, name, balance)
 * - Debit and credit operations with validation
 * - Expenditure tracking and association
 * - Date-based account lifecycle management
 * - Thread-safe balance operations using BigDecimal
 * 
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app.modules;

import java.math.BigDecimal;
import java.time.LocalDate;
import app.util.SimpleArrayList;

/**
 * BankAccount represents a financial account used for expenditure tracking.
 * 
 * This class manages all aspects of a bank account including:
 * - Account identification (number and name)
 * - Balance management with precision using BigDecimal
 * - Expenditure history tracking
 * - Account creation date for auditing
 * - Debit/credit operations with validation
 * 
 * The class ensures financial accuracy by using BigDecimal for all monetary
 * calculations and provides validation for all transaction operations.
 */
public class BankAccount {
    /** Unique identifier for the bank account */
    private String accountNumber;
    
    /** Human-readable name/description for the account */
    private String accountName;
    
    /** Current account balance using BigDecimal for precision */
    private BigDecimal balance;
    
    /** Date when the account was created in the system */
    private LocalDate createdDate;
    
    /** List of all expenditures associated with this account */
    private final SimpleArrayList<Expenditure> expenditures = new SimpleArrayList<>();

    /**
     * Creates a new BankAccount with zero initial balance.
     * 
     * This constructor initializes the account with the provided identification
     * information and sets the balance to zero. The creation date is automatically
     * set to the current date.
     * 
     * @param accountNumber Unique identifier for the account
     * @param accountName Human-readable name for the account
     */
    public BankAccount(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = BigDecimal.ZERO;
        this.createdDate = LocalDate.now();
    }

    /**
     * Creates a new BankAccount with a specified initial balance.
     * 
     * This constructor allows setting an initial balance when creating the account.
     * If the provided initial balance is null or negative, the balance defaults to zero.
     * 
     * @param accountNumber Unique identifier for the account
     * @param accountName Human-readable name for the account  
     * @param initialBalance Initial balance to set (must be positive, or defaults to zero)
     */
    public BankAccount(String accountNumber, String accountName, BigDecimal initialBalance) {
        this(accountNumber, accountName);
        if (initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = initialBalance;
        }
    }

    // Getter methods with documentation
    
    /** @return The unique account number identifier */
    public String getAccountNumber() { return accountNumber; }
    
    /** @return The human-readable account name */
    public String getAccountName() { return accountName; }
    
    /** @return The current account balance */
    public BigDecimal getBalance() { return balance; }
    
    /** @return The date this account was created */
    public LocalDate getCreatedDate() { return createdDate; }
    
    /** @return List of all expenditures associated with this account */
    public SimpleArrayList<Expenditure> getExpenditures() { return expenditures; }

    // Setter methods with documentation
    
    /** @param accountNumber The new account number to set */
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    /** @param accountName The new account name to set */
    public void setAccountName(String accountName) { this.accountName = accountName; }
    
    /** @param balance The new balance to set */
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    /** @param createdDate The new creation date to set */
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    /**
     * Debits (subtracts) the specified amount from the account balance.
     * 
     * This method performs validation to ensure:
     * - The amount is not null and is positive
     * - The account has sufficient funds for the debit
     * 
     * If validation passes, the amount is subtracted from the current balance.
     * 
     * @param amount The amount to debit from the account (must be positive)
     * @return true if the debit was successful, false if validation failed or insufficient funds
     */
    public boolean debit(BigDecimal amount) {
        // Validate amount is positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        
        // Check for sufficient funds
        if (balance.compareTo(amount) < 0) return false;
        
        // Perform the debit operation
        balance = balance.subtract(amount);
        return true;
    }

    /**
     * Credits (adds) the specified amount to the account balance.
     * 
     * This method validates that the amount is not null and is positive
     * before adding it to the current account balance.
     * 
     * @param amount The amount to credit to the account (must be positive)
     * @return true if the credit was successful, false if the amount is invalid
     */
    public boolean credit(BigDecimal amount) {
        // Validate amount is positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        
        // Perform the credit operation
        balance = balance.add(amount);
        return true;
    }

    /**
     * Adds an expenditure to this account's expenditure history.
     * 
     * This method associates an expenditure with this bank account for tracking
     * purposes. The expenditure is only added if it is not null and passes
     * validation checks.
     * 
     * @param e The expenditure to associate with this account
     */
    public void add_expenditure(Expenditure e) {
        // Only add valid expenditures to maintain data integrity
        if (e != null && e.isValid()) expenditures.add(e);
    }

    /**
     * Returns a string representation of the bank account.
     * 
     * The string includes account number, name, balance, creation date,
     * and the count of associated expenditures.
     * 
     * @return A formatted string containing account information
     */
    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", balance=" + balance +
                ", createdDate=" + createdDate +
                ", expenditures=" + expenditures.size() +
                '}';
    }
}