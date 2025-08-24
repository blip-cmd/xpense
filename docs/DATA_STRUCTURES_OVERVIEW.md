# Xpense Data Structures Quick Reference

## Visual Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         APPLICATION LAYER                       │
├─────────────────────────────────────────────────────────────────┤
│  ExpenditureManager │ CategoryManager │ BankLedger │ AlertSystem │
│  ┌─────────────────┐ │ ┌─────────────┐ │ ┌────────┐ │ ┌─────────┐ │
│  │ SimpleArrayList │ │ │ SimpleSet   │ │ │SimpleMap│ │ │ MinHeap │ │
│  │ <Expenditure>   │ │ │ <Category>  │ │ │<Acc,BA>│ │ │ <Alert> │ │
│  └─────────────────┘ │ │ SimpleMap   │ │ └────────┘ │ └─────────┘ │
│                      │ │ <Cat,ExpList│ │            │             │
│                      │ └─────────────┘ │            │             │
├─────────────────────────────────────────────────────────────────┤
│                  CUSTOM DATA STRUCTURES LAYER                   │
├─────────────────────────────────────────────────────────────────┤
│ SimpleArrayList │ SimpleMap │ SimpleSet │ MinHeap │ SimpleQueue │ │
│ SimpleStack │ SimpleTreeMap                                       │
├─────────────────────────────────────────────────────────────────┤
│                      JAVA FOUNDATION                            │
│                    (Object[], Comparable)                       │
└─────────────────────────────────────────────────────────────────┘
```

## Data Structure Hierarchy and Dependencies

```
SimpleArrayList (Foundation)
├── SimpleMap (uses 2x SimpleArrayList)
├── SimpleSet (uses 1x SimpleArrayList)
├── SimpleQueue (uses 1x SimpleArrayList)
└── SimpleStack (uses 1x SimpleArrayList)

MinHeap (Independent - uses Object[])

SimpleTreeMap (Independent - uses Node tree structure)
```

## Core Data Structures Summary

| Data Structure | Purpose | Key Methods | Complexity | Used By |
|---------------|---------|-------------|------------|---------|
| **SimpleArrayList&lt;T&gt;** | Dynamic array storage | add(), get(), remove() | O(1)/O(n) | Foundation for all others |
| **SimpleMap&lt;K,V&gt;** | Key-value mapping | put(), get(), remove() | O(n) | BankLedger, CategoryManager |
| **SimpleSet&lt;T&gt;** | Unique elements | add(), contains(), remove() | O(n) | CategoryManager |
| **MinHeap&lt;T&gt;** | Priority queue | insert(), removeMin() | O(log n) | AlertSystem |
| **SimpleQueue&lt;T&gt;** | FIFO processing | offer(), poll() | O(1)/O(n) | ReceiptHandler |
| **SimpleStack&lt;T&gt;** | LIFO processing | push(), pop(), peek() | O(1) | ReceiptHandler |
| **SimpleTreeMap&lt;K,V&gt;** | Ordered mapping | put(), get(), remove() | O(log n) avg | Available for future use |

## Usage Patterns in Application

### 1. **Expenditure Management Flow**
```
User Input → ExpenditureManager(SimpleArrayList) → Validation → Storage
                    ↓
            BankLedger(SimpleMap) → Account Lookup → Balance Update
                    ↓
            AlertSystem(MinHeap) → Low Balance Check → Priority Alert
```

### 2. **Category Management Flow**
```
New Category → CategoryManager(SimpleSet) → Uniqueness Check → Storage
                    ↓
            CategoryManager(SimpleMap) → Category-Expenditure Mapping
```

### 3. **Receipt Processing Flow**
```
New Receipt → ReceiptHandler(SimpleQueue) → FIFO Processing
                    ↓
            Processing Complete → ReceiptHandler(SimpleStack) → LIFO Review
```

### 4. **Alert Processing Flow**
```
Low Balance Event → AlertSystem(MinHeap) → Priority Assignment → Queue
                    ↓
            Display Alerts → MinHeap.removeMin() → Highest Priority First
