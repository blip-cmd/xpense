package app.modules;

import app.util.*;
import java.math.BigDecimal;

public class BankLedger {
    private final SimpleMap<String, BankAccount> accounts;
    private final AlertSystem alertSystem;

    public BankLedger(AlertSystem alertSystem) {
        this.accounts = new SimpleMap<>();
        this.alertSystem = alertSystem;
    }

    public boolean addAccount(BankAccount account) {
        if (account == null || account.getAccountNumber() == null) return false;
        if (accounts.containsKey(account.getAccountNumber())) return false;
        accounts.put(account.getAccountNumber(), account);
        return true;
    }

    public BankAccount getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public SimpleArrayList<BankAccount> getAllAccounts() {
        SimpleArrayList<BankAccount> list = new SimpleArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            list.add(accounts.getAt(i));
        }
        return list;
    }

    public boolean logExpenditure(String accountId, BigDecimal amount, String description) {
        BankAccount account = accounts.get(accountId);
        if (account == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        boolean debited = account.debit(amount);
        if (debited && alertSystem != null) {
            alertSystem.checkLowFunds(accountId, account.getBalance().doubleValue());
        }
        return debited;
    }
}