package app;

import app.modules.AlertSystem;
import app.modules.CLIHandler;
import app.modules.Expenditure;
import app.modules.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main { 

    public static void main(String[] args) {
        AlertSystem alertSystem = new AlertSystem(100.0, 5000.0);
        CLIHandler cli = new CLIHandler(alertSystem);
        
        // Create a proper category first
        Category category = new Category("CAT001", "General", "General expenses", "blue");
        
        // Create expenditure with proper parameters
        Expenditure expense = new Expenditure(
            "EKPK34", 
            "Sample Expense", 
            new BigDecimal("25.50"), 
            category, 
            LocalDateTime.now(), 
            "Supermarket"
        );
        
        // Start the CLI
        cli.displayMenu();
    }
}

