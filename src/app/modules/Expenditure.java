package app.modules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an expenditure/expense in the system
 */
public class Expenditure {
    private String id;
    private String description;
    private BigDecimal amount;
    private Category category;
    private LocalDateTime dateTime;
    private String location;
    
    /**
     * Constructor for Expenditure

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
     * @return expenditure ID
     */
    public String getId() {
        return id;
    }
    
    /**
   
     * @return expenditure amount
     */
    public BigDecimal getAmount() {
        // Done
        return amount;
    }
    
    /**
     * Get expenditure description
     * @return expenditure description
     */
    public String getDescription() {
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
     * @return true if valid, false otherwise
     */
    public boolean isValid() {

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

 

    
    // Add more methods
    public String get_summary() {
        return String.format("Expenditure ID: %s, Description: %s, Amount: %s, Category: %s, Date: %s, Location: %s",
                id, description, amount.toString(), (category != null ? category.getName() : "N/A"),
                dateTime.toString(), location);
    }
    


}
