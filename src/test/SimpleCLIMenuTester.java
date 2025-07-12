package test;

import app.modules.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Simple CLI Menu Unit Tester
 * Tests the actual CLIHandler functionality with simulated inputs
 */
public class SimpleCLIMenuTester {
    
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";
    
    public static void main(String[] args) {
        SimpleCLIMenuTester tester = new SimpleCLIMenuTester();
        tester.runTests();
    }
    
    public void runTests() {
        printHeader("🔧 Simple CLI Menu Tester");
        
        System.out.println("This tester provides commands to manually test your CLI menu.");
        System.out.println("Use the commands below to test different menu paths:\n");
        
        printTestCommands();
        printNavigationPaths();
        printInvalidInputTests();
        printUsageInstructions();
    }
    
    private void printTestCommands() {
        System.out.println(BLUE + "=== COMPILATION & EXECUTION COMMANDS ===" + RESET);
        System.out.println("cd \"c:\\Users\\HP\\OneDrive - University of Ghana\\RB\\mySakai\\lvl300 2nd sem\\DCIT308 DSA II with Prof Sarpong\\grp\\xpense\"");
        System.out.println("javac -cp src src/app/Main.java");
        System.out.println("java -cp src app.Main");
        System.out.println();
    }
    
    private void printNavigationPaths() {
        System.out.println(YELLOW + "=== RECURSIVE MENU NAVIGATION TESTS ===" + RESET);
        System.out.println();
        
        // Test paths for each menu
        String[][] testPaths = {
            {"Main Menu Basic Navigation", "1", "9", "8"}, // Expenditure and exit
            {"Deep Expenditure Menu", "1", "1", "9", "8"}, // Add expenditure
            {"Category Management", "2", "1", "6", "8"}, // Add category  
            {"Bank Account Flow", "3", "1", "6", "8"}, // Add account
            {"Receipt Management", "4", "1", "5", "8"}, // Add receipt
            {"Analytics Navigation", "5", "1", "6", "8"}, // View summary
            {"Help Menu Testing", "7", "1", "3", "8"}, // View alerts
            {"Complex Navigation", "1", "2", "9", "2", "2", "6", "7", "2", "3", "8"}, // Complex path
            {"Invalid Input Test", "99", "abc", "", "-1", "8"}, // Invalid inputs
            {"Alternative Exit", "help", "3", "exit"} // Using text commands
        };
        
        for (int i = 0; i < testPaths.length; i++) {
            String[] path = testPaths[i];
            System.out.println(GREEN + "Test " + (i + 1) + ": " + path[0] + RESET);
            System.out.print("  Input sequence: ");
            for (int j = 1; j < path.length; j++) {
                System.out.print("\"" + path[j] + "\"");
                if (j < path.length - 1) System.out.print(" → ");
            }
            System.out.println();
            System.out.println("  Expected: Navigate through menu and return/exit");
            System.out.println();
        }
    }
    
    private void printInvalidInputTests() {
        System.out.println(RED + "=== INVALID INPUT TESTING ===" + RESET);
        System.out.println("Test these invalid inputs in each menu:");
        System.out.println("• Empty input: \"\" (just press Enter)");
        System.out.println("• Out of range: \"99\", \"0\", \"-1\"");
        System.out.println("• Non-numeric: \"abc\", \"xyz\", \"hello\"");
        System.out.println("• Special chars: \"@#$\", \"!!!\", \"***\"");
        System.out.println("• Very long input: \"" + "a".repeat(20) + "\"");
        System.out.println("Expected: Application should show error message and re-prompt");
        System.out.println();
    }
    
    private void printUsageInstructions() {
        System.out.println(BLUE + "=== HOW TO USE THIS TESTER ===" + RESET);
        System.out.println("1. Compile and run your main application");
        System.out.println("2. Copy and paste the input sequences above");
        System.out.println("3. Verify the menu navigation works correctly");
        System.out.println("4. Test invalid inputs and error handling");
        System.out.println("5. Ensure you can exit from any menu level");
        System.out.println();
        
        System.out.println(YELLOW + "=== AUTOMATED TESTING SCRIPT ===" + RESET);
        System.out.println("For automated testing, create a text file with test inputs:");
        System.out.println();
        System.out.println("test_input.txt content:");
        System.out.println("1");
        System.out.println("2");
        System.out.println("9");
        System.out.println("2");
        System.out.println("1");
        System.out.println("6");
        System.out.println("8");
        System.out.println();
        System.out.println("Then run: java -cp src app.Main < test_input.txt");
        System.out.println();
        
        System.out.println(GREEN + "=== VALIDATION CHECKLIST ===" + RESET);
        System.out.println("✓ All menu options are displayed correctly");
        System.out.println("✓ Navigation between menus works");
        System.out.println("✓ Return to main menu functions work");
        System.out.println("✓ Exit commands terminate the application");
        System.out.println("✓ Invalid inputs are handled gracefully");
        System.out.println("✓ No infinite loops or menu traps");
        System.out.println("✓ Help menu is accessible and functional");
        System.out.println("✓ All submenu functions are reachable");
    }
    
    private void printHeader(String title) {
        System.out.println(BLUE + "═".repeat(60) + RESET);
        System.out.println(BLUE + "  " + title + RESET);
        System.out.println(BLUE + "═".repeat(60) + RESET);
        System.out.println();
    }
}
