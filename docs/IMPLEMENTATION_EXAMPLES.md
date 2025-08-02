# Xpense Data Structures - Implementation Examples

## Overview

This document provides practical code examples showing how the custom data structures are implemented and used throughout the Xpense application. Each example demonstrates real business logic powered by the custom data structures.

## Core Data Structure Examples

### 1. SimpleArrayList - Dynamic Storage Foundation

#### **Implementation Highlight: Auto-Resizing**
```java
// From SimpleArrayList.java
private void ensureCapacity() {
    if (size >= elements.length) {
        Object[] bigger = new Object[elements.length * 2];  // Double capacity
        System.arraycopy(elements, 0, bigger, 0, elements.length);
        elements = bigger;
    }
}

public boolean add(T element) {
    ensureCapacity();           // Auto-resize if needed
    elements[size++] = element; // Add and increment size
    return true;
}
```

#### **Real Usage: Expenditure Storage**
```java
// From ExpenditureManager.java
public class ExpenditureManager {
    private final SimpleArrayList<Expenditure> expenditures;
    
    public boolean addExpenditure(Expenditure expenditure) {
        // Validation logic...
        expenditures.add(expenditure);  // Uses SimpleArrayList.add()
        return true;
    }
    
    public SimpleArrayList<Expenditure> getAllExpenditures() {
        return expenditures;  // Direct access to underlying structure
    }
}
```

#### **Performance Impact**
- First 10 expenditures: No resizing overhead
- 11th expenditure: O(n) resize operation, capacity becomes 20
- 21st expenditure: O(n) resize operation, capacity becomes 40
- Average case: O(1) amortized insertion time

### 2. SimpleMap - Key-Value Business Logic

#### **Implementation Highlight: Parallel Array Strategy**
```java
// From SimpleMap.java
public class SimpleMap<K, V> {
    private final SimpleArrayList<K> keys;
    private final SimpleArrayList<V> values;
    
    public void put(K key, V value) {
        int idx = indexOf(key);
        if (idx >= 0) {
            // Complex update logic for existing keys (lines 13-35)
            // Must maintain synchronization between parallel arrays
        } else {
            keys.add(key);      // Add to keys array
            values.add(value);  // Add to values array at same index
        }
    }
    
    public V get(K key) {
        int idx = indexOf(key);                    // O(n) linear search
        if (idx >= 0) return values.get(idx);     // Corresponding value
        return null;
    }
}
```

#### **Real Usage: Bank Account Management**
```java
// From BankLedger.java
public class BankLedger {
    private final SimpleMap<String, BankAccount> accounts;
    
    public boolean addAccount(BankAccount account) {
        if (accounts.containsKey(account.getAccountNumber())) return false;
        accounts.put(account.getAccountNumber(), account);  // Store account
        return true;
    }
    
    public boolean logExpenditure(String accountId, BigDecimal amount, String description) {
        BankAccount account = accounts.get(accountId);  // O(n) lookup
        if (account == null) return false;
        return account.debit(amount);  // Update account balance
    }
}
```

#### **Business Impact Example**
```java
// Adding 3 bank accounts
ledger.addAccount(new BankAccount("ACC001", "Main Operations", 5000.00));
ledger.addAccount(new BankAccount("ACC002", "Marketing", 2500.00));
ledger.addAccount(new BankAccount("ACC003", "Sales", 1000.00));

// Internal structure after additions:
// keys:   ["ACC001", "ACC002", "ACC003"]
// values: [BankAccount1, BankAccount2, BankAccount3]

// Expenditure logging
ledger.logExpenditure("ACC002", 500.00, "TV Advertisement");
// 1. O(n) search for "ACC002" in keys array -> finds index 1
// 2. O(1) retrieval of BankAccount2 from values[1]
// 3. Debit operation updates balance: 2500.00 -> 2000.00
```

### 3. SimpleSet - Category Uniqueness Enforcement

#### **Implementation Highlight: Check-then-Add Pattern**
```java
// From SimpleSet.java
public boolean add(T item) {
    if (!contains(item)) {    // O(n) uniqueness check
        items.add(item);      // O(1) amortized addition
        return true;
    }
    return false;             // Duplicate rejected
}

public boolean contains(T item) {
    for (T t : items) {       // Linear search through SimpleArrayList
        if (t.equals(item)) return true;
    }
    return false;
}
```

