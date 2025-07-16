## Core Data Structures Overview

### 1. **Expenditure Management** (`ExpenditureManager.java`)

**Data Structures Used:**
- `ArrayList<Expenditure>` - Dynamic array for storing expenditures
- `HashMap<String, Expenditure>` - Quick O(1) lookup by expenditure ID

**Key Algorithms:**
- **Search**: Linear search through expenditures by description, category, location (`searchExpenditures`)
- **Sorting**: Uses Java's built-in sorting with custom comparators for date, amount, category, description
- **Filtering**: Linear filtering by amount range

### 2. **Bank Account Management** (`BankLedger.java`)

**Data Structures Used:**
- `HashMap<String, BankAccount>` - O(1) account lookup by account ID
- `List<Transaction>` - Transaction history (chronological order)
- `HashMap<String, List<Transaction>>` - Per-account transaction tracking

**Key Features:**
- **Transaction Logging**: Each expenditure automatically debits the account
- **Balance Tracking**: Real-time balance updates with transaction history
- **Alert Integration**: Checks for low balance after each transaction

### 3. **Category Management** (`CategoryManager.java`)

**Data Structures Used:**
- `SimpleSet<Category>` - Custom set implementation for unique categories
- `SimpleMap<String, List<Object>>` - Maps category names to their expenditures

**Key Operations:**
- **Validation**: Ensures expenditures use valid categories
- **Search**: Linear search through categories
- **Statistics**: Tracks expenditure count per category

### 4. **Analytics Module** (`AnalyticsModule.java`)

**Data Structures Used:**
- `TreeMap<String, Double>` - Sorted monthly spending data for trends
- `PriorityQueue<CategorySpending>` - Top spending categories (heap-based)
- `Map<String, Double>` - Category totals for cost analysis

**Key Algorithms:**
- **Monthly Burn Rate**: Aggregates total spending / distinct months
- **Trend Analysis**: Time-series analysis using TreeMap for chronological ordering
- **Category Ranking**: Priority queue for top-N spending categories

### 5. **File Management** (`FileManager.java`)

**Data Structures Used:**
- `ArrayList` for batch loading/saving operations
- `BufferedReader/BufferedWriter` for efficient file I/O

**File Formats:**
- **expenditures.txt**: `code|amount|date|phase|category|accountId`
- **categories.txt**: `name|description|color`
- **accounts.txt**: `accountId|accountName|balance`

### 6. **Receipt Management** (Mentioned in specs)

**Planned Data Structures:**
- `Stack<Receipt>` - LIFO processing for receipt review
- `Queue<Receipt>` - FIFO workflow for receipt processing
- `HashMap<String, String>` - Receipt to expenditure mapping

### 7. **CLI Handler** (`CLIHandler.java`)

**Data Structures Used:**
- `Scanner` for user input handling
- Switch statements for menu navigation
- Integration point for all other modules

## Algorithm Analysis

### **Search Operations:**
- **By ID**: O(1) using HashMap lookup
- **By Description/Category**: O(n) linear search through ArrayList
- **Category Validation**: O(1) using set lookup

### **Sorting Operations:**
- **Expenditures**: O(n log n) using Java's TimSort with custom comparators
- **Date sorting**: `sortExpenditures` method
- **Amount sorting**: Numerical comparison

### **Analytics Calculations:**
- **Monthly Burn Rate**: O(n) single pass through expenditures
- **Category Breakdown**: O(n) aggregation with HashMap
- **Top Categories**: O(n log k) using PriorityQueue

### **File Operations:**
- **Loading**: O(n) where n is file size
- **Saving**: O(n) batch write operations
- **Backup**: Incremental file copying

## Key Design Patterns

1. **Module Separation**: Each feature is encapsulated in its own class
2. **Data Consistency**: `BankLedger` automatically updates when expenditures are added
3. **Error Handling**: Graceful handling of invalid inputs and file operations
4. **Integration**: All modules coordinate through `CLIHandler`

## Performance Characteristics

- **Account Operations**: O(1) lookup and balance updates
- **Expenditure Search**: O(n) linear search (could be optimized with indexing)
- **Analytics**: O(n) for most calculations, O(n log n) for sorting
- **File I/O**: Batch operations for efficiency

