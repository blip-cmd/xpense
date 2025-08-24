# Xpense Data Structures Architecture Documentation

## Executive Summary

The Xpense expenditure management system is built entirely on **custom-implemented data structures**, avoiding Java Collections Framework for core business logic. This design choice was made to demonstrate mastery of fundamental data structure concepts and algorithms while providing a robust, offline expenditure tracking solution for Nkwa Real Estate Ltd.

## Custom Data Structures Foundation

### 1. **SimpleArrayList&lt;T&gt;** - Dynamic Array Implementation

**Location**: `app/util/SimpleArrayList.java`

**Core Functionality:**
```java
public class SimpleArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;
}
```

**Key Features:**
- **Dynamic Resizing**: Automatically doubles capacity when full (line 59-64)
- **Generic Type Support**: Type-safe operations with proper casting
- **Iterator Support**: Implements `Iterable<T>` for enhanced for-loops
- **Index-based Access**: Get/set operations by index

**Performance Characteristics:**
- **Access**: O(1) - Direct array indexing
- **Insertion**: O(1) amortized - May trigger O(n) resize operation
- **Removal**: O(n) - Requires shifting elements left
- **Search**: O(n) - Linear scan through elements

**Memory Usage**: 
- Initial: 10 object references (40-80 bytes depending on JVM)
- Growth: Doubles when capacity exceeded
- Overhead: ~25% unused capacity after growth

**Usage Throughout Application:**
- Foundation for all other custom data structures
- Direct usage in `ExpenditureManager` for storing expenditures
- Used in `BankAccount` for tracking per-account expenditures

### 2. **SimpleMap&lt;K,V&gt;** - Key-Value Mapping Structure

**Location**: `app/util/SimpleMap.java`

**Core Implementation:**
```java
public class SimpleMap<K, V> {
    private final SimpleArrayList<K> keys;
    private final SimpleArrayList<V> values;
}
```

**Architecture Design:**
- **Parallel Arrays**: Uses two synchronized `SimpleArrayList` instances
- **Index Correlation**: Key at index i corresponds to value at index i
- **Insertion Strategy**: Linear search for existing keys, append for new keys

**Key Operations:**
- **Put Operation**: O(n) - Must search for existing key, complex update logic
- **Get Operation**: O(n) - Linear search through keys
- **Remove Operation**: O(n) - Find index, then remove from both arrays
- **Contains Check**: O(n) - Linear scan of keys

**Critical Implementation Detail:**
The `put` method for updating existing keys (lines 13-35) demonstrates complex array manipulation to maintain synchronization between keys and values arrays.

**Usage Patterns:**
- **BankLedger**: Maps account IDs to `BankAccount` objects
- **CategoryManager**: Maps category names to expenditure lists
- **AnalyticsModule**: Maps categories/phases to aggregated amounts

**Trade-offs:**
- ✅ Simple implementation, no hash function complexity
- ✅ Predictable memory usage
- ❌ O(n) operations instead of O(1) for hash tables
- ❌ Update operation requires full array reconstruction

### 3. **SimpleSet&lt;T&gt;** - Uniqueness Enforcement Structure

**Location**: `app/util/SimpleSet.java`

**Implementation Strategy:**
```java
public class SimpleSet<T> implements Iterable<T> {
    private final SimpleArrayList<T> items;
    
    public boolean add(T item) {
        if (!contains(item)) {
            items.add(item);
            return true;
        }
        return false;
    }
}
```

**Uniqueness Algorithm:**
- **Duplicate Prevention**: Check-then-add pattern prevents duplicates
- **Equality Method**: Relies on object's `equals()` method for comparison
- **Linear Search**: O(n) contains check for each addition

**Performance Analysis:**
- **Add Operation**: O(n) - Must verify uniqueness before insertion
- **Contains Check**: O(n) - Linear scan through internal list
- **Remove Operation**: O(n) - Delegates to SimpleArrayList.remove()
- **Iteration**: O(1) setup, O(n) total - Direct delegation to SimpleArrayList

