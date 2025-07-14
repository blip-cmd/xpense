package app.modules;

import java.io.*;  // please limit to only the classes used
import java.util.*;  // please limit to only the classes used
import app.util.*;  // please limit to only the classes used

public class ReceiptHandler {
    private static final String RECEIPT_COUNTER_FILE = "receipts.txt";
    private List<Receipt> receipts;
    private SimpleQueue<Receipt> processingQueue;
    private SimpleStack<Receipt> processedStack;
    
    public ReceiptHandler() {
        this.receipts = new ArrayList<>();
        this.processingQueue = new SimpleQueue<>();
        this.processedStack = new SimpleStack<>();
    }
    
    public void addReceipt(Receipt receipt) {
        if (receipt != null) {
            receipts.add(receipt);
            processingQueue.offer(receipt);
        }
    }
    
    public List<Receipt> getAllReceipts() {
        return new ArrayList<>(receipts);
    }
    
    public Receipt getNextReceiptForProcessing() {
        return processingQueue.poll();
    }
    
    public void markReceiptProcessed(String receiptId) {
        Receipt receipt = findReceiptById(receiptId);
        if (receipt != null) {
            processedStack.push(receipt);
        }
    }
    
    public void linkReceiptToExpenditure(String receiptId, String expenditureId) {
        Receipt receipt = findReceiptById(receiptId);
        if (receipt != null) {
            receipt.setExpenseCode(expenditureId);
        }
    }
    
    public List<Receipt> getReceiptHistory() {
        List<Receipt> history = new ArrayList<>();
        Object[] processed = processedStack.toArray();
        for (Object obj : processed) {
            if (obj instanceof Receipt) {
                history.add((Receipt) obj);
            }
        }
        return history;
    }
    
    private Receipt findReceiptById(String receiptId) {
        return receipts.stream()
                .filter(r -> receiptId.equals(r.getReceiptId()))
                .findFirst()
                .orElse(null);
    }

    public static int loadLastReceiptNumber() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RECEIPT_COUNTER_FILE))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            // return 1; // Start from RCT0001 if file is missing or unreadable
        }
        return 1;
    }

    public static void saveLastReceiptNumber(int counter) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RECEIPT_COUNTER_FILE))) {
            writer.println(counter);
        } catch (IOException e) {
            System.out.println("Failed to save receipt counter.");
        }
    }
}
