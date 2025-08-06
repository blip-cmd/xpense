package app.modules;

import app.util.*;

public class ReceiptHandler {
    private final SimpleArrayList<Receipt> receipts;
    private final SimpleQueue<Receipt> processingQueue;
    private final SimpleStack<Receipt> processedStack;

    public ReceiptHandler() {
        this.receipts = new SimpleArrayList<>();
        this.processingQueue = new SimpleQueue<>();
        this.processedStack = new SimpleStack<>();
    }

    public void addReceipt(Receipt receipt) {
        if (receipt != null) {
            receipts.add(receipt);
            processingQueue.offer(receipt);
        }
    }

    /**
     * Processes the next receipt from the processing queue.
     * Moves the receipt from the queue to the processed stack.
     * 
     * @return The receipt that was processed, or null if queue is empty
     */
    public Receipt processNextReceipt() {
        Receipt receipt = processingQueue.poll();
        if (receipt != null) {
            processedStack.push(receipt);
        }
        return receipt;
    }

    /**
     * Gets the most recently processed receipt without removing it from the stack.
     * 
     * @return The most recently processed receipt, or null if none processed
     */
    public Receipt getLastProcessedReceipt() {
        return processedStack.peek();
    }

    /**
     * Undoes the last receipt processing by moving it back to the processing queue.
     * 
     * @return The receipt that was moved back to processing, or null if no processed receipts
     */
    public Receipt undoLastProcessing() {
        Receipt receipt = processedStack.pop();
        if (receipt != null) {
            processingQueue.offer(receipt);
        }
        return receipt;
    }

    /**
     * Gets the number of receipts pending processing.
     * 
     * @return Number of receipts in the processing queue
     */
    public int getPendingReceiptsCount() {
        return processingQueue.size();
    }

    /**
     * Gets the number of processed receipts.
     * 
     * @return Number of receipts in the processed stack
     */
    public int getProcessedReceiptsCount() {
        return processedStack.size();
    }

    /**
     * Checks if there are any receipts pending processing.
     * 
     * @return true if processing queue is empty, false otherwise
     */
    public boolean isProcessingQueueEmpty() {
        return processingQueue.isEmpty();
    }

    /**
     * Checks if any receipts have been processed.
     * 
     * @return true if processed stack is empty, false otherwise
     */
    public boolean isProcessedStackEmpty() {
        return processedStack.isEmpty();
    }

    /**
     * Clears all processed receipts from the stack.
     * This is useful for batch operations or system cleanup.
     */
    public void clearProcessedReceipts() {
        processedStack.clear();
    }

    public SimpleArrayList<Receipt> getAllReceipts() { return receipts; }
}