**Critical Usage - Category Management:**
```java
// CategoryManager.java
private final SimpleSet<Category> categories;

public boolean addCategory(Category category) {
    if (category == null || !category.isValid()) return false;
    if (categories.contains(category)) return false;  // O(n) check
    categories.add(category);  // O(n) add with uniqueness
    return true;
}
```

The `Category` class implements custom equality based on name comparison (case-insensitive), making the set enforce unique category names.

### 4. **MinHeap&lt;T&gt;** - Priority Queue Implementation

**Location**: `app/util/MinHeap.java`

**Binary Heap Structure:**
```java
public class MinHeap<T> {
    private Object[] heap;
    private int size;
    private final PriorityComparator<T> comparator;
}
```

**Heap Property Maintenance:**
- **Index 1 Based**: Root at index 1, left child at 2i, right child at 2i+1
- **Custom Comparator**: Flexible priority definition via PriorityComparator interface
- **Automatic Resizing**: Doubles capacity when needed

**Core Algorithms:**
- **Sift Up** (lines 31-38): Maintains heap property after insertion
- **Sift Down** (lines 40-51): Restores heap property after removal
- **Insert**: O(log n) - Add to end, then sift up
- **Remove Min**: O(log n) - Replace root with last element, sift down

**Real-World Usage - Alert System:**
```java
// AlertSystem.java
private final MinHeap<Alert> heap;

public AlertSystem(double lowBalanceThreshold, double spendingLimitThreshold) {
    this.heap = new MinHeap<>(new MinHeap.PriorityComparator<Alert>() {
        public int compare(Alert a, Alert b) {
            return a.priority - b.priority;  // Lower numbers = higher priority
        }
    });
}
```

**Alert Priority Processing:**
1. **Low Balance Alerts**: Priority 1 (highest)
2. **Spending Limit Alerts**: Higher priority numbers
3. **Processing Order**: Always retrieves most critical alerts first

### 5. **SimpleQueue&lt;T&gt;** - FIFO Data Structure

**Location**: `app/util/SimpleQueue.java`

**Implementation Simplicity:**
```java
public class SimpleQueue<T> {
    private final SimpleArrayList<T> arr;
    
    public void offer(T value) { arr.add(value); }           // O(1) amortized
    public T poll() { return arr.remove(0); }                // O(n) - array shift
}
```

**Performance Considerations:**
- **Enqueue (offer)**: O(1) amortized - Appends to end of array
- **Dequeue (poll)**: O(n) - Removes from front, shifts all elements left
- **Space Efficiency**: No wasted space, grows as needed

**Receipt Processing Workflow:**
```java
// ReceiptHandler.java
private final SimpleQueue<Receipt> processingQueue;

public void addReceipt(Receipt receipt) {
    if (receipt != null) {
        receipts.add(receipt);
        processingQueue.offer(receipt);  // Queued for processing
    }
}
```

**Trade-off Analysis:**
- ✅ Simple implementation
- ✅ Predictable FIFO behavior
- ❌ O(n) dequeue operation (could be O(1) with circular buffer)

### 6. **SimpleStack&lt;T&gt;** - LIFO Data Structure

**Location**: `app/util/SimpleStack.java`

**Optimal Implementation:**
```java
public class SimpleStack<T> {
    private final SimpleArrayList<T> arr;
    
    public void push(T value) { arr.add(value); }                    // O(1) amortized
    public T pop() { return arr.remove(arr.size() - 1); }           // O(1)
    public T peek() { return arr.get(arr.size() - 1); }             // O(1)
}
```

**Performance Excellence:**
- **Push Operation**: O(1) amortized - Appends to end
- **Pop Operation**: O(1) - Removes from end, no shifting needed
- **Peek Operation**: O(1) - Direct access to last element

**Receipt Review System:**
```java
// ReceiptHandler.java
private final SimpleStack<Receipt> processedStack;

// After processing receipts from queue, they're pushed to stack for review
```

