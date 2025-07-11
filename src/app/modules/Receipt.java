import java.io.*;
import app.util.*;

public class Receipt {
    private SimpleQueue<String> receiptTextQueue = new SimpleQueue<>();
    private SimpleStack<Expenditure> reviewStack = new SimpleStack<>();

    public static final String RECEIPT_FILE = "receipts.txt";

    public void enqueueReceipt(Expenditure e, String formattedReceipt) {
        if (e.getReceiptInfo() == null || e.getReceiptInfo().isEmpty()) {
            System.out.println("No receipt attached to expenditure");
            return;
        }
        receiptTextQueue.offer(formattedReceipt);
        reviewStack.push(e);
        System.out.println("Receipt enqueued for saving: " + e.getCode());
    }

    public void processNextReceipt() {
        if (receiptTextQueue.isEmpty()) {
            System.out.println("No receipts pending.");
            return;
        }
        String receipt = receiptTextQueue.poll();
        System.out.println("Reviewing receipt:\n" + receipt);
    }

    public void reviewHistory() {
        if (reviewStack.isEmpty()) {
            System.out.println("No reviewed receipts.");
            return;
        }
        System.out.println("Reviewed Receipts:");
        Object[] arr = reviewStack.toArray();
        for (Object o : arr) {
            Expenditure e = (Expenditure) o;
            System.out.println(" - " + e.getCode() + ": " + e.getReceiptInfo());
        }
    }

    public boolean hasPending() {
        return !receiptTextQueue.isEmpty();
    }

    public void clear() {
        receiptTextQueue.clear();
        reviewStack.clear();
    }

    public void saveReviewLog(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            while (!receiptTextQueue.isEmpty()) {
                writer.println(receiptTextQueue.poll());
            }
            System.out.println("Full receipt(s) saved.");
        } catch (IOException ex) {
            System.out.println("Could not save receipt log.");
        }
    }

    public void loadReviewLog(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    Expenditure e = new Expenditure(parts[0], null, 0, null, null, null, null, parts[1]);
                    reviewStack.push(e);
                }
            }
        } catch (FileNotFoundException e) {
            // No receipt history found. Starting fresh.
        } catch (IOException ex) {
            System.out.println("Error loading receipt history.");
        }
    }
}