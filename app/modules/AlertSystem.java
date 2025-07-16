package app.modules;

import app.util.MinHeap;

public class AlertSystem {
    private static class Alert {
        String message;
        int priority;

        Alert(String message, int priority) {
            this.message  = message;
            this.priority = priority;
        }
    }

    private final MinHeap<Alert> heap;
    private final double lowBalanceThreshold;
    private final double spendingLimitThreshold;

    public AlertSystem(double lowBalanceThreshold, double spendingLimitThreshold) {
        this.lowBalanceThreshold    = lowBalanceThreshold;
        this.spendingLimitThreshold = spendingLimitThreshold;
        this.heap = new MinHeap<>(new MinHeap.PriorityComparator<Alert>() {
            public int compare(Alert a, Alert b) {
                return a.priority - b.priority;
            }
        });
    }

    public void addAlert(String message, int priority) {
        heap.insert(new Alert(message, priority));
    }

    public String getNextAlert() {
        Alert alert = heap.removeMin();
        return alert == null ? null : alert.message;
    }

    public boolean hasAlerts() {
        return !heap.isEmpty();
    }

    public boolean checkLowFunds(String accountId, double currentBalance) {
        if (currentBalance < lowBalanceThreshold) {
            addAlert("Account " + accountId + " is low on funds: ₵" + currentBalance, 1);
            return true;
        }
        return false;
    }

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