**Design Rationale**: Stack allows reviewing recently processed receipts first (LIFO), which is intuitive for audit workflows.

### 7. **SimpleTreeMap&lt;K,V&gt;** - Ordered Key-Value Mapping

**Location**: `app/util/SimpleTreeMap.java`

**Binary Search Tree Implementation:**
```java
private class Node {
    K key;
    V value;
    Node left, right;
}
```

**Tree Operations:**
- **Insert**: O(log n) average, O(n) worst case - Recursive BST insertion
- **Search**: O(log n) average, O(n) worst case - Binary search
- **Delete**: O(log n) average, O(n) worst case - Complex case handling
- **In-order Traversal**: O(n) - Provides sorted key iteration

**Key Constraint**: Requires `K extends Comparable<K>` for natural ordering

**Potential Usage**: While implemented, this structure isn't heavily used in current modules but provides foundation for future chronological data analysis.

## Application Module Integration

### **ExpenditureManager** - Core Storage Engine

**Data Structure Strategy:**
```java
public class ExpenditureManager {
    private final SimpleArrayList<Expenditure> expenditures;
    private static int idCounter = 1000;
}
```

**Key Operations:**
- **ID Generation**: Synchronized counter ensures unique expenditure IDs
- **Duplicate Prevention**: Linear search prevents ID conflicts
- **Batch Loading**: Efficient bulk insertion from file system

**Smart ID Management:**
```java
private void initializeIdCounter() {
    int maxId = 1000;
    for (Expenditure exp : expenditures) {
        String id = exp.getId();
        if (id != null && id.startsWith("EXP")) {
            int numericPart = Integer.parseInt(id.substring(3));
            if (numericPart >= maxId) {
                maxId = numericPart + 1;
            }
        }
    }
    idCounter = maxId;
}
```

### **CategoryManager** - Uniqueness and Mapping

**Dual Data Structure Approach:**
```java
public class CategoryManager {
    private final SimpleSet<Category> categories;           // Uniqueness
    private final SimpleMap<String, SimpleArrayList<Expenditure>> categoryExpenditures;  // Grouping
}
```

**Business Logic Integration:**
- **Category Validation**: SimpleSet prevents duplicate category names
- **Expenditure Grouping**: SimpleMap organizes expenditures by category
- **Cross-reference Maintenance**: Updates both structures atomically

### **BankLedger** - Account Management and Alerts

**Financial Data Integrity:**
```java
public class BankLedger {
    private final SimpleMap<String, BankAccount> accounts;
    private final AlertSystem alertSystem;
    
    public boolean logExpenditure(String accountId, BigDecimal amount, String description) {
        BankAccount account = accounts.get(accountId);  // O(n) lookup
        boolean debited = account.debit(amount);
        if (debited && alertSystem != null) {
            alertSystem.checkLowFunds(accountId, account.getBalance().doubleValue());
        }
        return debited;
    }
}
```

**Integration Pattern**: Each expenditure triggers:
1. Account balance update
2. Low balance check
3. Potential alert generation (MinHeap insertion)

### **AlertSystem** - Priority-based Notification

**MinHeap-Powered Alerting:**
```java
public class AlertSystem {
    private final MinHeap<Alert> heap;
    
    public boolean checkLowFunds(String accountId, double currentBalance) {
        if (currentBalance < lowBalanceThreshold) {
            addAlert("Account " + accountId + " is low on funds: GHc " + currentBalance, 1);
            return true;
        }
        return false;
    }
}
```

**Priority Scheme**:
- Priority 1: Critical alerts (low balance)
- Priority 2+: Warning levels
- Always processes highest priority (lowest number) first

### **AnalyticsModule** - Aggregation Engine