#### **Real Usage: Category Management**
```java
// From CategoryManager.java
public class CategoryManager {
    private final SimpleSet<Category> categories;
    
    public boolean addCategory(Category category) {
        if (category == null || !category.isValid()) return false;
        if (categories.contains(category)) return false;  // Duplicate check
        categories.add(category);                         // Unique addition
        return true;
    }
}

// From Category.java - Custom equality for business logic
@Override
public boolean equals(Object obj) {
    if (!(obj instanceof Category)) return false;
    Category other = (Category) obj;
    return name.equalsIgnoreCase(other.name);  // Case-insensitive uniqueness
}
```

#### **Business Scenario Example**
```java
// Attempting to add categories
CategoryManager mgr = new CategoryManager();

// First addition - success
Category cement = new Category("CAT001", "Cement", "Building material", "gray");
mgr.addCategory(cement);  // Returns true, added to set

// Duplicate attempt - rejected
Category cement2 = new Category("CAT002", "CEMENT", "Concrete material", "blue");
mgr.addCategory(cement2);  // Returns false, name "CEMENT" equals "Cement"

// Different category - success
Category marketing = new Category("CAT003", "Marketing", "Promotion costs", "red");
mgr.addCategory(marketing);  // Returns true, unique name
```

### 4. MinHeap - Priority Alert Processing

#### **Implementation Highlight: Binary Heap Algorithms**
```java
// From MinHeap.java
public void insert(T value) {
    ensureCapacity();
    heap[++size] = value;    // Add to end of heap
    siftUp(size);            // Restore heap property
}

private void siftUp(int idx) {
    while (idx > 1) {
        int parent = idx / 2;
        if (comparator.compare((T)heap[idx], (T)heap[parent]) < 0) {
            swap(idx, parent);   // Child has higher priority than parent
            idx = parent;        // Move up the tree
        } else break;            // Heap property satisfied
    }
}

public T removeMin() {
    if (size == 0) return null;
    T min = (T)heap[1];         // Root has minimum value (highest priority)
    heap[1] = heap[size--];     // Move last element to root
    siftDown(1);                // Restore heap property
    return min;
}
```

#### **Real Usage: Alert System**
```java
// From AlertSystem.java
public class AlertSystem {
    private final MinHeap<Alert> heap;
    
    public AlertSystem(double lowBalanceThreshold, double spendingLimitThreshold) {
        this.heap = new MinHeap<>(new MinHeap.PriorityComparator<Alert>() {
            public int compare(Alert a, Alert b) {
                return a.priority - b.priority;  // Lower number = higher priority
            }
        });
    }
    
    public boolean checkLowFunds(String accountId, double currentBalance) {
        if (currentBalance < lowBalanceThreshold) {
            addAlert("Account " + accountId + " is low on funds: GHc " + currentBalance, 1);
            return true;
        }
        return false;
    }
    
    public void displayAllAlerts() {
        while (hasAlerts()) {
            System.out.println("• " + getNextAlert());  // Always highest priority first
        }
    }
}
```

#### **Priority Processing Example**
```java
AlertSystem alerts = new AlertSystem(500.0, 10000.0);

// Add various alerts with different priorities
alerts.addAlert("Low balance on ACC001", 1);        // Critical
alerts.addAlert("Monthly spending high", 3);        // Warning  
alerts.addAlert("New expenditure added", 5);        // Info
alerts.addAlert("Account ACC002 overdrawn", 1);     // Critical

// Internal heap structure (conceptual):
//       Alert(priority=1)
//      /                \
//  Alert(priority=1)   Alert(priority=3)
//  /
// Alert(priority=5)

// Processing order:
// 1. "Low balance on ACC001" (priority 1)
// 2. "Account ACC002 overdrawn" (priority 1)  
// 3. "Monthly spending high" (priority 3)
// 4. "New expenditure added" (priority 5)
```

### 5. SimpleQueue & SimpleStack - Receipt Workflow

#### **Implementation Highlight: Workflow Management**
```java
// From SimpleQueue.java (FIFO)
public void offer(T value) { arr.add(value); }        // Add to end
public T poll() { return arr.remove(0); }             // Remove from front

// From SimpleStack.java (LIFO)  
public void push(T value) { arr.add(value); }         // Add to end
public T pop() { return arr.remove(arr.size() - 1); } // Remove from end
```

