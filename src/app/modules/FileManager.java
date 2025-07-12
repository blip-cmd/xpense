package app.modules;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;

/**
 * FileManager handles all file I/O for data persistence in the Xpense system.
 * - Loads and saves expenditures, categories, accounts, and receipts.
 * - Uses efficient data structures (ArrayList, BufferedReader/Writer).
 * - Provides backup and restore functionality.
 * - Handles file format validation and error recovery.
 * - Integrates with all modules via simple, consistent methods.
 */
public class FileManager {
    // Define where the data and backup files will be stored
    private final String dataDir = "src/data/";
    private final String backupDir = "src/data/backup/";

    //Ensures backup directory exists when the app runs.
    public FileManager() {
        File backup = new File(backupDir);
        if (!backup.exists()) {
            backup.mkdirs();
        }
    }

    /**
     * Load expenditures from file (expenditures.txt)
     * Each line has: code|amount|date|phase|category|accountId
     * Returns a list of Expenditure objects.
     */
    public List<Expenditure> loadExpenditures(String filename) {
        List<Expenditure> expenditures = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 6) continue; // skip invalid lines
                try {
                    // Parse fields
                    String code = parts[0];
                    String description = parts[1];
                    BigDecimal amount = new BigDecimal(parts[2]);
                    // Create a default category for now
                    Category category = new Category("DEFAULT", parts[4], "Default category", "blue");
                    LocalDateTime dateTime = LocalDateTime.parse(parts[3]);
                    String location = "Unknown";
                    
                    Expenditure exp = new Expenditure(code, description, amount, category, dateTime, location);
                    exp.setBankAccountId(parts[5]);
                    expenditures.add(exp);
                } catch (Exception e) {
                    // Skip malformed lines
                }
            }
        } catch (IOException e) {
            // File not found or read error: return empty list
        }
        return expenditures;
    }

    /**
     * Save expenditures to file (expenditures.txt)
     * Each Expenditure is serialized as: code|description|amount|dateTime|category|bankAccountId
     * Returns true if successful.
     */
    public boolean saveExpenditures(List<Expenditure> expenditures, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (Expenditure exp : expenditures) {
                String line = String.join("|",
                        exp.getCode(),
                        exp.getDescription(),
                        exp.getAmount().toString(),
                        exp.getDateTime().toString(),
                        exp.getCategory().getName(),
                        exp.getBankAccountId() != null ? exp.getBankAccountId() : ""
                );
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Load categories from file (categories.txt)
     * Each line: name|description|color
     * Returns a list of Category objects.
     */
    public List<Category> loadCategories(String filename) {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 3) continue;
                try {
                    String name = parts[0];
                    String description = parts[1];
                    String color = parts[2];
                    categories.add(new Category("CAT" + System.currentTimeMillis(), name, description, color));
                } catch (Exception e) {
                    // Skip malformed lines
                }
            }
        } catch (IOException e) {
            // File not found or read error: return empty list
        }
        return categories;
    }

    /**
     * Save categories to file (categories.txt)
     * Each Category is serialized as: name|description|color
     * Returns true if successful.
     */
    public boolean saveCategories(List<Category> categories, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (Category cat : categories) {
                String line = String.join("|", cat.getName(), cat.getDescription(), cat.getColor());
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Load bank accounts from file (accounts.txt)
     * Each line: accountId|accountName|balance
     * Returns a list of BankAccount objects.
     */
    public List<BankAccount> loadAccounts(String filename) {
        List<BankAccount> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip comment lines
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length != 3) continue;
                try {
                    String id = parts[0];
                    String name = parts[1];
                    BigDecimal balance = new BigDecimal(parts[2]);
                    
                    // Create account with proper constructor (id, name)
                    BankAccount account = new BankAccount(id, name);
                    // Set balance after creation
                    if (balance.compareTo(BigDecimal.ZERO) > 0) {
                        account.credit(balance);
                    }
                    accounts.add(account);
                } catch (Exception e) {
                    // Skip malformed lines
                }
            }
        } catch (IOException e) {
            // File not found or read error: return empty list
        }
        return accounts;
    }
    
    /**
     * Load bank accounts from file (alias for loadAccounts for compatibility)
     */
    public List<BankAccount> loadBankAccounts(String filename) {
        return loadAccounts(filename);
    }

    /**
     * Save bank accounts to file (accounts.txt)
     * Each BankAccount is serialized as: accountId|accountName|balance
     * Returns true if successful.
     */
    public boolean saveAccounts(List<BankAccount> accounts, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (BankAccount acc : accounts) {
                String line = String.join("|", 
                    acc.getAccountNumber(), 
                    acc.getAccountName(), 
                    acc.getBalance().toString());
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Load receipts from file (receipts.txt)
     * Each line: receiptId|expenseCode|filePath|timestamp
     * Returns a list of Receipt objects.
     */
    public List<Receipt> loadReceipts(String filename) {
        List<Receipt> receipts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataDir + filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 4) continue;
                try {
                    String receiptId = parts[0];
                    String expenseCode = parts[1];
                    String filePath = parts[2];
                    String timestampStr = parts[3];
                    LocalDateTime timestamp = LocalDateTime.parse(timestampStr);
                    receipts.add(new Receipt(receiptId, expenseCode, filePath, timestamp));
                } catch (Exception e) {
                    // Skip malformed lines
                }
            }
        } catch (IOException e) {
            // File not found or read error: return empty list
        }
        return receipts;
    }

    /**
     * Save receipts to file (receipts.txt)
     * Each Receipt is serialized as: receiptId|expenseCode|filePath|timestamp
     * Returns true if successful.
     */
    public boolean saveReceipts(List<Receipt> receipts, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataDir + filename))) {
            for (Receipt r : receipts) {
                String line = String.join("|", r.getReceiptId(), r.getExpenseCode(), r.getFilePath(), r.getTimestamp().toString());
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Create backup of all data files (simple copy to backup directory)
     * Returns true if all files backed up successfully.
     */
    public boolean createBackup() {
        String[] files = {"expenditures.txt", "categories.txt", "accounts.txt", "receipts.txt"};
        boolean allSuccess = true;
        for (String file : files) {
            File src = new File(dataDir + file);
            File dest = new File(backupDir + file + ".bak");
            try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dest)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    /**
     * Restore all data files from backup
     * Returns true if all files restored successfully.
     */
    public boolean restoreFromBackup() {
        String[] files = {"expenditures.txt", "categories.txt", "accounts.txt", "receipts.txt"};
        boolean allSuccess = true;
        for (String file : files) {
            File src = new File(backupDir + file + ".bak");
            File dest = new File(dataDir + file);
            try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dest)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }
}
