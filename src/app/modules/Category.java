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
     * Get category description
     * @return category description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get category color
     * @return category color
     */
    public String getColor() {
        return color;
    }

    /**
     * Set category ID
     * @param id category ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set category name
     * @param name category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set category description
     * @param description category description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set category color
     * @param color category color
     */
    public void setColor(String color) {
        this.color = color;
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