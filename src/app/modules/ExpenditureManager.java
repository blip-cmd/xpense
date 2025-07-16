package app.modules;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

/**
 * Manages expenditure operations and business logic
 * Implements CRUD operations, search, and sorting for expenditures
 */
public class ExpenditureManager {
    private List<Expenditure> expenditures;
    private HashMap<String, Expenditure> expenditureMap; // For O(1) lookups
    private FileManager fileManager;

    public ExpenditureManager() {
        this.expenditures = new ArrayList<>();
        this.expenditureMap = new HashMap<>();
        this.fileManager = new FileManager();
        loadExpenditures();
    }

    public boolean addExpenditure(Expenditure expenditure) {
        if (expenditure == null || !expenditure.isValid()) {
            return false;
        }

        if (expenditureMap.containsKey(expenditure.getId())) {
            return false;
        }

        expenditures.add(expenditure);
        expenditureMap.put(expenditure.getId(), expenditure);
        saveExpenditures();
        return true;
    }

    public List<Expenditure> getAllExpenditures() {
        return new ArrayList<>(expenditures);
    }

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

    public boolean updateExpenditure(Expenditure expenditure) {
        if (expenditure == null || !expenditure.isValid()) {
            return false;
        }

        String id = expenditure.getId();
        if (!expenditureMap.containsKey(id)) {
            return false;
        }

        for (int i = 0; i < expenditures.size(); i++) {
            if (expenditures.get(i).getId().equals(id)) {
                expenditures.set(i, expenditure);
                break;
            }
        }

        expenditureMap.put(id, expenditure);
        saveExpenditures();
        return true;
    }

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
     */
    public List<Expenditure> filterByAmount(Float minAmount, Float maxAmount) {
        List<Expenditure> results = new ArrayList<>();

        for (Expenditure exp : expenditures) {
            Float amount = exp.getAmount();
            if ((minAmount == null || amount >= minAmount) &&
                (maxAmount == null || amount <= maxAmount)) {
                results.add(exp);
            }
        }

        return results;
    }

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
                return sorted;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        sorted.sort(comparator);
        return sorted;
    }

    public Expenditure getExpenditureById(String id) {
        return expenditureMap.get(id);
    }

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
     */
    public Float getTotalAmount() {
        Float total = 0f;
        for (Expenditure exp : expenditures) {
            total += exp.getAmount();
        }
        return total;
    }

    private void loadExpenditures() {
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

    private void saveExpenditures() {
        // TODO: Once FileManager is fixed to match Expenditure constructor, uncomment:
        // fileManager.saveExpenditures(expenditures, "expenditures.txt");
    }
}
