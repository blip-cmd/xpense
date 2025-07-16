package app.modules;

import app.util.*;
import java.util.Scanner;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CLIHandler {
    private final XpenseSystem xpense;
    private final Scanner scanner;

    public CLIHandler(XpenseSystem xpenseSystem) {
        this.xpense = xpenseSystem;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Welcome to Xpense - Project Financial Tracker");
        boolean running = true;
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Add Expenditure");
            System.out.println("2. List Expenditures");
            System.out.println("3. Add Bank Account");
            System.out.println("4. List Bank Accounts");
            System.out.println("5. Add Category");
            System.out.println("6. List Categories");
            System.out.println("7. View Alerts");
            System.out.println("8. Exit");
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": addExpenditure(); break;
                case "2": listExpenditures(); break;
                case "3": addBankAccount(); break;
                case "4": listBankAccounts(); break;
                case "5": addCategory(); break;
                case "6": listCategories(); break;
                case "7": xpense.getAlertSystem().displayAllAlerts(); break;
                case "8": running = false; break;
                default: System.out.println("Invalid option."); break;
            }
        }
        System.out.println("Exiting Xpense CLI. Goodbye!");
        scanner.close();
    }

    private void addExpenditure() {
        System.out.print("Expenditure ID: ");
        String id = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        System.out.print("Category name: ");
        String categoryName = scanner.nextLine();
        if (!xpense.getCategoryManager().validateCategory(categoryName)) {
            System.out.println("Category does not exist.");
            return;
        }
        Category category = null;
        SimpleArrayList<Category> cats = xpense.getAllCategories();
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getName().equalsIgnoreCase(categoryName)) category = cats.get(i);
        }
        System.out.print("Phase: ");
        String phase = scanner.nextLine();
        System.out.print("Bank Account ID: ");
        String bankAccountId = scanner.nextLine();
        if (xpense.getBankLedger().getAccount(bankAccountId) == null) {
            System.out.println("Bank account does not exist.");
            return;
        }
        Expenditure exp = new Expenditure(id, description, amount, category, LocalDateTime.now(), phase);
        exp.setBankAccountId(bankAccountId);
        boolean added = xpense.addExpenditure(exp);
        if (added) System.out.println("Expenditure added and bank debited.");
        else System.out.println("Failed to add expenditure. See alerts.");
    }

    private void listExpenditures() {
        SimpleArrayList<Expenditure> exps = xpense.getAllExpenditures();
        System.out.println("ID | Desc | Amount | Category | Date | Phase | Account");
        for (int i = 0; i < exps.size(); i++) {
            Expenditure e = exps.get(i);
            System.out.printf("%s | %s | %s | %s | %s | %s | %s\n",
                e.getId(), e.getDescription(), e.getAmount(),
                e.getCategory().getName(), e.getDateTime().toLocalDate(), e.getPhase(), e.getBankAccountId());
        }
    }

    private void addBankAccount() {
        System.out.print("Account ID: ");
        String id = scanner.nextLine();
        System.out.print("Account Name: ");
        String name = scanner.nextLine();
        System.out.print("Initial Balance: ");
        BigDecimal bal = new BigDecimal(scanner.nextLine());
        BankAccount acct = new BankAccount(id, name, bal);
        boolean added = xpense.addBankAccount(acct);
        if (added) System.out.println("Bank account added.");
        else System.out.println("Failed to add bank account.");
    }

    private void listBankAccounts() {
        SimpleArrayList<BankAccount> acts = xpense.getAllBankAccounts();
        System.out.println("ID | Name | Balance");
        for (int i = 0; i < acts.size(); i++) {
            BankAccount b = acts.get(i);
            System.out.printf("%s | %s | %s\n", b.getAccountNumber(), b.getAccountName(), b.getBalance());
        }
    }

    private void addCategory() {
        System.out.print("Category ID: ");
        String id = scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Color: ");
        String color = scanner.nextLine();
        Category c = new Category(id, name, desc, color);
        boolean added = xpense.addCategory(c);
        if (added) System.out.println("Category added.");
        else System.out.println("Failed to add category.");
    }

    private void listCategories() {
        SimpleArrayList<Category> cats = xpense.getAllCategories();
        System.out.println("ID | Name | Description | Color");
        for (int i = 0; i < cats.size(); i++) {
            Category c = cats.get(i);
            System.out.printf("%s | %s | %s | %s\n", c.getId(), c.getName(), c.getDescription(), c.getColor());
        }
    }
}