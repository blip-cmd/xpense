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

    public SimpleArrayList<Receipt> getAllReceipts() { return receipts; }
}