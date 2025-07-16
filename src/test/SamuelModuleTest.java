package test;

import app.modules.*;
import app.modules.BankLedger;
import app.modules.ExpenditureManager;
import app.modules.Category;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Test class for Samuel Akpah's modules:
 * - ExpenditureManager
 * - BankLedger
 */
public class SamuelModuleTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Samuel's Modules ===\n");
        
        testBankLedger();
        testExpenditureManager();
        
        System.out.println("\n=== All Tests Completed ===");
    }
    
    /**
     * Test BankLedger functionality
     */
    public static void testBankLedger() {
        System.out.println("--- Testing BankLedger ---");
        
        // Create BankLedger
        BankLedger ledger = new BankLedger();
        
        // Test 1: Create accounts
        System.out.println("Test 1: Creating accounts...");
        boolean result1 = ledger.createAccount("ACC001", "Main Checking", new Float("1000.00"));
        boolean result2 = ledger.createAccount("ACC002", "Savings Account", new Float("5000.00"));
        System.out.println("Account creation results: " + result1 + ", " + result2);
        
        // Test 2: Check balances
        System.out.println("\nTest 2: Checking balances...");
        float balance1 = ledger.getBalance("ACC001");
        float balance2 = ledger.getBalance("ACC002");
        System.out.println("ACC001 balance: ₵" + balance1);
        System.out.println("ACC002 balance: ₵" + balance2);
        
        // Test 3: Log expenditures
        System.out.println("\nTest 3: Logging expenditures...");
        boolean exp1 = ledger.logExpenditure("ACC001", new Float("50.00"), "Lunch");
        boolean exp2 = ledger.logExpenditure("ACC001", new Float("25.75"), "Bus fare");
        System.out.println("Expenditure logging results: " + exp1 + ", " + exp2);
        
        // Test 4: Check updated balances
        System.out.println("\nTest 4: Updated balances...");
        System.out.println("ACC001 balance after expenses: ₵" + ledger.getBalance("ACC001"));
        
        // Test 5: Get transaction history
        System.out.println("\nTest 5: Transaction history for ACC001:");
        List<String> history = ledger.getTransactionHistory("ACC001");
        for (String transaction : history) {
            System.out.println("  " + transaction);
        }
        
        // Test 6: Account summary
        System.out.println("\nTest 6: Account summary:");
        System.out.println(ledger.getAccountSummary("ACC001"));
        
        System.out.println("BankLedger tests completed.\n");
    }
    
    /**
     * Test ExpenditureManager functionality
     */
    public static void testExpenditureManager() {
        System.out.println("--- Testing ExpenditureManager ---");
        
        // Create ExpenditureManager
        ExpenditureManager manager = new ExpenditureManager();
        
        // Create test category
        Category foodCategory = new Category("CAT001", "Food", "Food and dining expenses", "green");
        
        // Test 1: Add expenditures
        System.out.println("Test 1: Adding expenditures...");
        Expenditure exp1 = new Expenditure("EXP001", "Lunch at cafeteria", 
            new Float("25.50"), foodCategory, LocalDateTime.now(), "University Cafeteria", null, null);
        Expenditure exp2 = new Expenditure("EXP002", "Dinner with friends", 
            new Float("45.00"), foodCategory, LocalDateTime.now(), "Pizza Palace", null, null);
        
        boolean add1 = manager.addExpenditure(exp1);
        boolean add2 = manager.addExpenditure(exp2);
        System.out.println("Add results: " + add1 + ", " + add2);
        
        // Test 2: Get all expenditures
        System.out.println("\nTest 2: All expenditures:");
        List<Expenditure> allExp = manager.getAllExpenditures();
        System.out.println("Total expenditures: " + allExp.size());
        for (Expenditure exp : allExp) {
            System.out.println("  " + exp.get_summary());
        }
        
        // Test 3: Search expenditures
        System.out.println("\nTest 3: Searching expenditures...");
        List<Expenditure> searchResults = manager.searchExpenditures("lunch", "description");
        System.out.println("Search results for 'lunch': " + searchResults.size());
        for (Expenditure exp : searchResults) {
            System.out.println("  " + exp.get_summary());
        }
        
        // Test 4: Filter by amount
        System.out.println("\nTest 4: Filtering by amount...");
        List<Expenditure> filtered = manager.filterByAmount(new Float("20.00"), new Float("30.00"));
        System.out.println("Expenditures between ₵20-₵30: " + filtered.size());
        for (Expenditure exp : filtered) {
            System.out.println("  " + exp.get_summary());
        }
        
        // Test 5: Sort expenditures
        System.out.println("\nTest 5: Sorting expenditures by amount (descending)...");
        List<Expenditure> sorted = manager.sortExpenditures("amount", false);
        for (Expenditure exp : sorted) {
            System.out.println("  ₵" + exp.getAmount() + " - " + exp.getDescription());
        }
        
        // Test 6: Total amount
        System.out.println("\nTest 6: Total expenditure amount:");
        float total = manager.getTotalAmount();
        System.out.println("Total: ₵" + total);
        
        // Test 7: Get by ID
        System.out.println("\nTest 7: Get expenditure by ID:");
        Expenditure found = manager.getExpenditureById("EXP001");
        if (found != null) {
            System.out.println("Found: " + found.get_summary());
        } else {
            System.out.println("Not found");
        }
        
        System.out.println("ExpenditureManager tests completed.\n");
    }
} 