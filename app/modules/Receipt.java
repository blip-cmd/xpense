/**
 * Receipt.java
 * 
 * Represents a receipt or invoice entity in the Nkwa Real Estate Expenditure Management System.
 * This class manages receipt information that can be linked to expenditures for documentation
 * and audit purposes. Receipts provide evidence and documentation for financial transactions.
 * 
 * The Receipt class supports:
 * - Unique receipt identification for tracking
 * - Linkage to specific expenditures through expense codes
 * - File path storage for physical/digital receipt documents
 * - Automatic timestamping for audit trails
 * - Document management and organization
 * 
 * Receipts are managed by the ReceiptHandler and can be processed through
 * queue and stack data structures for organized workflow management.
 * 
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app.modules;

import java.time.LocalDateTime;

/**
 * Receipt represents a receipt or invoice document linked to an expenditure.
 * 
 * This class manages:
 * - Unique receipt identification for system tracking
 * - Association with expenditures through expense codes
 * - File path storage for document location
 * - Timestamp tracking for audit and organization
 * 
 * Receipts serve multiple purposes:
 * - Legal documentation for tax and audit purposes
 * - Verification of expenditure amounts and details
 * - Support for expense reporting and reimbursement
 * - Evidence for financial reconciliation
 * 
 * The class automatically handles timestamp generation when receipts
 * are created without explicit timing information.
 */
public class Receipt {
    /** Unique identifier for the receipt */
    private String receiptId;
    
    /** Expenditure code this receipt is associated with */
    private String expenseCode;
    
    /** File path to the receipt document (image, PDF, etc.) */
    private String filePath;
    
    /** Timestamp when the receipt was created or processed */
    private LocalDateTime timestamp;

    /**
     * Creates a new empty Receipt with current timestamp.
     * 
     * This constructor is useful for creating receipt objects that
     * will be populated through setter methods.
     */
    public Receipt() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Creates a new Receipt with all information specified.
     * 
     * @param receiptId Unique identifier for the receipt
     * @param expenseCode The expenditure code this receipt documents
     * @param filePath Path to the receipt file/document
     * @param timestamp When the receipt was created (uses current time if null)
     */
    public Receipt(String receiptId, String expenseCode, String filePath, LocalDateTime timestamp) {
        this.receiptId = receiptId;
        this.expenseCode = expenseCode;
        this.filePath = filePath;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    // Getter methods with documentation
    
    /** @return The unique receipt identifier */
    public String getReceiptId() { return receiptId; }
    
    /** @return The expenditure code this receipt is linked to */
    public String getExpenseCode() { return expenseCode; }
    
    /** @return The file path to the receipt document */
    public String getFilePath() { return filePath; }
    
    /** @return The timestamp when the receipt was processed */
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Setter methods with documentation
    
    /** @param receiptId The receipt identifier to set */
    public void setReceiptId(String receiptId) { this.receiptId = receiptId; }
    
    /** @param expenseCode The expenditure code to associate with */
    public void setExpenseCode(String expenseCode) { this.expenseCode = expenseCode; }
    
    /** @param filePath The file path to set */
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    /** @param timestamp The timestamp to set */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    /**
     * Returns a string representation of the receipt.
     * 
     * @return A formatted string containing all receipt information
     */
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