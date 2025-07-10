package test;

import app.modules.Category;
import app.modules.CategoryManager;
import java.util.List;

/**
 * Comprehensive test suite for the CategoryManager class.
 * Tests all CRUD operations, search functionality, validation, and edge cases.
 */
public class CategoryManagerTest {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("=== CATEGORY MANAGER TEST SUITE ===\n");
        
        // Run all test methods
        testAddCategory();
        testDeleteCategory();
        testUpdateCategory();
        testSearchCategories();
        testValidateCategory();
        testGetAllCategories();
        testCategoryStatistics();
        testExpenditureTracking();
        testUtilityMethods();
        testEdgeCases();
        
        // Print summary
        System.out.println("\n=== TEST SUMMARY ===");
        System.out.printf("Tests Passed: %d/%d\n", testsPassed, testsTotal);
        System.out.printf("Success Rate: %.1f%%\n", (testsPassed * 100.0) / testsTotal);
        
        if (testsPassed == testsTotal) {
            System.out.println("✅ ALL TESTS PASSED!");
        } else {
            System.out.println("❌ SOME TESTS FAILED!");
        }
    }
    
    /**
     * Test adding categories
     */
    public static void testAddCategory() {
        System.out.println("--- Testing Add Category ---");
        
        CategoryManager manager = new CategoryManager();
        Category category1 = new Category("Food", "Groceries and dining", "green");
        Category category2 = new Category("Transport", "Vehicle expenses", "blue");
        
        // Test adding valid category
        testCase("Add valid category", () -> {
            return manager.addCategory(category1);
        });
        
        // Test adding duplicate category (should fail)
        testCase("Add duplicate category fails", () -> {
            Category duplicate = new Category("Food", "Different description", "red");
            return !manager.addCategory(duplicate);
        });
        
        // Test adding second different category
        testCase("Add second different category", () -> {
            return manager.addCategory(category2);
        });
        
        // Test adding null category
        testCase("Add null category fails", () -> {
            return !manager.addCategory(null);
        });
        
        // Test adding invalid category
        testCase("Add invalid category fails", () -> {
            try {
                Category invalidCategory = new Category("");
                return false; // Should throw exception
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
        
        // Test category count after additions
        testCase("Category count is correct after additions", () -> {
            return manager.getCategoryCount() == 2;
        });
    }
    
    /**
     * Test deleting categories
     */
    public static void testDeleteCategory() {
        System.out.println("\n--- Testing Delete Category ---");
        
        CategoryManager manager = new CategoryManager();
        Category category1 = new Category("Health");
        Category category2 = new Category("Entertainment");
        
        manager.addCategory(category1);
        manager.addCategory(category2);
        
        // Test deleting existing category
        testCase("Delete existing category", () -> {
            return manager.deleteCategory("Health");
        });
        
        // Test deleting non-existing category
        testCase("Delete non-existing category fails", () -> {
            return !manager.deleteCategory("NonExistent");
        });
        
        // Test deleting with null name
        testCase("Delete with null name fails", () -> {
            return !manager.deleteCategory(null);
        });
        
        // Test deleting with empty name
        testCase("Delete with empty name fails", () -> {
            return !manager.deleteCategory("");
        });
        
        // Test category count after deletion
        testCase("Category count correct after deletion", () -> {
            return manager.getCategoryCount() == 1;
        });
        
        // Test remaining category is correct
        testCase("Correct category remains after deletion", () -> {
            return manager.validateCategory("Entertainment");
        });
    }
    
    /**
     * Test updating categories
     */
    public static void testUpdateCategory() {
        System.out.println("\n--- Testing Update Category ---");
        
        CategoryManager manager = new CategoryManager();
        Category originalCategory = new Category("Bills", "Monthly bills", "red");
        Category updatedCategory = new Category("Utilities", "Water, gas, electricity", "blue");
        
        manager.addCategory(originalCategory);
        
        // Test updating existing category
        testCase("Update existing category", () -> {
            return manager.updateCategory("Bills", updatedCategory);
        });
        
        // Test old category name no longer exists
        testCase("Old category name no longer exists", () -> {
            return !manager.validateCategory("Bills");
        });
        
        // Test new category name exists
        testCase("New category name exists", () -> {
            return manager.validateCategory("Utilities");
        });
        
        // Test updating non-existing category
        testCase("Update non-existing category fails", () -> {
            Category newCategory = new Category("Test");
            return !manager.updateCategory("NonExistent", newCategory);
        });
        
        // Test updating with null parameters
        testCase("Update with null old name fails", () -> {
            Category newCategory = new Category("Test");
            return !manager.updateCategory(null, newCategory);
        });
        
        testCase("Update with null new category fails", () -> {
            return !manager.updateCategory("Utilities", null);
        });
    }
    
    /**
     * Test searching categories
     */
    public static void testSearchCategories() {
        System.out.println("\n--- Testing Search Categories ---");
        
        CategoryManager manager = new CategoryManager();
        manager.addCategory(new Category("Food & Dining"));
        manager.addCategory(new Category("Transportation"));
        manager.addCategory(new Category("Entertainment"));
        manager.addCategory(new Category("Food Delivery"));
        
        // Test partial name search
        testCase("Search by partial name", () -> {
            List<Category> results = manager.searchCategories("Food");
            return results.size() == 2;
        });
        
        // Test case-insensitive search
        testCase("Search is case-insensitive", () -> {
            List<Category> results = manager.searchCategories("TRANSPORT");
            return results.size() == 1 && results.get(0).getName().equals("Transportation");
        });
        
        // Test exact match search
        testCase("Search exact match", () -> {
            List<Category> results = manager.searchCategories("Entertainment");
            return results.size() == 1 && results.get(0).getName().equals("Entertainment");
        });
        
        // Test search with no results
        testCase("Search with no results", () -> {
            List<Category> results = manager.searchCategories("NonExistent");
            return results.size() == 0;
        });
        
        // Test search with null term
        testCase("Search with null term returns empty list", () -> {
            List<Category> results = manager.searchCategories(null);
            return results.size() == 0;
        });
        
        // Test search with empty term
        testCase("Search with empty term returns all categories", () -> {
            List<Category> results = manager.searchCategories("");
            return results.size() == 4;
        });
    }
    
    /**
     * Test category validation
     */
    public static void testValidateCategory() {
        System.out.println("\n--- Testing Validate Category ---");
        
        CategoryManager manager = new CategoryManager();
        manager.addCategory(new Category("Shopping"));
        manager.addCategory(new Category("Medical"));
        
        // Test validating existing category
        testCase("Validate existing category", () -> {
            return manager.validateCategory("Shopping");
        });
        
        // Test case-insensitive validation
        testCase("Validation is case-insensitive", () -> {
            return manager.validateCategory("MEDICAL");
        });
        
        // Test validating non-existing category
        testCase("Validate non-existing category fails", () -> {
            return !manager.validateCategory("NonExistent");
        });
        
        // Test validating null category
        testCase("Validate null category fails", () -> {
            return !manager.validateCategory(null);
        });
    }
    
    /**
     * Test getting all categories
     */
    public static void testGetAllCategories() {
        System.out.println("\n--- Testing Get All Categories ---");
        
        CategoryManager manager = new CategoryManager();
        
        // Test empty manager
        testCase("Get all categories from empty manager", () -> {
            List<Category> categories = manager.getAllCategories();
            return categories.size() == 0;
        });
        
        // Add some categories
        manager.addCategory(new Category("Category1"));
        manager.addCategory(new Category("Category2"));
        manager.addCategory(new Category("Category3"));
        
        // Test getting all categories
        testCase("Get all categories returns correct count", () -> {
            List<Category> categories = manager.getAllCategories();
            return categories.size() == 3;
        });
        
        // Test that returned list contains expected categories
        testCase("Get all categories contains expected categories", () -> {
            List<Category> categories = manager.getAllCategories();
            boolean hasCategory1 = false, hasCategory2 = false, hasCategory3 = false;
            
            for (Category c : categories) {
                if (c.getName().equals("Category1")) hasCategory1 = true;
                if (c.getName().equals("Category2")) hasCategory2 = true;
                if (c.getName().equals("Category3")) hasCategory3 = true;
            }
            
            return hasCategory1 && hasCategory2 && hasCategory3;
        });
    }
    
    /**
     * Test category statistics
     */
    public static void testCategoryStatistics() {
        System.out.println("\n--- Testing Category Statistics ---");
        
        CategoryManager manager = new CategoryManager();
        Category category = new Category("TestCategory");
        manager.addCategory(category);
        
        // Test statistics for existing category
        testCase("Get statistics for existing category", () -> {
            String stats = manager.getCategoryStatistics("TestCategory");
            return stats.contains("TestCategory") && stats.contains("0");
        });
        
        // Test statistics for non-existing category
        testCase("Get statistics for non-existing category", () -> {
            String stats = manager.getCategoryStatistics("NonExistent");
            return stats.equals("Category not found.");
        });
        
        // Test getting all category statistics
        testCase("Get all category statistics", () -> {
            String stats = manager.getAllCategoryStatistics();
            return stats.contains("CATEGORY STATISTICS") && stats.contains("TestCategory");
        });
        
        // Test all statistics for empty manager
        CategoryManager emptyManager = new CategoryManager();
        testCase("Get all statistics for empty manager", () -> {
            String stats = emptyManager.getAllCategoryStatistics();
            return stats.equals("No categories available.");
        });
    }
    
    /**
     * Test expenditure tracking
     */
    public static void testExpenditureTracking() {
        System.out.println("\n--- Testing Expenditure Tracking ---");
        
        CategoryManager manager = new CategoryManager();
        Category category = new Category("TrackingTest");
        manager.addCategory(category);
        
        // Test adding expenditure to category
        testCase("Add expenditure to valid category", () -> {
            return manager.addExpenditureToCategory("TrackingTest", "MockExpenditure1");
        });
        
        // Test adding multiple expenditures
        testCase("Add multiple expenditures to category", () -> {
            manager.addExpenditureToCategory("TrackingTest", "MockExpenditure2");
            manager.addExpenditureToCategory("TrackingTest", "MockExpenditure3");
            
            String stats = manager.getCategoryStatistics("TrackingTest");
            return stats.contains("3");
        });
        
        // Test adding expenditure to non-existing category
        testCase("Add expenditure to non-existing category fails", () -> {
            return !manager.addExpenditureToCategory("NonExistent", "MockExpenditure");
        });
        
        // Test adding null expenditure
        testCase("Add null expenditure fails", () -> {
            return !manager.addExpenditureToCategory("TrackingTest", null);
        });
    }
    
    /**
     * Test utility methods
     */
    public static void testUtilityMethods() {
        System.out.println("\n--- Testing Utility Methods ---");
        
        CategoryManager manager = new CategoryManager();
        
        // Test isEmpty on empty manager
        testCase("isEmpty returns true for empty manager", () -> {
            return manager.isEmpty();
        });
        
        // Test getCategoryCount on empty manager
        testCase("getCategoryCount returns 0 for empty manager", () -> {
            return manager.getCategoryCount() == 0;
        });
        
        // Add categories and test again
        manager.addCategory(new Category("Test1"));
        manager.addCategory(new Category("Test2"));
        
        testCase("isEmpty returns false after adding categories", () -> {
            return !manager.isEmpty();
        });
        
        testCase("getCategoryCount returns correct count", () -> {
            return manager.getCategoryCount() == 2;
        });
        
        // Test clearAllCategories
        manager.clearAllCategories();
        
        testCase("clearAllCategories empties the manager", () -> {
            return manager.isEmpty() && manager.getCategoryCount() == 0;
        });
    }
    
    /**
     * Test edge cases and boundary conditions
     */
    public static void testEdgeCases() {
        System.out.println("\n--- Testing Edge Cases ---");
        
        CategoryManager manager = new CategoryManager();
        
        // Test operations on empty manager
        testCase("Delete from empty manager fails gracefully", () -> {
            return !manager.deleteCategory("AnyCategory");
        });
        
        testCase("Update in empty manager fails gracefully", () -> {
            Category category = new Category("Test");
            return !manager.updateCategory("AnyCategory", category);
        });
        
        testCase("Validate in empty manager fails gracefully", () -> {
            return !manager.validateCategory("AnyCategory");
        });
        
        // Test with categories containing special characters
        testCase("Handle categories with special characters", () -> {
            Category specialCategory = new Category("Bills & Utilities");
            boolean added = manager.addCategory(specialCategory);
            boolean validated = manager.validateCategory("Bills & Utilities");
            return added && validated;
        });
        
        // Test case sensitivity edge cases
        testCase("Case sensitivity in search and validation", () -> {
            manager.addCategory(new Category("CaseSensitive"));
            
            boolean validateUpper = manager.validateCategory("CASESENSITIVE");
            boolean validateLower = manager.validateCategory("casesensitive");
            List<Category> searchResults = manager.searchCategories("CASE");
            
            return validateUpper && validateLower && searchResults.size() == 1;
        });
        
        // Test very long category names
        testCase("Handle very long category names", () -> {
            String longName = "A".repeat(200);
            Category longCategory = new Category(longName);
            manager.addCategory(longCategory);
            return manager.validateCategory(longName);
        });
    }
    
    /**
     * Helper method to run individual test cases
     */
    private static void testCase(String testName, TestFunction test) {
        testsTotal++;
        try {
            if (test.run()) {
                System.out.println("✅ " + testName);
                testsPassed++;
            } else {
                System.out.println("❌ " + testName);
            }
        } catch (Exception e) {
            System.out.println("❌ " + testName + " (Exception: " + e.getMessage() + ")");
        }
    }
    
    /**
     * Functional interface for test functions
     */
    @FunctionalInterface
    private interface TestFunction {
        boolean run() throws Exception;
    }
}
