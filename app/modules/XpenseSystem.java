package app.modules;

import app.util.*;

public class XpenseSystem {
    private final FileManager fileManager;
    private final AlertSystem alertSystem;
    private final CategoryManager categoryManager;
    private final BankLedger bankLedger;
    private final ExpenditureManager expenditureManager;
    private final ReceiptHandler receiptHandler;
    private final AnalyticsModule analyticsModule;
    private final SearchAndSortModule searchSortModule;

    public XpenseSystem(double lowBalanceThreshold, double spendingLimitThreshold) {
        this.fileManager = new FileManager();
        this.alertSystem = new AlertSystem(lowBalanceThreshold, spendingLimitThreshold);
        this.categoryManager = new CategoryManager();
        this.bankLedger = new BankLedger(alertSystem);
        this.expenditureManager = new ExpenditureManager();
        this.receiptHandler = new ReceiptHandler();
        this.analyticsModule = new AnalyticsModule();
        this.searchSortModule = new SearchAndSortModule();
        loadAllData();
    }

    private void loadAllData() {
        SimpleArrayList<Category> categories = fileManager.loadCategories("categories.txt");
        for (int i = 0; i < categories.size(); i++) categoryManager.addCategory(categories.get(i));

        SimpleArrayList<BankAccount> accounts = fileManager.loadAccounts("accounts.txt");
        for (int i = 0; i < accounts.size(); i++) bankLedger.addAccount(accounts.get(i));

        SimpleArrayList<Expenditure> expenditures = fileManager.loadExpenditures("expenditures.txt");
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (exp.getBankAccountId() != null &&
                bankLedger.getAccount(exp.getBankAccountId()) != null &&
                categoryManager.validateCategory(exp.getCategory().getName())) 
            {
                expenditureManager.addExpenditure(exp);
                categoryManager.addExpenditureToCategory(exp.getCategory().getName(), exp);
                bankLedger.getAccount(exp.getBankAccountId()).add_expenditure(exp);
            }
        }

        SimpleArrayList<Receipt> receipts = fileManager.loadReceipts("receipts.txt");
        for (int i = 0; i < receipts.size(); i++) receiptHandler.addReceipt(receipts.get(i));
    }

    public boolean addExpenditure(Expenditure exp) {
        if (exp.getBankAccountId() == null || bankLedger.getAccount(exp.getBankAccountId()) == null) {
            alertSystem.addAlert("Cannot add expenditure: Bank account does not exist.", 1);
            return false;
        }
        if (!categoryManager.validateCategory(exp.getCategory().getName())) {
            alertSystem.addAlert("Cannot add expenditure: Category does not exist.", 2);
            return false;
        }
        BankAccount bank = bankLedger.getAccount(exp.getBankAccountId());
        if (!bank.debit(exp.getAmount())) {
            alertSystem.addAlert("Insufficient funds in account " + bank.getAccountNumber(), 1);
            return false;
        }
        boolean added = expenditureManager.addExpenditure(exp);
        if (added) {
            categoryManager.addExpenditureToCategory(exp.getCategory().getName(), exp);
            bank.add_expenditure(exp);
            bankLedger.logExpenditure(bank.getAccountNumber(), exp.getAmount(), exp.getDescription());
            fileManager.saveExpenditures(expenditureManager.getAllExpenditures(), "expenditures.txt");
            fileManager.saveAccounts(bankLedger.getAllAccounts(), "accounts.txt");
            return true;
        } else {
            bank.credit(exp.getAmount());
            alertSystem.addAlert("Expenditure not added due to duplicate ID or invalid data.", 2);
            return false;
        }
    }

    public boolean addBankAccount(BankAccount acct) {
        boolean added = bankLedger.addAccount(acct);
        if (added) fileManager.saveAccounts(bankLedger.getAllAccounts(), "accounts.txt");
        return added;
    }

    public boolean addCategory(Category cat) {
        boolean added = categoryManager.addCategory(cat);
        if (added) fileManager.saveCategories(categoryManager.getAllCategories(), "categories.txt");
        return added;
    }

    public void saveAll() {
        fileManager.saveExpenditures(expenditureManager.getAllExpenditures(), "expenditures.txt");
        fileManager.saveAccounts(bankLedger.getAllAccounts(), "accounts.txt");
        fileManager.saveCategories(categoryManager.getAllCategories(), "categories.txt");
    }

    public SimpleArrayList<Expenditure> getAllExpenditures() { return expenditureManager.getAllExpenditures(); }
    public SimpleArrayList<BankAccount> getAllBankAccounts() { return bankLedger.getAllAccounts(); }
    public SimpleArrayList<Category> getAllCategories() { return categoryManager.getAllCategories(); }
    public AlertSystem getAlertSystem() { return alertSystem; }
    public CategoryManager getCategoryManager() { return categoryManager; }
    public BankLedger getBankLedger() { return bankLedger; }
    public ExpenditureManager getExpenditureManager() { return expenditureManager; }
    public AnalyticsModule getAnalyticsModule() { return analyticsModule; }
    public ReceiptHandler getReceiptHandler() { return receiptHandler; }
    public SearchAndSortModule getSearchSortModule() { return searchSortModule; }
}