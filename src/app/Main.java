package app;

import app.modules.AlertSystem;
import app.modules.CLIHandler;

public class Main {
    public static void main(String[] args) {
        AlertSystem alertSystem = new AlertSystem(100.0, 5000.0);
        CLIHandler cli = new CLIHandler(alertSystem);
        cli.displayMenu();
    }
}