**Multi-Structure Analytics:**
```java
public class AnalyticsModule {
    public BigDecimal calculateMonthlyBurn(SimpleArrayList<Expenditure> expenditures) {
        BigDecimal total = BigDecimal.ZERO;
        SimpleSet<String> months = new SimpleSet<>();  // Unique month tracking
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            total = total.add(e.getAmount());
            months.add(e.getDateTime().getYear() + "-" + e.getDateTime().getMonthValue());
        }
        
        int monthCount = months.size() == 0 ? 1 : months.size();
        return total.divide(BigDecimal.valueOf(monthCount), 2, RoundingMode.HALF_UP);
    }
}
```

**Analytical Patterns**:
- **SimpleSet**: Counts unique time periods
- **SimpleMap**: Aggregates amounts by categories/phases
- **SimpleArrayList**: Iteration through all expenditures

### **SearchAndSortModule** - Query Engine

**Algorithm Implementation:**
```java
public SimpleArrayList<Expenditure> sortByCategoryAlphabetical(SimpleArrayList<Expenditure> expenditures) {
    // Bubble sort implementation - O(n²)
    for (int i = 0; i < sorted.size() - 1; i++) {
        for (int j = 0; j < sorted.size() - 1 - i; j++) {
            String cat1 = sorted.get(j).getCategory().getName().toLowerCase();
            String cat2 = sorted.get(j + 1).getCategory().getName().toLowerCase();
            if (cat1.compareTo(cat2) > 0) {
                // Swap elements
            }
        }
    }
}
```

**Search Strategies**:
- **Linear Search**: O(n) for all search operations
- **Bubble Sort**: O(n²) for sorting (simple but inefficient)
- **Multiple Criteria**: Date, category, amount range, account, phase

## Performance Analysis and Complexity

### **Operation Complexity Summary**

| Operation | Data Structure | Best Case | Average Case | Worst Case | Space |
|-----------|---------------|-----------|--------------|------------|-------|
| **Add Expenditure** | SimpleArrayList | O(1) | O(1) amortized | O(n) resize | O(n) |
| **Search by ID** | SimpleArrayList | O(1) first | O(n/2) | O(n) | O(1) |
| **Account Lookup** | SimpleMap | O(1) first | O(n/2) | O(n) | O(1) |
| **Category Validation** | SimpleSet | O(1) first | O(n/2) | O(n) | O(1) |
| **Alert Processing** | MinHeap | O(log 1) | O(log n) | O(log n) | O(1) |
| **Receipt Queue** | SimpleQueue | O(1) add | O(n) remove | O(n) remove | O(1) |
| **Receipt Stack** | SimpleStack | O(1) | O(1) | O(1) | O(1) |
| **Sort by Category** | Custom | O(n²) | O(n²) | O(n²) | O(n) |

### **Memory Usage Patterns**

**SimpleArrayList Growth Strategy:**
- Initial capacity: 10 elements
- Growth factor: 2x (doubles when full)
- Memory overhead: ~25% unused space after growth
- Garbage collection: Old arrays eligible for GC after resize

**SimpleMap Memory Efficiency:**
- Parallel array storage: 2n references for n key-value pairs
- No hash table overhead
- Linear memory growth with entries
- Cache-friendly sequential access

**MinHeap Memory Layout:**
- Array-based binary heap starting at index 1
- Parent-child relationship: parent(i) = i/2, children(i) = 2i, 2i+1
- Memory locality: Excellent for small heaps, good cache performance

## Architecture Design Patterns

### **1. Composition over Inheritance**

All custom data structures are built by **composing SimpleArrayList**, rather than extending it:

```java
// SimpleMap composes two SimpleArrayLists
public class SimpleMap<K, V> {
    private final SimpleArrayList<K> keys;
    private final SimpleArrayList<V> values;
}

// SimpleSet composes one SimpleArrayList
public class SimpleSet<T> {
    private final SimpleArrayList<T> items;
}
```

**Benefits:**
- ✅ Clear separation of concerns
- ✅ Easier to maintain and debug
- ✅ Flexible internal implementation changes
- ✅ No inheritance complexity

### **2. Template Method Pattern**

The **MinHeap** uses a strategy pattern for comparison:

