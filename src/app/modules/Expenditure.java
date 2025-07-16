package app.modules;

import java.time.LocalDateTime;

/**
 * Represents an expenditure/expense in the system
 */
public class Expenditure {
    private String id;
    private String description;
    private Float amount;  // Changed from BigDecimal to Float
    private Category category;
    private LocalDateTime dateTime;
    private String location;
    private String phase;
    private String bankAccountId;
    private String receiptInfo;

    /**
     * Constructor with all fields
     */
    public Expenditure(String id, String description, Float amount, Category category,
                       LocalDateTime dateTime, String location, String phase, String bankAccountId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateTime = dateTime;
        this.location = location;
        this.phase = phase;
        this.bankAccountId = bankAccountId;
        this.receiptInfo = null;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return id;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public String getMonth() {
        if (dateTime == null) return "1970-01";
        return String.format("%04d-%02d", dateTime.getYear(), dateTime.getMonthValue());
    }

    public String getPhase() {
        return phase;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public String getReceiptInfo() {
        return receiptInfo;
    }

    public Float getAmount() {
        return amount;
    }

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

    public boolean isValid() {
        return id != null && !id.isBlank()
            && description != null && !description.isBlank()
            && amount != null && amount > 0
            && category != null
            && dateTime != null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Float amount) {
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

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void setReceiptInfo(String receiptInfo) {
        this.receiptInfo = receiptInfo;
    }

    public String get_summary() {
        return String.format("Expenditure ID: %s, Description: %s, Amount: %.2f, Category: %s, Date: %s, Location: %s",
                id, description, amount,
                (category != null ? category.getName() : "N/A"),
                dateTime.toString(), location);
    }
}
