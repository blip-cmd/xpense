package app.modules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Expenditure {
    private String id;
    private String description;
    private BigDecimal amount;
    private Category category;
    private LocalDateTime dateTime;
    private String phase;
    private String bankAccountId;
    private String receiptInfo;

    public Expenditure(String id, String description, BigDecimal amount, Category category, LocalDateTime dateTime, String phase) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.dateTime = dateTime;
        this.phase = phase;
        this.bankAccountId = null;
        this.receiptInfo = null;
    }

    public Expenditure(String id, String description, BigDecimal amount, Category category, LocalDateTime dateTime, String phase, String bankAccountId) {
        this(id, description, amount, category, dateTime, phase);
        this.bankAccountId = bankAccountId;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public Category getCategory() { return category; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getPhase() { return phase; }
    public String getBankAccountId() { return bankAccountId; }
    public String getReceiptInfo() { return receiptInfo; }
    public void setId(String id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCategory(Category category) { this.category = category; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setPhase(String phase) { this.phase = phase; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }
    public void setReceiptInfo(String receiptInfo) { this.receiptInfo = receiptInfo; }

    public boolean isValid() {
        return id != null && !id.isBlank()
            && description != null && !description.isBlank()
            && amount != null && amount.compareTo(BigDecimal.ZERO) > 0
            && category != null && dateTime != null;
    }

    @Override
    public String toString() {
        return String.format("Expenditure{id='%s', desc='%s', amount='%s', category='%s', date='%s', phase='%s', account='%s'}",
                id, description, amount, (category != null ? category.getName() : "N/A"), dateTime, phase, bankAccountId);
    }
}