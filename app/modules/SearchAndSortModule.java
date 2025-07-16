package app.modules;

import app.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SearchAndSortModule {
    
    // Sort expenditures alphabetically by category
    public SimpleArrayList<Expenditure> sortByCategoryAlphabetical(SimpleArrayList<Expenditure> expenditures) {
        if (expenditures == null || expenditures.size() <= 1) return expenditures;
        
        SimpleArrayList<Expenditure> sorted = new SimpleArrayList<>();
        for (int i = 0; i < expenditures.size(); i++) {
            sorted.add(expenditures.get(i));
        }
        
        // Simple bubble sort by category name
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                String cat1 = sorted.get(j).getCategory().getName().toLowerCase();
                String cat2 = sorted.get(j + 1).getCategory().getName().toLowerCase();
                if (cat1.compareTo(cat2) > 0) {
                    Expenditure temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }
    
    // Sort expenditures chronologically
    public SimpleArrayList<Expenditure> sortByDateChronological(SimpleArrayList<Expenditure> expenditures) {
        if (expenditures == null || expenditures.size() <= 1) return expenditures;
        
        SimpleArrayList<Expenditure> sorted = new SimpleArrayList<>();
        for (int i = 0; i < expenditures.size(); i++) {
            sorted.add(expenditures.get(i));
        }
        
        // Simple bubble sort by date
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                LocalDateTime date1 = sorted.get(j).getDateTime();
                LocalDateTime date2 = sorted.get(j + 1).getDateTime();
                if (date1.isAfter(date2)) {
                    Expenditure temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }
    
    // Search by time range
    public SimpleArrayList<Expenditure> searchByTimeRange(SimpleArrayList<Expenditure> expenditures, 
                                                         LocalDate startDate, LocalDate endDate) {
        SimpleArrayList<Expenditure> results = new SimpleArrayList<>();
        if (expenditures == null || startDate == null || endDate == null) return results;
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            LocalDate expDate = exp.getDateTime().toLocalDate();
            if ((expDate.isEqual(startDate) || expDate.isAfter(startDate)) && 
                (expDate.isEqual(endDate) || expDate.isBefore(endDate))) {
                results.add(exp);
            }
        }
        return results;
    }
    
    // Search by category
    public SimpleArrayList<Expenditure> searchByCategory(SimpleArrayList<Expenditure> expenditures, 
                                                        String categoryName) {
        SimpleArrayList<Expenditure> results = new SimpleArrayList<>();
        if (expenditures == null || categoryName == null) return results;
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (exp.getCategory().getName().equalsIgnoreCase(categoryName.trim())) {
                results.add(exp);
            }
        }
        return results;
    }
    
    // Search by cost range
    public SimpleArrayList<Expenditure> searchByCostRange(SimpleArrayList<Expenditure> expenditures, 
                                                         BigDecimal minAmount, BigDecimal maxAmount) {
        SimpleArrayList<Expenditure> results = new SimpleArrayList<>();
        if (expenditures == null || minAmount == null || maxAmount == null) return results;
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            BigDecimal amount = exp.getAmount();
            if (amount.compareTo(minAmount) >= 0 && amount.compareTo(maxAmount) <= 0) {
                results.add(exp);
            }
        }
        return results;
    }
    
    // Search by bank account
    public SimpleArrayList<Expenditure> searchByBankAccount(SimpleArrayList<Expenditure> expenditures, 
                                                           String bankAccountId) {
        SimpleArrayList<Expenditure> results = new SimpleArrayList<>();
        if (expenditures == null || bankAccountId == null) return results;
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (bankAccountId.equalsIgnoreCase(exp.getBankAccountId())) {
                results.add(exp);
            }
        }
        return results;
    }
    
    // Search by phase
    public SimpleArrayList<Expenditure> searchByPhase(SimpleArrayList<Expenditure> expenditures, 
                                                     String phase) {
        SimpleArrayList<Expenditure> results = new SimpleArrayList<>();
        if (expenditures == null || phase == null) return results;
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            if (phase.equalsIgnoreCase(exp.getPhase())) {
                results.add(exp);
            }
        }
        return results;
    }
}
