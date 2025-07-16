# Module Assignment - Xpense Financial Tracker
**Group 68 - DCIT308 DSA II Project**

## Team Members:
1. **Fenteng Michael** - 11210750
2. **Samuel Akpah** - 11125009
3. **Ryan Brown** - 11357610
4. **Wisdom Nana-Abena Ogbonna** - 11288300
5. **Michel Kpodo** - 11012578
6. **Ransford Larbi** - 10681368
7. **Adams Emmanuel Paddy** - 11264136

---

## Module Assignments

### 1. **Fenteng Michael** - Core Data Models & Validation
**Files to implement:**
- `Expenditure.java` - Core expenditure data structure
- `Category.java` - Category management model
- `BankAccount.java` - Bank account data structure

**Functional Requirements:**
- Implement expenditure data model with fields: code, amount, date, phase, category, bankAccountId
- Create category validation system (is_valid() method)
- Design bank account structure with balance tracking
- Implement data validation for all models
- Create proper constructors, getters, setters, and toString methods

**Data Structures Focus:**
- Use appropriate primitive types and String handling
- Implement validation rules for financial data
- Consider using BigDecimal for monetary values

---

### 2. **Samuel Akpah** - Expenditure Management System
**Files to implement:**
- `ExpenditureManager.java` - Core business logic for expenditures
- `BankLedger.java` - Transaction logging and account management

**Functional Requirements:**
- Implement CRUD operations for expenditures (Create, Read, Update, Delete)
- Handle expenditure searching and filtering
- Implement expenditure sorting (by date, amount, category)
- Manage bank account expenditure logging
- Track account balances and expenditure history

**Data Structures Focus:**
- Use ArrayList/LinkedList for expenditure storage
- Implement search algorithms (linear/binary search)
- Use HashMap for quick account lookups
- Implement sorting algorithms

---

### 3. **Ryan Brown** - Category Management System
**Files to implement:**
- `CategoryManager.java` - Category operations and management

**Functional Requirements:**
- Implement category creation and validation
- Category search and filtering functionality
- Manage category relationships and hierarchy
- Validate expenditure categories against available categories
- Handle category statistics and usage tracking

**Data Structures Focus:**
- Use HashSet for unique category storage
- Implement Tree structure for category hierarchy
- Use HashMap for category-to-expenditure mapping
- Implement efficient search algorithms

---

### 4. **Wisdom Nana-Abena Ogbonna** - File Management & Data Persistence
**Files to implement:**
- `FileManager.java` - File I/O operations for all data

**Functional Requirements:**
- Implement file loading for: expenditures.txt, categories.txt, accounts.txt, receipts.txt
- Handle file saving and data persistence
- Implement backup and recovery mechanisms
- Handle file format validation and error recovery
- Manage data serialization/deserialization

**Data Structures Focus:**
- Use BufferedReader/BufferedWriter for file operations
- Implement data parsing and formatting
- Use ArrayList for batch data operations
- Handle file-based data integrity

---

### 5. **Michel Kpodo** - Receipt Management System
**Files to implement:**
- `Receipt.java` - Receipt data model
- `ReceiptHandler.java` - Receipt processing and management

**Functional Requirements:**
- Implement receipt data structure (receiptId, expenseCode, filePath, timestamp)
- Handle receipt upload and storage
- Implement receipt review system using queue/stack
- Link receipts to expenditures
- Handle receipt file management

**Data Structures Focus:**
- Use Stack<Receipt> for receipt processing order
- Use Queue<Receipt> for receipt review workflow
- Implement LinkedList for receipt history
- Use HashMap for receipt-to-expenditure mapping

---

### 6. **Ransford Larbi** - Analytics & Reporting Module
**Files to implement:**
- `AnalyticsModule.java` - Financial analytics and reporting

**Functional Requirements:**
- Calculate monthly burn rate and spending patterns
- Generate cost analysis reports
- Provide affordability insights and recommendations
- Track spending trends over time
- Generate summary statistics and charts (text-based)