```java
public interface PriorityComparator<T> {
    int compare(T a, T b);
}

// Usage allows different priority schemes:
// Alert System: Lower number = higher priority
// Could extend for: Higher amount = higher priority, etc.
```

### **3. Data Structure Synchronization**

**Critical Pattern**: Multiple data structures stay synchronized atomically:

```java
// CategoryManager maintains two synchronized structures
public boolean addCategory(Category category) {
    categories.add(category);                                    // SimpleSet
    categoryExpenditures.put(category.getName(), new SimpleArrayList<>()); // SimpleMap
    return true;
}
```

### **4. Defensive Programming**

**Null Safety and Validation:**
```java
public boolean addExpenditure(Expenditure expenditure) {
    if (expenditure == null) return false;           // Null check
    if (!expenditure.isValid()) return false;        // Business logic validation
    
    // Check for duplicate IDs
    for (Expenditure e : expenditures) {
        if (e.getId().equalsIgnoreCase(expenditure.getId())) {
            return false;
        }
    }
    expenditures.add(expenditure);
    return true;
}
```

## Real-World Usage Examples

### **1. Adding an Expenditure - Complete Data Flow**

```java
// 1. User inputs expenditure data via CLI
// 2. ExpenditureManager validates and stores
ExpenditureManager em = new ExpenditureManager();
em.addExpenditure("Cement purchase", new BigDecimal("1200.00"), category, "Construction", "ACC001");

// 3. BankLedger debits account
BankLedger ledger = new BankLedger(alertSystem);
ledger.logExpenditure("ACC001", new BigDecimal("1200.00"), "Cement purchase");

// 4. AlertSystem checks for low balance
// MinHeap automatically orders alerts by priority
alertSystem.checkLowFunds("ACC001", 500.00);  // May add alert to heap

// 5. CategoryManager tracks category usage
categoryManager.addExpenditureToCategory("Cement", expenditure);
```

**Data Structures Involved:**
- SimpleArrayList (expenditure storage)
- SimpleMap (account lookup)
- MinHeap (alert priority queue)
- SimpleMap (category-expenditure mapping)

### **2. Monthly Analytics Generation**

```java
// Analytics leverages multiple data structures for calculations
public BigDecimal calculateMonthlyBurn(SimpleArrayList<Expenditure> expenditures) {
    BigDecimal total = BigDecimal.ZERO;                    // Accumulator
    SimpleSet<String> months = new SimpleSet<>();          // Unique month tracking
    
    // Single pass through expenditures (O(n))
    for (int i = 0; i < expenditures.size(); i++) {
        Expenditure e = expenditures.get(i);
        total = total.add(e.getAmount());                  // Sum amounts
        months.add(e.getDateTime().getYear() + "-" + 
                  e.getDateTime().getMonthValue());        // Track unique months
    }
    
    // Division by unique month count
    int monthCount = months.size() == 0 ? 1 : months.size();
    return total.divide(BigDecimal.valueOf(monthCount), 2, RoundingMode.HALF_UP);
}
```

**Algorithm Efficiency:**
- One pass through expenditures: O(n)
- SimpleSet ensures unique month counting: O(n²) worst case for contains checks
- Total complexity: O(n²) due to set operations

### **3. Priority Alert Processing**

```java
// Alert System processes alerts in priority order
public void displayAllAlerts() {
    if (!hasAlerts()) {
        System.out.println("No active alerts.");
        return;
    }
    
    System.out.println("=== ALERTS ===");
    while (hasAlerts()) {                          // Until heap is empty
        System.out.println("• " + getNextAlert()); // O(log n) removeMin operation
    }
}
```

**Processing Guarantees:**
- Priority 1 alerts (critical) always display first
- MinHeap ensures O(log n) extraction of next highest priority
- Complete processing: O(n log n) for n alerts

## Design Trade-offs and Decisions

### **Simplicity vs Performance**

