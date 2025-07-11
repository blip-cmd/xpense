package app.modules;

// import java.math.BigDecimal;

/**
 * Represents a category for organizing expenditures
 * TODO: Implement the complete Category class
 */
public class Category {

    private String id;
    private String name;
    private String description;
    private String color; // for UI purposes
    
    

     public Category(String id, String name, String description, String color) {

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
     * Get category name
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Validate category data
     * @return true if valid, false otherwise
     */
    public boolean isValid() {

        return id != null && !id.trim().isEmpty() &&
               name != null && !name.trim().isEmpty() &&
               description != null && !description.trim().isEmpty() &&
               color != null && !color.trim().isEmpty();
    }
    
    // done
    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}