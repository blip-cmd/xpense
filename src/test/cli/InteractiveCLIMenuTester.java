package test.cli;

import java.util.*;

/**
 * Interactive CLI Menu Tester
 * A simple tool to recursively test CLI menu navigation by simulating user
 * inputs
 */
public class InteractiveCLIMenuTester {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    private Scanner scanner;
    private List<String> navigationHistory;
    private Map<String, List<String>> menuOptions;

    public InteractiveCLIMenuTester() {
        this.scanner = new Scanner(System.in);
        this.navigationHistory = new ArrayList<>();
        initializeMenuStructure();
    }

    private void initializeMenuStructure() {
        menuOptions = new HashMap<>();

        // Main Menu Options
        menuOptions.put("MAIN", Arrays.asList(
                "1 - Expenditure Management",
                "2 - Category Management",
                "3 - Bank Account Management",
                "4 - Receipt Management",
                "5 - Analytics & Reports",
                "6 - View Alerts",
                "7 - Help Menu",
                "8 - Exit"));

        // Expenditure Menu Options
        menuOptions.put("EXPENDITURE", Arrays.asList(
                "1 - Add New Expenditure",
                "2 - View All Expenditures",
                "3 - Search Expenditures",
                "4 - Sort Expenditures",
                "5 - Edit Expenditure",
                "6 - Delete Expenditure",
                "7 - Filter by Amount Range",
                "8 - View Expenditures by Category",
                "9 - Return to Main Menu"));

        // Help Menu Options
        menuOptions.put("HELP", Arrays.asList(
                "1 - View all system alerts",
                "2 - Add a test alert manually",
                "3 - Return to Main Menu"));

        // Category Menu Options (estimated)
        menuOptions.put("CATEGORY", Arrays.asList(
                "1 - Add Category",
                "2 - View Categories",
                "3 - Edit Category",
                "4 - Delete Category",
                "5 - Search Categories",
                "6 - Return to Main Menu"));

        // Bank Account Menu Options (estimated)
        menuOptions.put("BANK", Arrays.asList(
                "1 - Add Account",
                "2 - View Accounts",
                "3 - Edit Account",
                "4 - Delete Account",
                "5 - View Transactions",
                "6 - Return to Main Menu"));

        // Receipt Menu Options (estimated)
        menuOptions.put("RECEIPT", Arrays.asList(
                "1 - Add Receipt",
                "2 - View Receipts",
                "3 - Search Receipts",
                "4 - Delete Receipt",
                "5 - Return to Main Menu"));

        // Analytics Menu Options (estimated)
        menuOptions.put("ANALYTICS", Arrays.asList(
                "1 - View Summary",
                "2 - Category Analysis",
                "3 - Monthly Reports",
                "4 - Budget Analysis",
                "5 - Export Data",
                "6 - Return to Main Menu"));
    }

    public static void main(String[] args) {
        InteractiveCLIMenuTester tester = new InteractiveCLIMenuTester();
        tester.startTesting();
    }

    public void startTesting() {
        printWelcome();

        boolean running = true;
        while (running) {
            printMainTestMenu();
            String choice = getUserInput();

            switch (choice.toLowerCase()) {
                case "1":
                    recursiveMenuTest();
                    break;
                case "2":
                    manualNavigationTest();
                    break;
                case "3":
                    automatedTestSequence();
                    break;
                case "4":
                    viewNavigationHistory();
                    break;
                case "5":
                    generateTestCommands();
                    break;
                case "6":
                    clearHistory();
                    break;
                case "7":
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println(RED + "Invalid option. Please try again." + RESET);
            }
        }

        System.out.println(GREEN + "CLI Menu Tester exited. Happy testing!" + RESET);
    }

    private void printWelcome() {
        System.out.println(CYAN + "═".repeat(60) + RESET);
        System.out.println(CYAN + "🔧 INTERACTIVE CLI MENU TESTER" + RESET);
        System.out.println(CYAN + "═".repeat(60) + RESET);
        System.out.println("This tool helps you test CLI menu navigation recursively.");
        System.out.println("Use it to validate all menu paths and options.\n");
    }

    private void printMainTestMenu() {
        System.out.println(BLUE + "\n=== CLI MENU TESTER OPTIONS ===" + RESET);
        System.out.println("1. Recursive Menu Test (All Paths)");
        System.out.println("2. Manual Navigation Test");
        System.out.println("3. Automated Test Sequence");
        System.out.println("4. View Navigation History");
        System.out.println("5. Generate Test Commands");
        System.out.println("6. Clear History");
        System.out.println("7. Exit");
        System.out.print("Select an option: ");
    }

