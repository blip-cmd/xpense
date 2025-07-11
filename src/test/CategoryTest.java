package test;

import app.modules.Category;

/**
 * Comprehensive test suite for the Category class.
 * Tests all constructors, methods, validation, and edge cases.
 */
public class CategoryTest {
    
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("=== CATEGORY CLASS TEST SUITE ===\n");
        
        // Run all test methods
        testConstructors();
        testValidation();
        testGetters();
        testEqualsAndHashCode();
        testToString();
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
     * Test all constructor variants
     */
    public static void testConstructors() {
        System.out.println("--- Testing Constructors ---");
        
        // Test single parameter constructor
        testCase("Constructor with name only", () -> {
            Category category = new Category("Food");
            return category.getName().equals("Food") && 
                   category.getDescription().equals("") && 
                   category.getColor().equals("");
        });
        
        // Test full constructor
        testCase("Constructor with all parameters", () -> {
            Category category = new Category("Transport", "Vehicle expenses", "blue");
            return category.getName().equals("Transport") && 
                   category.getDescription().equals("Vehicle expenses") && 
                   category.getColor().equals("blue");
        });
        
        // Test constructor with null description and color
        testCase("Constructor with null description/color", () -> {
            Category category = new Category("Entertainment", null, null);
            return category.getName().equals("Entertainment") && 
                   category.getDescription().equals("") && 
                   category.getColor().equals("");
        });
        
        // Test constructor trims whitespace
        testCase("Constructor trims whitespace", () -> {
            Category category = new Category("  Utilities  ", "  Water and electricity  ", "  green  ");
            return category.getName().equals("Utilities") && 
                   category.getDescription().equals("Water and electricity") && 
                   category.getColor().equals("green");
        });
        
        // Test constructor with empty name throws exception
        testCase("Constructor with empty name throws exception", () -> {
            try {
                new Category("");
                return false; // Should not reach here
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
        
        // Test constructor with null name throws exception
        testCase("Constructor with null name throws exception", () -> {
            try {
                new Category(null);
                return false; // Should not reach here
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
    }
    
    /**
     * Test validation methods
     */
    public static void testValidation() {
        System.out.println("\n--- Testing Validation ---");
        
        // Test valid category
        testCase("Valid category passes validation", () -> {
            Category category = new Category("Medical");
            return category.isValid();
        });
        
        // Test category with whitespace-only name is invalid
        testCase("Category with whitespace-only name is invalid", () -> {
            try {
                new Category("   ");
                return false; // Should throw exception
            } catch (IllegalArgumentException e) {
                return true;
            }
        });
    }
    
    /**
     * Test getter methods
     */
    public static void testGetters() {
        System.out.println("\n--- Testing Getters ---");
        
        Category category = new Category("Shopping", "Retail purchases", "purple");
        
        testCase("getName() returns correct value", () -> {
            return category.getName().equals("Shopping");
        });
        
        testCase("getDescription() returns correct value", () -> {
            return category.getDescription().equals("Retail purchases");
        });
        
        testCase("getColor() returns correct value", () -> {
            return category.getColor().equals("purple");
        });
    }
    
    /**
     * Test equals and hashCode methods
     */
    public static void testEqualsAndHashCode() {
        System.out.println("\n--- Testing Equals and HashCode ---");
        
        Category category1 = new Category("Food", "Groceries", "red");
        Category category2 = new Category("FOOD", "Different description", "blue");
        Category category3 = new Category("Transport");
        
        // Test equals with same name (case-insensitive)
        testCase("Categories with same name are equal (case-insensitive)", () -> {
            return category1.equals(category2);
        });
        
        // Test equals with different names
        testCase("Categories with different names are not equal", () -> {
            return !category1.equals(category3);
        });
        
        // Test equals with null
        testCase("Category does not equal null", () -> {
            return !category1.equals(null);
        });
        
        // Test equals with different object type
        testCase("Category does not equal different object type", () -> {
            return !category1.equals("Food");
        });
        
        // Test equals reflexive property
        testCase("Category equals itself", () -> {
            return category1.equals(category1);
        });
        
        // Test hashCode consistency
        testCase("Equal categories have same hashCode", () -> {
            return category1.hashCode() == category2.hashCode();
        });
    }
    
    /**
     * Test toString method
     */
    public static void testToString() {
        System.out.println("\n--- Testing ToString ---");
        
        Category category = new Category("Health", "Medical expenses", "green");
        
        testCase("toString() includes all fields", () -> {
            String str = category.toString();
            return str.contains("Health") && 
                   str.contains("Medical expenses") && 
                   str.contains("green");
        });
        
        testCase("toString() is not null or empty", () -> {
            String str = category.toString();
            return str != null && !str.isEmpty();
        });
    }
    
    /**
     * Test edge cases and boundary conditions
     */
    public static void testEdgeCases() {
        System.out.println("\n--- Testing Edge Cases ---");
        
        // Test category with very long name
        testCase("Category with long name", () -> {
            String longName = "A".repeat(100);
            Category category = new Category(longName);
            return category.getName().equals(longName);
        });
        
        // Test category with special characters
        testCase("Category with special characters", () -> {
            Category category = new Category("Bills & Utilities", "Water, gas & electricity", "blue-green");
            return category.getName().equals("Bills & Utilities") &&
                   category.getDescription().equals("Water, gas & electricity") &&
                   category.getColor().equals("blue-green");
        });
        
        // Test category with numbers
        testCase("Category with numbers", () -> {
            Category category = new Category("Tax2024", "Taxes for year 2024", "color123");
            return category.getName().equals("Tax2024") &&
                   category.getDescription().equals("Taxes for year 2024") &&
                   category.getColor().equals("color123");
        });
        
        // Test empty description and color
        testCase("Category with empty description and color", () -> {
            Category category = new Category("Miscellaneous", "", "");
            return category.getDescription().equals("") && 
                   category.getColor().equals("");
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