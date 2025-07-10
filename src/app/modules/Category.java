package app.modules;

/**
 * Represents a category for organizing expenditures.
 * Each category has a unique name, optional description, and color for UI.
 */
public class Category {
    private String name;
    private String description;
    private String color;

    /**
     * Constructs a Category with the required name.
     * @param name The unique name of the category.
     */
    public Category(String name) {
        this(name, "", "");
    }

    /**
     * Constructs a Category with name, description, and color.
     * @param name The unique name of the category.
     * @param description A description of the category.
     * @param color A color label for UI (optional).
     */
    public Category(String name, String description, String color) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }
        this.name = name.trim();
        this.description = description == null ? "" : description.trim();
        this.color = color == null ? "" : color.trim();
    }

    /**
     * Get the category name (unique identifier).
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the category description.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the category color.
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Validate category data.
     * @return true if the category name is not empty, false otherwise
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Two categories are equal if their names are equal (case-insensitive).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category)) return false;
        Category other = (Category) obj;
        return this.name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    /**
     * String representation for debugging.
     */
    @Override
    public String toString() {
        return String.format("Category{name='%s', description='%s', color='%s'}", name, description, color);
    }
}