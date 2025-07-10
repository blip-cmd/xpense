package app.modules;

/**
 * Manages system alerts and notifications WITHOUT using built‑in data‑structure classes.
 * Implements an internal binary min‑heap where lower priority numbers come out first.
 *
 * Big‑O  (n = number of alerts in the heap)
 * ─────────────────────────────────────────
 * insert / addAlert     :  O(log n)
 * removeMin / getNext   :  O(log n)
 * peek                  :  O(1)
 * displayAllAlerts      :  O(n log n)   (because we repeatedly removeMin)
 */
public class AlertSystem {

    /* ‑‑‑‑‑‑ Nested Alert class ‑‑‑‑‑‑ */
    private static class Alert {
        String message;
        int priority; // lower value = higher priority (e.g., 1 is urgent)

        Alert(String message, int priority) {
            this.message  = message;
            this.priority = priority;
        }
    }

    /* ‑‑‑‑‑‑ Min‑Heap implementation ‑‑‑‑‑‑ */
    private Alert[] heap;   // index‑0 unused for cleaner arithmetic (or use 0‑based—your call)
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    /* thresholds governed by business rules */
    private final double lowBalanceThreshold;
    private final double spendingLimitThreshold;

    /* ---------- Constructor ---------- */
    public AlertSystem(double lowBalanceThreshold,
                       double spendingLimitThreshold) {
        this.lowBalanceThreshold    = lowBalanceThreshold;
        this.spendingLimitThreshold = spendingLimitThreshold;

        /* index‑0 left unused => we allocate +1 */
        heap = new Alert[INITIAL_CAPACITY + 1];
        size = 0;
    }

    /* ---------- Core heap helpers ---------- */

    private void ensureCapacity() {
        if (size >= heap.length - 1) {            // minus 1 because of dummy 0 index
            Alert[] bigger = new Alert[heap.length * 2];
            System.arraycopy(heap, 0, bigger, 0, heap.length);
            heap = bigger;
        }
    }

    private void swap(int i, int j) {
        Alert temp = heap[i];
        heap[i]    = heap[j];
        heap[j]    = temp;
    }

    private void siftUp(int idx) {
        while (idx > 1) {                         // while not at root
            int parent = idx / 2;
            if (heap[idx].priority < heap[parent].priority) {
                swap(idx, parent);
                idx = parent;
            } else break;
        }
    }

    private void siftDown(int idx) {
        while (2 * idx <= size) {                 // while at least one child
            int left  = 2 * idx;
            int right = left + 1;
            int smallest = left;

            if (right <= size &&
                heap[right].priority < heap[left].priority) {
                smallest = right;
            }
            if (heap[smallest].priority < heap[idx].priority) {
                swap(idx, smallest);
                idx = smallest;
            } else break;
        }
    }

    /* ---------- Public API ---------- */

    /** O(log n) insertion */
    public void addAlert(String message, int priority) {
        ensureCapacity();
        heap[++size] = new Alert(message, priority);
        siftUp(size);
    }

    /** O(log n) removal; returns null if empty */
    public String getNextAlert() {
        if (size == 0) return null;
        String topMsg = heap[1].message;
        heap[1] = heap[size--];       // move last element to root and shrink
        siftDown(1);
        return topMsg;
    }

    /** O(1) peek without removal */
    public boolean hasAlerts() {
        return size > 0;
    }

    /** O(log n) check & insert low‑balance alert */
    public boolean checkLowFunds(String accountId, double currentBalance) {
        if (currentBalance < lowBalanceThreshold) {
            addAlert("Account " + accountId + " is low on funds: ₵" + currentBalance, 1);
            return true;
        }
        return false;
    }

    /** O(log n) check & insert overspend alert */
    public boolean checkSpendingLimit(double categorySpending, double limit) {
        if (categorySpending > limit) {
            addAlert("Spending limit exceeded — ₵" +
                     categorySpending + " > ₵" + limit, 2);
            return true;
        }
        return false;
    }

    /** O(n log n) prints & drains alerts one by one */
    public void displayAllAlerts() {
        if (!hasAlerts()) {
            System.out.println("[✔] No active alerts.");
            return;
        }
        System.out.println("=== ALERTS ===");
        while (hasAlerts()) {
            System.out.println("• " + getNextAlert());
        }
    }
}