#### **Real Usage: Receipt Processing Workflow**
```java
// From ReceiptHandler.java
public class ReceiptHandler {
    private final SimpleQueue<Receipt> processingQueue;  // FIFO for processing
    private final SimpleStack<Receipt> processedStack;   // LIFO for review
    
    public void addReceipt(Receipt receipt) {
        if (receipt != null) {
            receipts.add(receipt);
            processingQueue.offer(receipt);  // Queue for processing
        }
    }
    
    // Conceptual processing workflow
    public void processReceiptQueue() {
        while (!processingQueue.isEmpty()) {
            Receipt receipt = processingQueue.poll();  // FIFO: first in, first out
            // ... process receipt ...
            processedStack.push(receipt);              // Stack for review
        }
    }
    
    public void reviewRecentReceipts() {
        while (!processedStack.isEmpty()) {
            Receipt recent = processedStack.pop();     // LIFO: most recent first
            // ... review receipt ...
        }
    }
}
```

#### **Workflow Example**
```java
ReceiptHandler handler = new ReceiptHandler();

// Add receipts in order
handler.addReceipt(new Receipt("RCT001", "EXP001", "receipt1.pdf"));
handler.addReceipt(new Receipt("RCT002", "EXP002", "receipt2.pdf"));  
handler.addReceipt(new Receipt("RCT003", "EXP003", "receipt3.pdf"));

// Processing Queue (FIFO): [RCT001, RCT002, RCT003]
// Process: RCT001 -> RCT002 -> RCT003

// After processing, Processed Stack (LIFO): [RCT003, RCT002, RCT001]
// Review: RCT003 -> RCT002 -> RCT001 (most recent first)
```

## Complex Integration Examples

### 1. Complete Expenditure Addition Flow

```java
public class XpenseSystem {
    public boolean addNewExpenditure(String description, BigDecimal amount, 
                                   String categoryName, String accountId) {
        // 1. Validate category exists (SimpleSet operation)
        if (!categoryManager.validateCategory(categoryName)) {
            return false;  // O(n) category lookup
        }
        
        // 2. Get category object (SimpleSet iteration)  
        Category category = null;
        for (Category cat : categoryManager.getAllCategories()) {
            if (cat.getName().equalsIgnoreCase(categoryName)) {
                category = cat;
                break;
            }
        }
        
        // 3. Create and add expenditure (SimpleArrayList operation)
        Expenditure expenditure = new Expenditure(description, amount, category, 
                                                 LocalDateTime.now(), "Construction");
        expenditure.setBankAccountId(accountId);
        
        if (!expenditureManager.addExpenditure(expenditure)) {
            return false;  // O(1) amortized addition
        }
        
        // 4. Update bank account (SimpleMap operation)
        if (!bankLedger.logExpenditure(accountId, amount, description)) {
            return false;  // O(n) account lookup + balance update
        }
        
        // 5. Check for alerts (MinHeap operation)
        BankAccount account = bankLedger.getAccount(accountId);
        if (account != null) {
            alertSystem.checkLowFunds(accountId, account.getBalance().doubleValue());
            // O(log n) heap insertion if alert needed
        }
        
        // 6. Update category mapping (SimpleMap operation)
        categoryManager.addExpenditureToCategory(categoryName, expenditure);
        // O(n) category lookup + SimpleArrayList addition
        
        return true;
    }
}
```

**Data Structure Operations Count for Single Expenditure**:
- SimpleSet: 1 contains check (O(n))
- SimpleArrayList: 2 iterations + 1 addition (O(n) + O(1))
- SimpleMap: 2 lookups + 1 addition (O(n) + O(n) + O(n))
- MinHeap: 0-1 insertions (O(log n))
- **Total Complexity**: O(n) where n is the number of categories/accounts

### 2. Analytics Generation with Multiple Data Structures

