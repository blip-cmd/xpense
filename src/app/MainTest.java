package app;

import app.modules.AlertSystem;
import app.modules.CLIHandler;
import app.modules.Expenditure;
import app.modules.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class MainTest {
    public static void main(String[] args) {
        System.out.println("=== MainTest: Testing imports and basic instantiation ===");

        // Test AlertSystem import and instantiation
        AlertSystem alertSystem = new AlertSystem(100.0, 5000.0);
        System.out.println("AlertSystem created: " + (alertSystem != null));

        // Test CLIHandler import and instantiation
        CLIHandler cliHandler = new CLIHandler(alertSystem);
        System.out.println("CLIHandler created: " + (cliHandler != null));

        // Test Category import and instantiation
        Category category = new Category("CAT001", "General", "General expenses", "blue");
        System.out.println("Category created: " + (category != null));
        System.out.println("Category name: " + category.getName());

        // Test Expenditure import and instantiation
        Expenditure expenditure = new Expenditure(
                "EXP001",
                "Test Expense",
                new Float("50.00"),
                category,
                LocalDateTime.now(),
                "Test Location",
                null,
                null
        );
        System.out.println("Expenditure created: " + (expenditure != null));
        System.out.println("Expenditure description: " + expenditure.getDescription());

        System.out.println("=== MainTest: All import tests completed ===");
    }
}