package app.modules;

import app.util.MinHeap;

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
    private final MinHeap<Alert> heap;

    /* thresholds governed by business rules */
    private final double lowBalanceThreshold;
    private final double spendingLimitThreshold;

    /* ---------- Constructor ---------- */
    public AlertSystem(double lowBalanceThreshold,
                       double spendingLimitThreshold) {
        this.lowBalanceThreshold    = lowBalanceThreshold;
        this.spendingLimitThreshold = spendingLimitThreshold;
        this.heap = new MinHeap<>(new MinHeap.PriorityComparator<Alert>() {
            public int compare(Alert a, Alert b) {
                return a.priority - b.priority;
            }
        });
    }

    /* ---------- Public API ---------- */

    /** O(log n) insertion */
    public void addAlert(String message, int priority) {
        heap.insert(new Alert(message, priority));
    }

    /** O(log n) removal; returns null if empty */
    public String getNextAlert() {
        Alert alert = heap.removeMin();
        return alert == null ? null : alert.message;
    }

    /** O(1) peek without removal */
    public boolean hasAlerts() {
        return !heap.isEmpty();
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
    public boolean checkSpendingLimit(double categorySpending, Double limit) {
        double effectiveLimit = (limit != null) ? limit : spendingLimitThreshold;
        if (categorySpending > effectiveLimit) {
            addAlert("Spending limit exceeded — ₵" +
                     categorySpending + " > ₵" + effectiveLimit, 2);
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
