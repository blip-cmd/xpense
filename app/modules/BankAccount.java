package app.modules;

import java.math.BigDecimal;
import java.time.LocalDate;
import app.util.SimpleArrayList;

public class BankAccount {
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
    private LocalDate createdDate;
    private final SimpleArrayList<Expenditure> expenditures = new SimpleArrayList<>();

    public BankAccount(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = BigDecimal.ZERO;
        this.createdDate = LocalDate.now();
    }

    public BankAccount(String accountNumber, String accountName, BigDecimal initialBalance) {
        this(accountNumber, accountName);
        if (initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = initialBalance;
        }
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountName() { return accountName; }
    public BigDecimal getBalance() { return balance; }
    public LocalDate getCreatedDate() { return createdDate; }
    public SimpleArrayList<Expenditure> getExpenditures() { return expenditures; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public boolean debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (balance.compareTo(amount) < 0) return false;
        balance = balance.subtract(amount);
        return true;
    }

    public boolean credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        balance = balance.add(amount);
        return true;
    }

    public void add_expenditure(Expenditure e) {
        if (e != null && e.isValid()) expenditures.add(e);
    }

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