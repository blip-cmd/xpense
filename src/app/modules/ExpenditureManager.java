package app.modules;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Manages expenditure operations and business logic
 * Implements CRUD operations, search, and sorting for expenditures
 */
public class ExpenditureManager {
    private List<Expenditure> expenditures;
    private HashMap<String, Expenditure> expenditureMap; // For O(1) lookups
    private FileManager fileManager;
    
    /**
     * Constructor for ExpenditureManager
     * Initializes data structures and loads existing data
     */
    public ExpenditureManager() {
        this.expenditures = new ArrayList<>();
        this.expenditureMap = new HashMap<>();
        this.fileManager = new FileManager();
        loadExpenditures();
    }
    
    /**
     * Add a new expenditure
     * Validates expenditure before adding and saves to file
     * @param expenditure the expenditure to add
     * @return true if successful, false otherwise
     */
    public boolean addExpenditure(Expenditure expenditure) {
        if (expenditure == null || !expenditure.isValid()) {
            return false;
        }
        
        // Check if ID already exists
        if (expenditureMap.containsKey(expenditure.getId())) {
            return false;
        }
        
        expenditures.add(expenditure);
        expenditureMap.put(expenditure.getId(), expenditure);
        saveExpenditures();
        return true;
    }
    
    /**
     * Get all expenditures
     * @return list of all expenditures
     */
    public List<Expenditure> getAllExpenditures() {
        return new ArrayList<>(expenditures);
    }
    
    /**
     * Delete an expenditure
     * Removes expenditure by ID from both list and map
     * @param id the ID of expenditure to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteExpenditure(String id) {
        if (id == null || !expenditureMap.containsKey(id)) {
            return false;
        }
        
        Expenditure expenditure = expenditureMap.get(id);
        expenditures.remove(expenditure);
        expenditureMap.remove(id);
        saveExpenditures();
        return true;
    }
    
    /**
     * Update an expenditure
     * Updates existing expenditure with new data
     * @param expenditure the updated expenditure
     * @return true if successful, false otherwise
     */
    public boolean updateExpenditure(Expenditure expenditure) {
        if (expenditure == null || !expenditure.isValid()) {
            return false;
        }
        
        String id = expenditure.getId();
        if (!expenditureMap.containsKey(id)) {
            return false;
        }
        
        // Find and replace in list
        for (int i = 0; i < expenditures.size(); i++) {
            if (expenditures.get(i).getId().equals(id)) {
                expenditures.set(i, expenditure);
                break;
            }
        }
        
        // Update map
        expenditureMap.put(id, expenditure);
        saveExpenditures();
        return true;
    }
    
    /**
     * Search expenditures by various criteria
     * @param searchTerm the term to search for
     * @param searchType the type of search (id, description, category, location)
     * @return list of matching expenditures
     */
    public List<Expenditure> searchExpenditures(String searchTerm, String searchType) {
        List<Expenditure> results = new ArrayList<>();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return results;
        }
        
        String term = searchTerm.toLowerCase().trim();
        
        for (Expenditure exp : expenditures) {
            boolean matches = false;
            
            switch (searchType.toLowerCase()) {
                case "id":
                    matches = exp.getId().toLowerCase().contains(term);
                    break;
                case "description":
                    matches = exp.getDescription().toLowerCase().contains(term);
                    break;
                case "category":
                    matches = exp.getCategory() != null && 
                             exp.getCategory().getName().toLowerCase().contains(term);
                    break;
                case "location":
                    matches = exp.getLocation() != null && 
                             exp.getLocation().toLowerCase().contains(term);
                    break;
                case "all":
                default:
                    matches = exp.getId().toLowerCase().contains(term) ||
                             exp.getDescription().toLowerCase().contains(term) ||
                             (exp.getCategory() != null && exp.getCategory().getName().toLowerCase().contains(term)) ||
                             (exp.getLocation() != null && exp.getLocation().toLowerCase().contains(term));
                    break;
            }
            
            if (matches) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Filter expenditures by amount range
     * @param minAmount minimum amount (inclusive)
     * @param maxAmount maximum amount (inclusive)
     * @return list of expenditures within range
     */
    public List<Expenditure> filterByAmount(BigDecimal minAmount, BigDecimal maxAmount) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            BigDecimal amount = exp.getAmount();
            if ((minAmount == null || amount.compareTo(minAmount) >= 0) &&
                (maxAmount == null || amount.compareTo(maxAmount) <= 0)) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Sort expenditures by various criteria
     * @param sortBy the field to sort by (date, amount, category, id)
     * @param ascending true for ascending order, false for descending
     * @return sorted list of expenditures
     */
    public List<Expenditure> sortExpenditures(String sortBy, boolean ascending) {
        List<Expenditure> sorted = new ArrayList<>(expenditures);
        
        Comparator<Expenditure> comparator = null;
        
        switch (sortBy.toLowerCase()) {
            case "date":
                comparator = Comparator.comparing(Expenditure::getDateTime);
                break;
            case "amount":
                comparator = Comparator.comparing(Expenditure::getAmount);
                break;
            case "category":
                comparator = Comparator.comparing(exp -> 
                    exp.getCategory() != null ? exp.getCategory().getName() : "");
                break;
            case "id":
                comparator = Comparator.comparing(Expenditure::getId);
                break;
            case "description":
                comparator = Comparator.comparing(Expenditure::getDescription);
                break;
            default:
                return sorted; // Return unsorted if invalid sort field
        }
        
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        sorted.sort(comparator);
        return sorted;
    }
    
    /**
     * Get expenditure by ID (O(1) lookup)
     * @param id the expenditure ID
     * @return expenditure or null if not found
     */
    public Expenditure getExpenditureById(String id) {
        return expenditureMap.get(id);
    }
    
    /**
     * Get expenditures by category
     * @param categoryName the category name
     * @return list of expenditures in the category
     */
    public List<Expenditure> getExpendituresByCategory(String categoryName) {
        List<Expenditure> results = new ArrayList<>();
        
        for (Expenditure exp : expenditures) {
            if (exp.getCategory() != null && 
                exp.getCategory().getName().equalsIgnoreCase(categoryName)) {
                results.add(exp);
            }
        }
        
        return results;
    }
    
    /**
     * Get total expenditure amount
     * @return sum of all expenditure amounts
     */
    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (Expenditure exp : expenditures) {
            total = total.add(exp.getAmount());
        }
        return total;
    }
    
    /**
     * Load expenditures from file
     */
    private void loadExpenditures() {
        // Note: FileManager expects different format, we'll work with what we have
        // For now, we'll just initialize empty - FileManager needs to be fixed to match Expenditure constructor
        expenditures.clear();
        expenditureMap.clear();
        
        // TODO: Once FileManager is fixed to match Expenditure constructor, uncomment:
        // List<Expenditure> loaded = fileManager.loadExpenditures("expenditures.txt");
        // if (loaded != null) {
        //     for (Expenditure exp : loaded) {
        //         expenditures.add(exp);
        //         expenditureMap.put(exp.getId(), exp);
        //     }
        // }
    }
    
    /**
     * Save expenditures to file
     */
    private void saveExpenditures() {
        // TODO: Once FileManager is fixed to match Expenditure constructor, uncomment:
        // fileManager.saveExpenditures(expenditures, "expenditures.txt");
    }
}