```java
public class AnalyticsModule {
    public String generateComprehensiveReport(SimpleArrayList<Expenditure> expenditures) {
        // Use SimpleSet to count unique months
        SimpleSet<String> months = new SimpleSet<>();
        
        // Use SimpleMap to aggregate by category
        SimpleMap<String, BigDecimal> categoryTotals = new SimpleMap<>();
        
        // Use SimpleMap to aggregate by phase  
        SimpleMap<String, BigDecimal> phaseTotals = new SimpleMap<>();
        
        BigDecimal grandTotal = BigDecimal.ZERO;
        
        // Single pass through expenditures (O(n))
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure exp = expenditures.get(i);
            
            // Track unique months (O(n) for each contains check)
            String month = exp.getDateTime().getYear() + "-" + exp.getDateTime().getMonthValue();
            months.add(month);  // O(n) uniqueness check
            
            // Aggregate by category (O(n) for each get/put)
            String category = exp.getCategory().getName();
            BigDecimal catTotal = categoryTotals.get(category);  // O(n) lookup
            categoryTotals.put(category, catTotal == null ? exp.getAmount() : 
                              catTotal.add(exp.getAmount()));   // O(n) put
            
            // Aggregate by phase (O(n) for each get/put)
            String phase = exp.getPhase();
            BigDecimal phaseTotal = phaseTotals.get(phase);     // O(n) lookup
            phaseTotals.put(phase, phaseTotal == null ? exp.getAmount() : 
                           phaseTotal.add(exp.getAmount()));    // O(n) put
            
            grandTotal = grandTotal.add(exp.getAmount());
        }
        
        // Generate report using aggregated data
        StringBuilder report = new StringBuilder();
        report.append("=== COMPREHENSIVE FINANCIAL REPORT ===\n");
        report.append("Total Expenditure: GHc ").append(grandTotal).append("\n");
        report.append("Unique Months: ").append(months.size()).append("\n");
        
        // Monthly burn rate calculation
        int monthCount = months.size() == 0 ? 1 : months.size();
        BigDecimal monthlyBurn = grandTotal.divide(BigDecimal.valueOf(monthCount), 2, RoundingMode.HALF_UP);
        report.append("Monthly Burn Rate: GHc ").append(monthlyBurn).append("\n\n");
        
        // Category breakdown (O(n) iteration through SimpleMap)
        report.append("BY CATEGORY:\n");
        for (int i = 0; i < categoryTotals.size(); i++) {
            String cat = categoryTotals.getKeyAt(i);
            BigDecimal total = categoryTotals.getAt(i);
            BigDecimal percentage = total.divide(grandTotal, 4, RoundingMode.HALF_UP)
                                       .multiply(BigDecimal.valueOf(100));
            report.append("  ").append(cat).append(": GHc ").append(total)
                  .append(" (").append(percentage).append("%)\n");
        }
        
        // Phase breakdown (O(n) iteration through SimpleMap)
        report.append("\nBY PHASE:\n");  
        for (int i = 0; i < phaseTotals.size(); i++) {
            String phase = phaseTotals.getKeyAt(i);
            BigDecimal total = phaseTotals.getAt(i);
            report.append("  ").append(phase).append(": GHc ").append(total).append("\n");
        }
        
        return report.toString();
    }
}
```

**Performance Analysis of Analytics Generation**:
- Main loop: O(n) through expenditures
- Per expenditure: 3 O(n) operations (SimpleSet.add, 2x SimpleMap.put)
- **Total Complexity**: O(n²) for n expenditures
- **Space Complexity**: O(k) where k is unique categories + unique phases + unique months

## Performance Optimization Examples

### Current vs. Optimized Approaches

#### **Current: Linear Search in SimpleMap**
```java
// Current O(n) lookup
public V get(K key) {
    int idx = indexOf(key);  // O(n) linear search
    if (idx >= 0) return values.get(idx);
    return null;
}

private int indexOf(K key) {
    for (int i = 0; i < keys.size(); i++) {  // Linear scan
        if (keys.get(i).equals(key)) return i;
    }
    return -1;
}
```

#### **Potential: Hash-Based Optimization**
```java
// Potential O(1) lookup with hash table
public class OptimizedMap<K, V> {
    private Object[] buckets;
    private int capacity = 16;
    
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    public V get(K key) {
        int bucket = hash(key);           // O(1) hash calculation
        // O(1) average lookup in bucket
        return findInBucket(bucket, key);
    }
}
```

This demonstrates how the current simple implementations could be enhanced while maintaining the educational value of custom data structure development.

## Conclusion

These implementation examples demonstrate how custom data structures power real business functionality in the Xpense application. The examples show:

1. **Practical Usage**: Each data structure solves specific business problems
2. **Performance Trade-offs**: Simple implementations vs. optimal performance
3. **Integration Patterns**: How multiple data structures work together
4. **Complexity Analysis**: Real-world performance characteristics
5. **Optimization Opportunities**: Future enhancement possibilities

The custom implementations successfully balance educational clarity with practical functionality, providing a solid foundation for expenditure management while demonstrating fundamental computer science concepts.