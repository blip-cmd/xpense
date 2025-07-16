package app;

import app.modules.*;

public class Main {
    public static void main(String[] args) {
        XpenseSystem system = new XpenseSystem(100.0, 5000.0);
        CLIHandler cli = new CLIHandler(system);
        cli.displayMenu();
    }
}