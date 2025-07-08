package app.modules;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

/**
 * Manages system alerts and notifications
 */
public class AlertSystem {

    private static class Alert {
        String message;
        int priority; // Lower value = higher priority

        public Alert(String message, int priority) {
            this.message = message;
            this.priority = priority;
        }
    }

    // PriorityQueue based on priority (lower value = higher priority)
    private Queue<Alert> alertQueue;
    private double lowBalanceThreshold;
    private double spendingLimitThreshold;

    /**
     * Constructor for AlertSystem
     */
    public AlertSystem(double lowBalanceThreshold, double spendingLimitThreshold) {
        this.lowBalanceThreshold = lowBalanceThreshold;
        this.spendingLimitThreshold = spendingLimitThreshold;
        this.alertQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a.priority));
    }

    /**
     * Check for low funds alert
     */
    public boolean checkLowFunds(String accountId, double currentBalance) {
        if (currentBalance < lowBalanceThreshold) {
            addAlert("Account " + accountId + " is low on funds: ₵" + currentBalance, 1);
            return true;
        }
        return false;
    }

    /**
     * Add alert to queue
     */
    public void addAlert(String message, int priority) {
        alertQueue.offer(new Alert(message, priority));
    }

    /**
     * Get next alert from queue
     */
    public String getNextAlert() {
        Alert alert = alertQueue.poll();
        return (alert != null) ? alert.message : null;
    }

    /**
     * Check spending limits
     */
    public boolean checkSpendingLimit(double categorySpending, double limit) {
        if (categorySpending > limit) {
            addAlert("Spending limit exceeded for category: ₵" + categorySpending + " > ₵" + limit, 2);
            return true;
        }
        return false;
    }

    /**
     * Show all alerts
     */
    public void displayAllAlerts() {
        if (alertQueue.isEmpty()) {
            System.out.println("[✔] No active alerts.");
        } else {
            System.out.println("=== ALERTS ===");
            while (!alertQueue.isEmpty()) {
                System.out.println("• " + getNextAlert());
            }
        }
    }
}