**Decision**: Chose simple implementations over optimal performance
- **SimpleMap**: O(n) operations vs O(1) hash map
- **SearchAndSortModule**: O(n²) bubble sort vs O(n log n) algorithms

**Rationale**:
- ✅ Educational clarity: Easy to understand and debug
- ✅ Predictable behavior: No hash collision edge cases
- ✅ Small data sets: Performance difference negligible for typical usage
- ❌ Scalability: Would not perform well with thousands of expenditures

### **Memory vs CPU Trade-offs**

**SimpleArrayList Growth Strategy**:
- Doubles capacity when full (aggressive growth)
- ✅ Reduces number of resize operations
- ❌ May waste up to 50% memory after growth
- Alternative: 1.5x growth would use less memory but resize more often

**SimpleMap Parallel Arrays**:
- Uses two arrays instead of array of key-value pairs
- ✅ Type safety and simplicity
- ❌ Poor cache locality compared to single array
- ❌ Two array lookups instead of one

### **Consistency vs Performance**

**Synchronized Data Structures**:
- CategoryManager maintains both SimpleSet and SimpleMap
- ✅ Data consistency always guaranteed
- ❌ Duplicate storage of category information
- ❌ Multiple O(n) operations for single logical operation

## Optimization Opportunities

### **1. SimpleMap Enhancement**
```java
// Current: O(n) operations
// Potential: Hash-based implementation for O(1) average case
// Alternative: Use SimpleTreeMap for O(log n) operations with ordering
```

### **2. Search Optimization**
```java
// Current: Linear search for expenditures by ID
// Potential: Maintain SimpleMap<String, Expenditure> for O(n) → O(n) lookup
// Cost: Additional memory for secondary index
```

### **3. Sorting Algorithm Upgrade**
```java
// Current: O(n²) bubble sort
// Potential: Implement merge sort or quick sort for O(n log n)
// Benefit: Better performance for larger datasets
```

### **4. Memory Pool Management**
```java
// Current: Each SimpleArrayList manages its own capacity
// Potential: Shared memory pool for all dynamic arrays
// Benefit: Reduced memory fragmentation and GC pressure
```

## Testing and Quality Assurance

### **Data Structure Invariants**

**SimpleArrayList Invariants:**
- `size <= elements.length` always true
- `elements[size]` through `elements[elements.length-1]` are null
- No null elements in positions 0 through size-1

**SimpleMap Invariants:**
- `keys.size() == values.size()` always true
- `keys.get(i)` corresponds to `values.get(i)` for all valid i
- No duplicate keys exist

**MinHeap Invariants:**
- Heap property: `parent.priority <= child.priority` for all nodes
- Complete binary tree structure maintained
- Root element (index 1) has minimum priority

### **Error Handling Patterns**

**Boundary Condition Handling:**
```java
// SimpleArrayList.get()
public T get(int index) {
    if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    return (T) elements[index];
}

// SimpleMap.get()
public V get(K key) {
    int idx = indexOf(key);
    if (idx >= 0) return values.get(idx);
    return null;  // Graceful failure instead of exception
}
```

**Different strategies**: Exception for programming errors, null for missing data

## Conclusion

The Xpense data structures architecture demonstrates a successful balance between **educational clarity** and **practical functionality**. While the implementations prioritize simplicity over optimal performance, they provide:

1. **Complete Functionality**: All required operations for expenditure management
2. **Type Safety**: Generic implementations prevent runtime type errors  
3. **Predictable Performance**: Linear complexity is predictable and debuggable
4. **Educational Value**: Clear implementation of fundamental data structure concepts
5. **Extensibility**: Modular design allows easy addition of new features

The custom data structures successfully power a complete business application while serving as an excellent demonstration of data structure implementation principles. For production use with larger datasets, selective optimization of critical paths (particularly SimpleMap operations) would provide significant performance benefits while maintaining the architectural clarity.

**Future Enhancements**: The solid foundation enables easy transition to more sophisticated algorithms (hash tables, balanced trees, advanced sorting) when performance requirements demand optimization.

