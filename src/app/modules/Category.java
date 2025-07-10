package app.modules;

// import java.math.BigDecimal;

/**
 * Represents a category for organizing expenditures
 * TODO: Implement the complete Category class
 */
public class Category {
    // TODO: Add fields for category data
    private String id;
    private String name;
    private String description;
    private String color; // for UI purposes
    
    /**
     * Constructor for Category
     * TODO: Implement constructor with proper parameters
     */
    public Category(String id, String name, String description, String color) {
        // TODO: Initialize category fields
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }
    
    /**
     * Get category ID
     * @return category ID
     */
    public String getId() {
        
        return id;
    }
    
    /**
     
     * @return category name
     */
    public String getName() {
        // TODO: Return actual name
        return name;
    }


    /**
     
     * @return category name
     */
    public String getDescription() {
        // TODO: Return actual name
        return description;
    }

    /**
     * Get category name
     * TODO: Implement name retrieval
     * @return category name
     */
    public String getColor() {
        // TODO: Return actual name
        return color;
    }
    
    /**
     * Validate category data
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        // TODO: Implement validation
        return id != null && !id.isBlank()
            && description != null && !description.isBlank()
            && name != null && name.isBlank()
            && color != null;
    }
    
    // Setters for each field
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
    
    // TODO: Add more methods as needed
    
}