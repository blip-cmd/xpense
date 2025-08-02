package app.modules;

import app.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FileManager {
    private final String dataDir = "";

    public FileManager() {}

    public SimpleArrayList<Expenditure> loadExpenditures(String filename) {
        SimpleArrayList<Expenditure> expenditures = new SimpleArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue; // Need at least 5 fields for basic expenditure
                
                String code = parts[0];
                String description = parts[1];
                BigDecimal amount = new BigDecimal(parts[2]);
                LocalDateTime dateTime = LocalDateTime.parse(parts[3]);
                
                // Handle different file formats for backward compatibility
                String categoryName, bankAccountId, phase = "active";
                String receiptInfo = null;
                
                if (parts.length == 6) {
                    // Old format: ID|description|amount|datetime|categoryName|bankAccountId
                    categoryName = parts[4];
                    bankAccountId = parts[5];
                } else if (parts.length >= 7) {
                    // New format: ID|description|amount|datetime|phase|categoryName|bankAccountId|receiptInfo
                    phase = parts[4];
                    categoryName = parts[5];
                    bankAccountId = parts[6];
                    if (parts.length >= 8) {
                        receiptInfo = parts[7].trim().isEmpty() ? null : parts[7];
                    }
                } else {
                    // Incomplete data, skip this line
                    continue;
                }
                
                // Create a temporary category - the actual category will be resolved later
                Category category = new Category("TEMP_" + categoryName, categoryName, "Loaded category", "blue");
                
                Expenditure exp = new Expenditure(code, description, amount, category, dateTime, phase, bankAccountId);
                if (receiptInfo != null) {
                    exp.setReceiptInfo(receiptInfo);
                }
                expenditures.add(exp);
            }
        } catch (IOException e) {}
        return expenditures;
    }

    public boolean saveExpenditures(SimpleArrayList<Expenditure> expenditures, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (int i = 0; i < expenditures.size(); i++) {
                Expenditure exp = expenditures.get(i);
                String line = String.join("|",
                        exp.getId(),
                        exp.getDescription(),
                        exp.getAmount().toString(),
                        exp.getDateTime().toString(),
                        exp.getPhase() != null ? exp.getPhase() : "active", // Include phase field
                        exp.getCategory().getName(),
                        exp.getBankAccountId() != null ? exp.getBankAccountId() : "",
                        exp.getReceiptInfo() != null ? exp.getReceiptInfo() : ""
                );
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    public SimpleArrayList<Category> loadCategories(String filename) {
        SimpleArrayList<Category> categories = new SimpleArrayList<>();
        int categoryCounter = 1000;
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;
                categories.add(new Category("CAT" + String.format("%04d", categoryCounter++), parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {}
        return categories;
    }

    public boolean saveCategories(SimpleArrayList<Category> categories, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                String line = String.join("|", cat.getName(), cat.getDescription(), cat.getColor());
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    public SimpleArrayList<BankAccount> loadAccounts(String filename) {
        SimpleArrayList<BankAccount> accounts = new SimpleArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;
                String id = parts[0];
                String name = parts[1];
                BigDecimal balance = new BigDecimal(parts[2]);
                BankAccount account = new BankAccount(id, name, balance);
                accounts.add(account);
            }
        } catch (IOException e) {}
        return accounts;
    }

    public boolean saveAccounts(SimpleArrayList<BankAccount> accounts, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (int i = 0; i < accounts.size(); i++) {
                BankAccount acc = accounts.get(i);
                String line = String.join("|", acc.getAccountNumber(), acc.getAccountName(), acc.getBalance().toString());
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }

    public SimpleArrayList<Receipt> loadReceipts(String filename) {
        SimpleArrayList<Receipt> receipts = new SimpleArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;
                String receiptId = parts[0];
                String expenseCode = parts[1];
                String filePath = parts[2];
                String timestampStr = parts[3];
                receipts.add(new Receipt(receiptId, expenseCode, filePath, LocalDateTime.parse(timestampStr)));
            }
        } catch (IOException e) {}
        return receipts;
    }

    public boolean saveReceipts(SimpleArrayList<Receipt> receipts, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (int i = 0; i < receipts.size(); i++) {
                Receipt r = receipts.get(i);
                String line = String.join("|", r.getReceiptId(), r.getExpenseCode(), r.getFilePath(), r.getTimestamp().toString());
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) { return false; }
    }
}