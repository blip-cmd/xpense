package app.modules;

import java.io.*;
import app.modules.*;
import app.util.*;

public class ReceiptHandler {
    private static final String RECEIPT_COUNTER_FILE = "receipts.txt";

    public static int loadLastReceiptNumber() {

        // File file = new File(RECEIPT_COUNTER_FILE); // This creates the file incase
        // it is missing, still contemplating if I should indeed ad it.
        // if (!file.exists()) {
        // saveLastReceiptNumber(1);
        // return 1;
        // }

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
