# Contributing to Xpense - Financial Tracker

## Development Workflow
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Implement your assigned module
4. Write tests for your implementation
5. Submit a pull request

## Module Assignment Guide
- **BankAccount.java**: Core account data structure
- **Expenditure.java**: Expenditure model and operations
- **Category.java**: Category management system
- **ExpenditureManager.java**: Main business logic for expenditures
- **CategoryManager.java**: Category operations and validation
- **FileManager.java**: File I/O operations
- **ReceiptHandler.java**: Receipt processing logic
- **BankLedger.java**: Transaction history management
- **AnalyticsModule.java**: Data analysis and reporting
- **AlertSystem.java**: Notification and alert system

## Coding Standards
- Use proper Java naming conventions
- Include package declarations: `package app.modules;`
- Add comprehensive JavaDoc comments
- Follow the established project structure
- Write unit tests for all public methods
- **CRITICAL: Only use Java standard library - NO external packages/dependencies**

## Testing
- Place test files in `src/test/` directory
- Use JUnit framework (if available) or create simple test methods
- Test both success and failure scenarios

## Data Storage
- Use the files in `src/data/` for persistent storage
- Follow the established file formats for consistency
- Handle file I/O errors gracefully
