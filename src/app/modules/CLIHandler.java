package app.modules;

import java.util.Scanner;

/**
 * Handles the command-line interface and user interaction
 */
public class CLIHandler {
    private AlertSystem alertSystem;
    private Scanner scanner;

    public CLIHandler(AlertSystem alertSystem) {
        this.alertSystem = alertSystem;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Welcome to Xpense - Nkwa Financial Tracker CLI");

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. View Alerts");
            System.out.println("2. Add Dummy Alert");
            System.out.println("3. Help");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    alertSystem.displayAllAlerts();
                    break;
                case "2":
                    System.out.print("Enter alert message: ");
                    String message = scanner.nextLine();
                    System.out.print("Enter priority (1 = high, 5 = low): ");
                    int priority = Integer.parseInt(scanner.nextLine());
                    alertSystem.addAlert(message, priority);
                    System.out.println("[+] Alert added.");
                    break;
                case "3":
                    showHelp();
                    break;
                case "0":
                    System.out.println("Exiting Xpense CLI. Goodbye!");
                    return;
                default:
                    System.out.println("[!] Invalid input. Try again.");
            }
        }
    }

    private void showHelp() {
        System.out.println("\n--- HELP MENU ---");
        System.out.println("1 - View all system alerts");
        System.out.println("2 - Add a test alert manually");
        System.out.println("0 - Exit the application");
    }
}
