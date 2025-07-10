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

        boolean running = true;
        while (running) {
            showMainMenu();
            String input = getUserInput();
            switch (input.toLowerCase()) {
                case "1":
                    alertSystem.displayAllAlerts();
                    break;
                case "2":
                    addDummyAlert();
                    break;
                case "3":
                case "help":
                    showHelpMenu();
                    break;
                case "4":
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Type '3' or 'help' for assistance.");
            }
        }

        System.out.println("Exiting Xpense CLI. Goodbye!");
    }

    private void showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. View Alerts");
        System.out.println("2. Add Dummy Alert");
        System.out.println("3. Help Menu");
        System.out.println("4. Exit");
        System.out.print("Select an option: ");
    }

    private void addDummyAlert() {
        System.out.print("Enter alert message: ");
        String message = scanner.nextLine();
        System.out.print("Enter priority (1 = high, 5 = low): ");
        int priority = Integer.parseInt(scanner.nextLine());
        alertSystem.addAlert(message, priority);
        System.out.println("[+] Alert added.");
    }

    private void showHelpMenu() {
        boolean inHelp = true;
        while (inHelp) {
            System.out.println("\n--- HELP MENU ---");
            System.out.println("1. View all system alerts");
            System.out.println("2. Add a test alert manually");
            System.out.println("3. Return to Main Menu");
            System.out.print("Select a help option: ");
            String helpInput = getUserInput();
            switch (helpInput) {
                case "1":
                    alertSystem.displayAllAlerts();
                    break;
                case "2":
                    addDummyAlert();
                    break;
                case "3":
                case "exit":
                    inHelp = false;
                    break;
                default:
                    System.out.println("Invalid help option. Please select 1, 2, or 3.");
            }
        }
    }

    private String getUserInput() {
        return scanner.nextLine();
    }
}