    private void recursiveMenuTest() {
        System.out.println(YELLOW + "\n🔄 Starting Recursive Menu Test..." + RESET);

        String currentMenu = "MAIN";
        navigationHistory.add("Started at MAIN menu");

        while (true) {
            displayMenuStructure(currentMenu);

            System.out.print("\nEnter choice (or 'back' to return, 'quit' to exit test): ");
            String input = getUserInput();

            if (input.equalsIgnoreCase("quit")) {
                break;
            } else if (input.equalsIgnoreCase("back")) {
                currentMenu = goBack(currentMenu);
                continue;
            }

            String nextMenu = processMenuChoice(currentMenu, input);
            if (nextMenu != null) {
                navigationHistory.add("From " + currentMenu + " → " + nextMenu + " (choice: " + input + ")");
                currentMenu = nextMenu;
            } else {
                System.out.println(RED + "Invalid choice for " + currentMenu + " menu" + RESET);
            }
        }

        System.out.println(GREEN + "✓ Recursive test completed" + RESET);
    }

    private void manualNavigationTest() {
        System.out.println(YELLOW + "\n📋 Manual Navigation Test" + RESET);
        System.out.println("Enter a sequence of menu choices separated by spaces.");
        System.out.println("Example: 1 2 6 7 3");
        System.out.print("Enter sequence: ");

        String sequence = getUserInput();
        String[] choices = sequence.split("\\s+");

        String currentMenu = "MAIN";
        boolean success = true;

        System.out.println(BLUE + "\nExecuting sequence..." + RESET);

        for (String choice : choices) {
            System.out.println("Processing choice '" + choice + "' in " + currentMenu + " menu");

            String nextMenu = processMenuChoice(currentMenu, choice);
            if (nextMenu != null) {
                navigationHistory.add("Manual: " + currentMenu + " → " + nextMenu + " (choice: " + choice + ")");
                currentMenu = nextMenu;
                System.out.println(GREEN + "✓ Navigated to " + nextMenu + RESET);
            } else {
                System.out.println(RED + "✗ Invalid choice '" + choice + "' for " + currentMenu + " menu" + RESET);
                success = false;
                break;
            }
        }

        if (success) {
            System.out.println(GREEN + "✓ Manual navigation sequence completed successfully" + RESET);
        } else {
            System.out.println(RED + "✗ Manual navigation sequence failed" + RESET);
        }
    }

    private void automatedTestSequence() {
        System.out.println(YELLOW + "\n🤖 Running Automated Test Sequence..." + RESET);

        // Predefined test sequences
        String[][] testSequences = {
                { "1", "1", "9" }, // Main → Expenditure → Add → Back
                { "1", "2", "9" }, // Main → Expenditure → View → Back
                { "2", "1", "6" }, // Main → Category → Add → Back
                { "3", "1", "6" }, // Main → Bank → Add → Back
                { "4", "1", "5" }, // Main → Receipt → Add → Back
                { "5", "1", "6" }, // Main → Analytics → Summary → Back
                { "7", "1", "3" }, // Main → Help → Alerts → Back
                { "1", "3", "9", "2", "2", "6" } // Complex path
        };

        int passed = 0;
        int total = testSequences.length;

        for (int i = 0; i < testSequences.length; i++) {
            String[] sequence = testSequences[i];
            System.out.println("\nTest " + (i + 1) + ": " + Arrays.toString(sequence));

            if (executeTestSequence(sequence)) {
                System.out.println(GREEN + "✓ PASSED" + RESET);
                passed++;
            } else {
                System.out.println(RED + "✗ FAILED" + RESET);
            }
        }

        System.out.println(CYAN + "\n📊 Test Results: " + passed + "/" + total + " passed" + RESET);
        double successRate = (double) passed / total * 100;
        System.out.println("Success Rate: " + String.format("%.1f%%", successRate));
    }

    private boolean executeTestSequence(String[] sequence) {
        String currentMenu = "MAIN";

        for (String choice : sequence) {
            String nextMenu = processMenuChoice(currentMenu, choice);
            if (nextMenu == null) {
                return false;
            }
            currentMenu = nextMenu;
        }

        return true;
    }

