package app.modules;

import app.util.SimpleSet;
import app.util.SimpleMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages category operations and validation using custom data structures.
 */
public class CategoryManager {
    private SimpleSet<Category> categories;
    private SimpleMap<String, List<Object>> categoryExpenditures; // Replace Object with Expenditure if available

    public CategoryManager() {
        categories = new SimpleSet<>();
        categoryExpenditures = new SimpleMap<>();
    }

    public boolean addCategory(Category category) {
        if (category == null || !category.isValid()) return false;
        if (categories.contains(category)) return false;
        categories.add(category);
        if (!categoryExpenditures.containsKey(category.getName())) {
            categoryExpenditures.put(category.getName(), new ArrayList<>());
        }
        return true;
    }

    public List<Category> searchCategories(String searchTerm) {
        List<Category> result = new ArrayList<>();
        if (searchTerm == null) return result;
        String term = searchTerm.trim().toLowerCase();
        for (Category c : categories) {
            if (c.getName().toLowerCase().contains(term)) {
                result.add(c);
            }
        }
        return result;
    }

    public boolean validateCategory(String categoryName) {
        if (categoryName == null) return false;
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public List<Category> getAllCategories() {
        return categories.toList();
    }

    public String getCategoryStatistics(String categoryName) {
        if (!validateCategory(categoryName)) return "Category not found.";
        int count = 0;
        List<Object> expenditures = categoryExpenditures.get(categoryName);
        if (expenditures != null) count = expenditures.size();
        return "Expenditures in '" + categoryName + "': " + count;
    }

    /**
     * Delete a category by name.
     * @param categoryName The name of the category to delete
     * @return true if category was deleted, false if not found
     */
    public boolean deleteCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) return false;
        
        // Find and remove the category
        Category toRemove = null;
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(categoryName.trim())) {
                toRemove = c;
                break;
            }
        }
        
        if (toRemove != null) {
            categories.remove(toRemove);
            categoryExpenditures.remove(categoryName);
            return true;
        }
        return false;
    }

    /**
     * Update an existing category's details.
     * @param oldName The current name of the category
     * @param newCategory The updated category object
     * @return true if update was successful, false otherwise
     */
    public boolean updateCategory(String oldName, Category newCategory) {
        if (oldName == null || newCategory == null || !newCategory.isValid()) return false;
        
        // Check if old category exists
        Category oldCategory = null;
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(oldName.trim())) {
                oldCategory = c;
                break;
            }
        }
        
        if (oldCategory != null) {
            categories.remove(oldCategory);
            categories.add(newCategory);
            
            // Update expenditure mapping if name changed
            if (!oldName.equalsIgnoreCase(newCategory.getName())) {
                List<Object> expenditures = categoryExpenditures.get(oldName);
                if (expenditures != null) {
                    categoryExpenditures.remove(oldName);
                    categoryExpenditures.put(newCategory.getName(), expenditures);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Get the number of categories.
     * @return total number of categories
     */
    public int getCategoryCount() {
        return categories.size();
    }

    /**
     * Check if no categories exist.
     * @return true if no categories exist
     */
    public boolean isEmpty() {
        return categories.size() == 0;
    }

    /**
     * Add an expenditure to a category's tracking list.
     * @param categoryName The category name
     * @param expenditure The expenditure to add
     * @return true if added successfully
     */
    public boolean addExpenditureToCategory(String categoryName, Object expenditure) {
        if (!validateCategory(categoryName) || expenditure == null) return false;
        
        List<Object> expenditures = categoryExpenditures.get(categoryName);
        if (expenditures != null) {
            expenditures.add(expenditure);
            return true;
        }
        return false;
    }

    /**
     * Get detailed statistics for all categories.
     * @return formatted string with all category statistics
     */
    public String getAllCategoryStatistics() {
        if (categories.size() == 0) {
            return "No categories available.";
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== CATEGORY STATISTICS ===\n");
        stats.append(String.format("Total Categories: %d\n\n", categories.size()));
        
        for (Category category : categories) {
            List<Object> expenditures = categoryExpenditures.get(category.getName());
            int count = expenditures != null ? expenditures.size() : 0;
            stats.append(String.format("• %s: %d expenditures\n", 
                                     category.getName(), count));
            if (!category.getDescription().isEmpty()) {
                stats.append(String.format("  Description: %s\n", category.getDescription()));
            }
            stats.append("\n");
        }
        
        return stats.toString();
    }

    /**
     * Clear all categories and their associated data.
     */
    public void clearAllCategories() {
        categories = new SimpleSet<>();
        categoryExpenditures = new SimpleMap<>();
    }
}