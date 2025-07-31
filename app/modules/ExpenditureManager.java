package app.modules;

import app.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenditureManager {
    private final SimpleArrayList<Expenditure> expenditures;
    private static int idCounter = 1000; // Start from 1000 for better-looking IDs

    public ExpenditureManager() {
        this.expenditures = new SimpleArrayList<>();
        initializeIdCounter();
    }

    /**
     * Initialize the ID counter based on existing expenditure IDs
     */
    private void initializeIdCounter() {
        int maxId = 1000; // Default starting point
        System.out.println("DEBUG: initializeIdCounter called, expenditures.size() = " + expenditures.size());
        for (Expenditure exp : expenditures) {
            String id = exp.getId();
            System.out.println("DEBUG: Found expenditure with ID: " + id);
            if (id != null && id.startsWith("EXP")) {
                try {
                    int numericPart = Integer.parseInt(id.substring(3));
                    System.out.println("DEBUG: Parsed numeric part: " + numericPart);
                    if (numericPart >= maxId) {
                        maxId = numericPart + 1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: Could not parse numeric part of ID: " + id);
                    // Ignore non-numeric IDs
                }
            }
        }
        idCounter = maxId;
        System.out.println("DEBUG: idCounter set to " + idCounter);
    }

    /**
     * Generate a unique expenditure ID
     * @return unique ID string
     */
    private synchronized String generateUniqueId() {
        return "EXP" + String.format("%04d", ++idCounter);
    }

    public boolean addExpenditure(Expenditure expenditure) {
        System.out.println("DEBUG: addExpenditure called");
        if (expenditure == null) {
            System.out.println("DEBUG: expenditure is null");
            return false;
        }
        if (!expenditure.isValid()) {
            System.out.println("DEBUG: expenditure is invalid");
            System.out.println("DEBUG: description=" + expenditure.getDescription());
            System.out.println("DEBUG: amount=" + expenditure.getAmount());
            System.out.println("DEBUG: category=" + expenditure.getCategory());
            System.out.println("DEBUG: dateTime=" + expenditure.getDateTime());
            return false;
        }
        
        // Generate ID if not provided
        if (expenditure.getId() == null || expenditure.getId().isBlank()) {
            String newId = generateUniqueId();
            System.out.println("DEBUG: Generated new ID: " + newId + " (idCounter was " + idCounter + ")");
            expenditure.setId(newId);
        } else {
            System.out.println("DEBUG: Using existing ID: " + expenditure.getId());
        }
        
        // Check for duplicate IDs
        for (Expenditure e : expenditures) {
            if (e.getId().equalsIgnoreCase(expenditure.getId())) {
                System.out.println("DEBUG: Duplicate ID found: " + expenditure.getId() + " already exists");
                return false;
            }
        }
        System.out.println("DEBUG: No duplicate ID, adding expenditure with ID: " + expenditure.getId());
        expenditures.add(expenditure);
        return true;
    }

    /**
     * Add expenditure with auto-generated ID
     * @param description expenditure description
     * @param amount expenditure amount
     * @param category expenditure category
     * @param phase expenditure phase
     * @return true if successful, false otherwise
     */
    public boolean addExpenditure(String description, BigDecimal amount, Category category, String phase) {
        Expenditure expenditure = new Expenditure(description, amount, category, LocalDateTime.now(), phase);
        return addExpenditure(expenditure);
    }

    /**
     * Add expenditure with auto-generated ID and bank account
     * @param description expenditure description
     * @param amount expenditure amount
     * @param category expenditure category
     * @param phase expenditure phase
     * @param bankAccountId bank account ID
     * @return true if successful, false otherwise
     */
    public boolean addExpenditure(String description, BigDecimal amount, Category category, String phase, String bankAccountId) {
        Expenditure expenditure = new Expenditure(description, amount, category, LocalDateTime.now(), phase);
        expenditure.setBankAccountId(bankAccountId);
        return addExpenditure(expenditure);
    }

    public SimpleArrayList<Expenditure> getAllExpenditures() {
        return expenditures;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (Expenditure exp : expenditures) {
            total = total.add(exp.getAmount());
        }
        return total;
    }

    /**
     * Get the most recently added expenditure
     * @return the last expenditure added, or null if none exist
     */
    public Expenditure getLastExpenditure() {
        if (expenditures.size() == 0) {
            return null;
        }
        return expenditures.get(expenditures.size() - 1);
    }

    /**
     * Load expenditures from external source and update ID counter
     * @param loadedExpenditures list of expenditures to add
     */
    public void loadExpenditures(SimpleArrayList<Expenditure> loadedExpenditures) {
        if (loadedExpenditures != null) {
            for (int i = 0; i < loadedExpenditures.size(); i++) {
                expenditures.add(loadedExpenditures.get(i));
            }
            initializeIdCounter(); // Update counter based on loaded data
        }
    }
}