```

## Performance Characteristics

### **Best Case Scenarios**
- **SimpleArrayList**: O(1) append operations when capacity available
- **SimpleStack**: O(1) all operations (push, pop, peek)
- **MinHeap**: O(log 1) = O(1) for single element
- **SimpleMap**: O(1) when key is first in array

### **Worst Case Scenarios**
- **SimpleArrayList**: O(n) resize operation when capacity exceeded
- **SimpleMap**: O(n) for all operations (must scan entire key array)
- **SimpleSet**: O(n) contains check (must scan entire array)
- **SimpleQueue**: O(n) poll operation (must shift all elements)

### **Memory Usage**
- **SimpleArrayList**: 25% overhead on average (due to 2x growth strategy)
- **SimpleMap**: 100% overhead (two parallel arrays)
- **SimpleSet**: Minimal overhead (wrapper around SimpleArrayList)
- **MinHeap**: ~6% overhead (array starts at index 1)

## Common Operations Guide

### **Adding an Expenditure**
```java
// Step 1: Create expenditure
Expenditure exp = new Expenditure(desc, amount, category, date, phase);

// Step 2: Add to manager (SimpleArrayList operations)
expenditureManager.addExpenditure(exp);  // O(1) amortized

// Step 3: Update bank account (SimpleMap operations)
bankLedger.logExpenditure(accountId, amount, desc);  // O(n) lookup

// Step 4: Check for alerts (MinHeap operations)
alertSystem.checkLowFunds(accountId, balance);  // O(log n) insert
```

### **Searching Expenditures**
```java
// By category (linear search)
SearchAndSortModule.searchByCategory(expenditures, "Cement");  // O(n)

// By date range (linear search with date comparison)
SearchAndSortModule.searchByTimeRange(expenditures, start, end);  // O(n)

// By amount range (linear search with numeric comparison)
SearchAndSortModule.searchByCostRange(expenditures, min, max);  // O(n)
```

### **Processing Alerts**
```java
// Add alert with priority
alertSystem.addAlert("Low balance warning", 1);  // O(log n)

// Process all alerts in priority order
while (alertSystem.hasAlerts()) {
    String alert = alertSystem.getNextAlert();  // O(log n)
    System.out.println(alert);
}
```

### **Analytics Calculations**
```java
// Monthly burn rate (uses SimpleSet for unique months)
BigDecimal monthlyBurn = analyticsModule.calculateMonthlyBurn(expenditures);  // O(n²)

// Category breakdown (uses SimpleMap for aggregation)
String analysis = analyticsModule.generateCostAnalysis(expenditures);  // O(n²)
```

## Implementation Tips

### **Memory Management**
- SimpleArrayList grows by 2x when full - consider initial capacity for large datasets
- SimpleMap stores keys and values separately - memory usage is 2x + overhead
- MinHeap uses 1-based indexing - array[0] is unused

### **Performance Optimization**
- For frequent lookups, consider maintaining secondary indices
- SimpleMap operations are O(n) - batch operations when possible
- SimpleSet contains() is expensive - minimize duplicate checks

### **Error Handling**
- SimpleArrayList throws IndexOutOfBoundsException for invalid indices
- SimpleMap returns null for missing keys (no exceptions)
- MinHeap returns null for empty heap operations

### **Thread Safety**
- None of the custom data structures are thread-safe
- Application assumes single-threaded access
- Synchronization would be needed for concurrent use

## Extension Points

### **Future Enhancements**
1. **Hash-based SimpleMap**: Replace linear search with hash table for O(1) operations
2. **Balanced SimpleTreeMap**: Add self-balancing for guaranteed O(log n) operations
3. **Indexed Search**: Add secondary indices for common search patterns
4. **Memory Pools**: Shared object pools to reduce GC pressure
5. **Compression**: Compact storage for historical data

### **Algorithm Upgrades**
1. **Sorting**: Replace bubble sort with merge sort (O(n log n))
2. **Search**: Add binary search for sorted data
3. **Analytics**: Implement incremental calculations for real-time updates
4. **Caching**: Add LRU cache for frequently accessed data

This architecture provides a solid foundation for expenditure management while demonstrating core data structure concepts in a practical business application.