**Data Structures Focus:**
- Use TreeMap for time-based data analysis
- Implement statistical calculations (averages, totals)
- Use PriorityQueue for top spending categories
- Implement data aggregation algorithms

---

### 7. **Adams Emmanuel Paddy** - User Interface & Alert System
**Files to implement:**
- `CLIHandler.java` - Command-line interface and menu system
- `AlertSystem.java` - Notification and alert management

**Functional Requirements:**
- Implement complete CLI menu system
- Handle user input validation and error messages
- Coordinate all system modules
- Implement alert system for low funds and spending limits
- Create user-friendly error handling and help system

**Data Structures Focus:**
- Use Scanner for user input handling
- Implement menu navigation using switch statements
- Use ArrayList for menu options
- Implement alert queue using Queue<Alert>

---

## Common Implementation Guidelines

### **IMPORTANT: Java Standard Library Only**
- **NO external packages/libraries allowed** (no Maven, Gradle dependencies)
- **ONLY use Java's built-in libraries** (java.util.*, java.io.*, java.time.*, etc.)
- All current imports are compliant (ArrayList, HashMap, Scanner, etc.)

### Data Structure Requirements (DSA II Focus):
- **Lists**: ArrayList, LinkedList for dynamic data storage
- **Maps**: HashMap, TreeMap for key-value relationships
- **Sets**: HashSet for unique data collections
- **Queues**: LinkedList, PriorityQueue for processing order
- **Stacks**: Stack for LIFO operations (receipts)
- **Trees**: For hierarchical data (categories)

### File Format Specifications:
- **expenditures.txt**: code|amount|date|phase|category|accountId|receiptPath
- **categories.txt**: name|description|color
- **accounts.txt**: accountId|accountName|balance
- **receipts.txt**: receiptId|expenseCode|filePath|timestamp

### Testing Requirements:
- Each module must have corresponding test methods
- Test both success and failure scenarios
- Use the test framework provided in `src/test/`
- Include edge case testing

### Integration Points:
- All modules must work together through the CLIHandler
- FileManager is used by all modules for data persistence
- AlertSystem monitors all financial operations
- AnalyticsModule uses data from all other modules

---

## Development Timeline Suggestion:

### **10-Day Sprint Approach** (Parallel Development - No Waiting!)

**Days 1-3: Foundation Phase**
- **Fenteng**: Complete data models (Expenditure, Category, BankAccount) with basic structure
- **Everyone else**: Start implementing method stubs and basic class structure
- **Key Milestone**: Data models ready for others to import and use

**Days 3-6: Core Development Phase**
- **Samuel**: Implement ExpenditureManager CRUD operations (can use Fenteng's models)
- **Ryan**: Develop CategoryManager (can work with basic Category model)
- **Wisdom**: Build FileManager with file I/O operations
- **Michel**: Create Receipt and ReceiptHandler classes
- **Ransford**: Start AnalyticsModule with basic calculations
- **Adams**: Begin CLIHandler menu structure and basic UI

**Days 6-8: Integration Phase**
- **All team members**: Integrate modules through CLIHandler
- **Focus**: Making modules work together, handling dependencies
- **Testing**: Each member tests their module integration

**Days 8-10: Polish & Testing Phase**
- **All team members**: Complete advanced features and edge cases
- **Team effort**: System testing, bug fixes, documentation
- **Final integration**: Ensure all modules work seamlessly

### **Key Dependencies & Workarounds:**
- **Others depending on Fenteng**: Use the existing scaffolding code with TODO comments to start development
- **FileManager dependency**: Wisdom can create file format specifications first, others can use mock data
- **CLIHandler integration**: Adams can create menu structure while others implement business logic

### **Daily Sync (Critical for 10-day timeline):**
- **Daily**: Quick progress updates via GitHub or group chat (15 min max)
- **Every 2 days**: Brief team meeting to resolve integration issues (30 min max)
- **Continuous**: Use GitHub Issues to track blockers and dependencies
- **Emergency**: Immediate help requests via group chat for blockers

## Communication:
- Use GitHub Issues for task tracking
- Regular team meetings for integration discussions
- Code reviews before merging pull requests
- Shared documentation updates
