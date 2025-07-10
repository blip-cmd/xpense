package app;

import app.modules.CLIHandler;
import app.modules.Expenditure;

public class Main {
    public static void main(String[] args) {
        CLIHandler cli = new CLIHandler();
        cli.displayMenu();

        Expenditure expense = new Expenditure(null, null, null, null, null, null);
        // Example usage of Expenditure class
        // Assuming Expenditure has methods to set properties

        expense.setId("EKPK34");
        expense.setDescription("Sample Expense");
       
        expense.setLocation("Supermarket");

        
    }
}
