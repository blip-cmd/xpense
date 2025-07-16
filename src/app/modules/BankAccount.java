package app.modules;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a bank account in the expenditure management system.
 */
public class BankAccount {
    // Fields
    private String accountNumber;
    private String accountName;
    private float balance;
    private LocalDate createdDate;
    private ArrayList<Expenditure> expenditures = new ArrayList<>();

    /**
     * Constructor for BankAccount.
     * Initializes account with values and sets balance to 0.
     */
    public BankAccount(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = 0f;
        this.createdDate = LocalDate.now();
    }

    /**
     * Constructor for BankAccount with initial balance.
     */
    public BankAccount(String accountNumber, String accountName, Float initialBalance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = initialBalance != null ? initialBalance : 0f;
        this.createdDate = LocalDate.now();
    }

    // Getter for account balance
    public Float getBalance() {
        return balance;
    }

    // Getter for account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter for account ID (alias for getAccountNumber for compatibility)
    public String getAccountId() {
        return accountNumber;
    }

    // Getter for account name
    public String getAccountName() {
        return accountName;
    }

    // Getter for created date
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public ArrayList<Expenditure> getExpenditures() {
        return expenditures;
    }

    // Returns the current balance as a float (already is)
    public float get_balance() {
        return balance;
    }

    // Adds an expenditure and debits the account
    public void add_expenditure(Expenditure e) {
        if (e != null && e.isValid() && debit(e.getAmount())) {
            expenditures.add(e);
        }
    }

    /**
     * Debit account (subtract amount).
     * Returns false if amount is invalid or insufficient funds.
     */
    public boolean debit(Float amount) {
        if (amount == null || amount <= 0) {
            return false; // Invalid amount
        }
        if (balance < amount) {
            return false; // Not enough balance
        }
        balance -= amount;
        return true;
    }

    /**
     * Credit account (add amount).
     * Returns false if amount is invalid.
     */
    public boolean credit(Float amount) {
        if (amount == null || amount <= 0) {
            return false; // Invalid amount
        }
        balance += amount;
        return true;
    }

    // Setters for all fields
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBalance(Float balance) {
        this.balance = balance != null ? balance : 0f;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public void setExpenditures(ArrayList<Expenditure> expenditures) {
        this.expenditures = expenditures;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", balance=" + balance +
                ", createdDate=" + createdDate +
                ", expenditures=" + expenditures +
                '}';
    }
}
