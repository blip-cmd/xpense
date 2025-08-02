/**
 * Category.java
 * 
 * Represents an expenditure category in the Nkwa Real Estate Expenditure Management System.
 * Categories are used to classify and organize expenditures for reporting, analysis, and
 * budgeting purposes. Each category has a unique identifier, name, description, and color
 * for visual distinction in reports and user interfaces.
 * 
 * Categories enable:
 * - Expenditure classification and organization
 * - Budget tracking by category
 * - Analytical reporting and cost breakdowns
 * - Visual distinction through color coding
 * - Hierarchical cost analysis
 * 
 * The class implements proper equality semantics based on category names for
 * effective use in data structures and uniqueness validation.
 * 
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app.modules;

/**
 * Category represents a classification for expenditures in the system.
 * 
 * This class manages category information including:
 * - Unique identification for system tracking
 * - Human-readable name for user interaction
 * - Detailed description for clarification
 * - Color coding for visual distinction
 * - Validation for data integrity
 * 
 * Categories are used throughout the system for:
 * - Expenditure classification during data entry
 * - Reporting and analytical breakdowns
 * - Budget tracking and cost analysis
 * - Visual organization in user interfaces
 * 
 * The class implements equality based on category names (case-insensitive)
 * to ensure uniqueness in the system while allowing for proper data structure usage.
 */
public class Category {
    /** Unique identifier for the category */
    private String id;
    
    /** Human-readable name of the category */
    private String name;
    
    /** Detailed description of what this category encompasses */
    private String description;
    
    /** Color code for visual distinction (e.g., "red", "blue", "#FF0000") */
    private String color;

    /**
     * Creates a new Category with all required information.
     * 
     * @param id Unique identifier for the category
     * @param name Human-readable name of the category
     * @param description Detailed description of the category's purpose
     * @param color Color code for visual representation
     */
    public Category(String id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    // Getter methods with documentation
    
    /** @return The unique category identifier */
    public String getId() { return id; }
    
    /** @return The category name */
    public String getName() { return name; }
    
    /** @return The category description */
    public String getDescription() { return description; }
    
    /** @return The category color code */
    public String getColor() { return color; }

    // Setter methods with documentation
    
    /** @param id The new category ID to set */
    public void setId(String id) { this.id = id; }
    
    /** @param name The new category name to set */
    public void setName(String name) { this.name = name; }
    
    /** @param description The new description to set */
    public void setDescription(String description) { this.description = description; }
    
    /** @param color The new color code to set */
    public void setColor(String color) { this.color = color; }

    /**
     * Validates that this category has all required fields populated.
     * 
     * Checks that all fields (id, name, description, color) are not null
     * and not empty after trimming whitespace.
     * 
     * @return true if the category is valid, false otherwise
     */
    public boolean isValid() {
        return id != null && !id.trim().isEmpty()
            && name != null && !name.trim().isEmpty()
            && description != null && !description.trim().isEmpty()
            && color != null && !color.trim().isEmpty();
    }

    /**
     * Returns a string representation of the category.
     * 
     * @return A formatted string containing all category information
     */
    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    /**
     * Determines equality based on category names (case-insensitive).
     * 
     * Two categories are considered equal if they have the same name,
     * regardless of case. This ensures category uniqueness by name
     * throughout the system.
     * 
     * @param obj The object to compare with
     * @return true if the categories have the same name (case-insensitive)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category)) return false;
        Category other = (Category) obj;
        return name.equalsIgnoreCase(other.name);
    }

    /**
     * Returns a hash code based on the lowercase category name.
     * 
     * This ensures that categories with the same name (case-insensitive)
     * have the same hash code, which is required for proper behavior
     * when using categories in hash-based data structures.
     * 
     * @return The hash code of the lowercase category name
     */
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}