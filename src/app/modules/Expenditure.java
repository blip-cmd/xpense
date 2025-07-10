package app.modules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an expenditure/expense in the system
 * TODO: Implement the complete Expenditure class
 */
public class Expenditure {
    // TODO: Add fields for expenditure data
    private String id;
    private String description;
    private BigDecimal amount;
    private Category category;
    private LocalDateTime dateTime;
    private String location;
    
    /**
     * Constructor for Expenditure
     * TODO: Implement constructor with proper parameters
     */
    public Expenditure(String id, String description, BigDecimal amount, Category category,
                       LocalDateTime dateTime, String location) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateTime = dateTime;
        this.location = location;
    }
    
    /**
     * Get expenditure ID
     * TODO: Implement ID retrieval
     * @return expenditure ID
     */
    public String getId() {
        // TODO: Return actual ID
        return id;
    }
    
    /**
     * Get expenditure amount
     * TODO: Implement amount retrieval
     * @return expenditure amount
     */
    public BigDecimal getAmount() {
        // TODO: Return actual amount
        return amount;
    }
    
    /**
     * Get expenditure description
     * TODO: Implement description retrieval
     * @return expenditure description
     */
    public String getDescription() {
        // TODO: Return actual description
        return description;
    }

    
    public Category getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }
    /**
     * Validate expenditure data
     * TODO: Implement validation logic
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        // TODO: Implement validation

        return id != null && !id.isBlank()
            && description != null && !description.isBlank()
            && amount != null && amount.compareTo(BigDecimal.ZERO) > 0
            && category != null
            && dateTime != null;
    }
    
    // Setters for each field
    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Expenditure{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", category=" + (category != null ? category.getName() : "null") +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expenditure that = (Expenditure) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    // TODO: Add more methods as needed
}
