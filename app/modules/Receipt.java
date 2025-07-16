package app.modules;

import java.time.LocalDateTime;

public class Receipt {
    private String receiptId;
    private String expenseCode;
    private String filePath;
    private LocalDateTime timestamp;

    public Receipt() {
        this.timestamp = LocalDateTime.now();
    }

    public Receipt(String receiptId, String expenseCode, String filePath, LocalDateTime timestamp) {
        this.receiptId = receiptId;
        this.expenseCode = expenseCode;
        this.filePath = filePath;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public String getReceiptId() { return receiptId; }
    public String getExpenseCode() { return expenseCode; }
    public String getFilePath() { return filePath; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }
    public void setExpenseCode(String expenseCode) { this.expenseCode = expenseCode; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

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