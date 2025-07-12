package app.modules;

import java.time.LocalDateTime;

/**
 * Represents a receipt linked to an expenditure
 */
public class Receipt {
    private String receiptId;
    private String expenseCode;
    private String filePath;
    private LocalDateTime timestamp;
    
    /**
     * Default constructor
     */
    public Receipt() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Constructor with all fields
     */
    public Receipt(String receiptId, String expenseCode, String filePath, LocalDateTime timestamp) {
        this.receiptId = receiptId;
        this.expenseCode = expenseCode;
        this.filePath = filePath;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }
    
    // Getters
    public String getReceiptId() {
        return receiptId;
    }
    
    public String getExpenseCode() {
        return expenseCode;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    // Setters
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    
    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Receipt{" +
                "receiptId='" + receiptId + '\'' +
                ", expenseCode='" + expenseCode + '\'' +
                ", filePath='" + filePath + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}