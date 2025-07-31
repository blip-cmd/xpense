# Nkwa Real Estate Expenditure Management System

## Overview

This project implements an **offline, locally-stored, command-line expenditure tracking application** for Nkwa Real Estate Ltd, designed for DCIT308 at the University of Ghana.  
It uses **custom-built data structures only** (arrays, sets, maps, stacks, queues, heaps, etc.) and avoids all external libraries and Java Collections for business logic.

## Features

- **Expenditure Tracking:** Record code, amount, date, phase, category, and account used for each expenditure.
- **Category Management:** Uniqueness enforced via sets. Search, add, edit, delete categories.
- **Bank Account Ledger:** Store, update accounts; each expenditure draws funds and updates balances.
- **Receipt/Invoice Handling:** Link receipts to expenditures; use queues/stacks for upload and review.
- **Alerts:** Min-heap based priority alerts for low balances and overspending.
- **Analytics:** Monthly burn rate, cost breakdown, simple profitability forecast.
- **Search & Sort:** Sort by category or date; search by time, category, cost range, or account.
- **Persistence:** All data stored in plain text files: `accounts.txt`, `categories.txt`, `expenditures.txt`, `receipts.txt`.
- **CLI Menu:** Menu-driven workflow for all actions.

## Data Structures Used

- `SimpleArrayList` - dynamic arrays
- `SimpleSet` - uniqueness for categories
- `SimpleMap` - key-value mappings (accounts, expenditures)
- `SimpleQueue` - FIFO for receipts
- `SimpleStack` - LIFO for receipts
- `MinHeap` - alert priorities

## File Structure

```
src/
  app/
    Main.java
    modules/
      (All modules, managers, entities)
    util/
      (Custom data structures)
  data/
    accounts.txt
    categories.txt
    expenditures.txt
    receipts.txt
```

## Sample Data Files

### accounts.txt

```
ACC001|Main Operations|5000.00
ACC002|Marketing|2500.00
ACC003|Sales|1000.00
```

### categories.txt

```
Cement|Building material|gray
Printing|Marketing material|blue
TV Adverts|Promotion|red
```

### expenditures.txt

Format: `ID|Description|Amount|DateTime|Category|AccountID|ReceiptPath`

```
EXP001|Cement purchase|1200.00|2025-07-01T09:15:00|Cement|ACC001|receipts/cement_receipt.pdf
EXP002|TV promo spot|500.00|2025-07-02T11:00:00|TV Adverts|ACC002|receipts/tv_invoice.jpg
EXP003|Brochure printing|300.00|2025-07-03T14:30:00|Printing|ACC002|
EXP004|Site sand|700.00|2025-07-05T10:20:00|Cement|ACC001|receipts/sand_receipt.pdf
```

**Note:** The 7th field (ReceiptPath) is optional and can be empty for expenditures without linked receipts.

### receipts.txt

```
RCT001|EXP001|receipts/cement_july.pdf|2025-07-01T09:30:00
RCT002|EXP002|receipts/tv_spot.jpeg|2025-07-02T11:10:00
RCT003|EXP003|receipts/brochure.pdf|2025-07-03T14:35:00
```

## How to Run

1. **Compile:**
   ```
   javac -d bin src/app/Main.java
   ```

2. **Run:**
   ```
   java -cp bin app.Main
   ```

3. **Interact:**
   Use the CLI menu to add/view/edit expenditures, manage categories/accounts, view alerts, and analytics.

4. **Clean .class files recursively:**
    ```bash
    find . -type f -name "*.class" -delete
    ```

| Operation                        | Data Structure        | Complexity           |
|-----------------------------------|----------------------|----------------------|
| Add Expenditure                  | SimpleArrayList/Map  | O(1)                 |
| Search by ID                     | Map                  | O(1)                 |
| Sort by Category/Date            | Array                | O(n log n)           |
| Category Uniqueness              | Set                  | O(n)                 |
| Receipt Process                  | Queue/Stack          | O(1)                 |
| Alert Processing                 | MinHeap              | O(log n)             |
| Account Lookup/Update            | Map                  | O(1)                 |

## Data Integrity & Rules

- Expenditure must reference valid bank account and category.
- Adding expenditure always debits account atomically.
- Category names are unique.
- Alerts for low balances and overspending.

## Authors

- Group 68, University of Ghana

## License

- Educational use only.