    private void viewNavigationHistory() {
        System.out.println(BLUE + "\n📜 Navigation History" + RESET);
        System.out.println("─".repeat(50));

        if (navigationHistory.isEmpty()) {
            System.out.println("No navigation history available.");
            return;
        }

        for (int i = 0; i < navigationHistory.size(); i++) {
            System.out.println((i + 1) + ". " + navigationHistory.get(i));
        }
    }

    private void generateTestCommands() {
        System.out.println(YELLOW + "\n⚙️ Generating Test Commands..." + RESET);

        System.out.println("// Java command to compile and run your CLI:");
        System.out.println("javac -cp src src/app/Main.java");
        System.out.println("java -cp src app.Main");
        System.out.println();

        System.out.println("// Test scenarios to manually verify:");
        System.out.println("1. Navigate: Main → Expenditure → Add → Return");
        System.out.println("2. Navigate: Main → Category → View → Return");
        System.out.println("3. Test invalid inputs in each menu");
        System.out.println("4. Test exit from each submenu");
        System.out.println("5. Test help menu functionality");
        System.out.println();

        System.out.println("// Automated test inputs (copy and paste):");
        System.out.println("Test 1: 1 1 9 8");
        System.out.println("Test 2: 2 2 6 8");
        System.out.println("Test 3: 7 1 3 8");
        System.out.println("Test 4: Invalid inputs: 99 abc -1 @#$");
    }

    private void clearHistory() {
        navigationHistory.clear();
        System.out.println(GREEN + "✓ Navigation history cleared" + RESET);
    }

    private void displayMenuStructure(String menuName) {
        System.out.println(CYAN + "\n=== " + menuName + " MENU STRUCTURE ===" + RESET);

        List<String> options = menuOptions.get(menuName);
        if (options != null) {
            for (String option : options) {
                System.out.println(option);
            }
        } else {
            System.out.println("Menu structure not defined for: " + menuName);
        }
    }

    private String processMenuChoice(String currentMenu, String choice) {
        if (choice == null || choice.trim().isEmpty()) {
            return null;
        }

        switch (currentMenu) {
            case "MAIN":
                switch (choice) {
                    case "1":
                        return "EXPENDITURE";
                    case "2":
                        return "CATEGORY";
                    case "3":
                        return "BANK";
                    case "4":
                        return "RECEIPT";
                    case "5":
                        return "ANALYTICS";
                    case "6":
                        return "MAIN"; // View alerts stays in main
                    case "7":
                        return "HELP";
                    case "8":
                    case "exit":
                        return null; // Exit
                    default:
                        return null;
                }

            case "EXPENDITURE":
                switch (choice) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                        return "EXPENDITURE"; // Stay in expenditure menu
                    case "9":
                        return "MAIN"; // Return to main
                    default:
                        return null;
                }

            case "CATEGORY":
                switch (choice) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return "CATEGORY"; // Stay in category menu
                    case "6":
                        return "MAIN"; // Return to main
                    default:
                        return null;
                }

            case "BANK":
                switch (choice) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return "BANK"; // Stay in bank menu
                    case "6":
                        return "MAIN"; // Return to main
                    default:
                        return null;
                }

            case "RECEIPT":
                switch (choice) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        return "RECEIPT"; // Stay in receipt menu
                    case "5":
                        return "MAIN"; // Return to main
                    default:
                        return null;
                }

            case "ANALYTICS":
                switch (choice) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        return "ANALYTICS"; // Stay in analytics menu
                    case "6":
                        return "MAIN"; // Return to main
                    default:
                        return null;
                }

            case "HELP":
                switch (choice) {
                    case "1":
                    case "2":
                        return "HELP"; // Stay in help menu
                    case "3":
                    case "exit":
                        return "MAIN"; // Return to main
                    default:
                        return null;
                }

            default:
                return null;
        }
    }

    private String goBack(String currentMenu) {
        switch (currentMenu) {
            case "EXPENDITURE":
            case "CATEGORY":
            case "BANK":
            case "RECEIPT":
            case "ANALYTICS":
            case "HELP":
                navigationHistory.add("Returned from " + currentMenu + " to MAIN");
                return "MAIN";
            case "MAIN":
                System.out.println("Already at main menu");
                return "MAIN";
            default:
                return "MAIN";
        }
    }

    private String getUserInput() {
        try {
            return scanner.nextLine().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
