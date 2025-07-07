package app;

import app.modules.CLIHandler;

public class Main {
    public static void main(String[] args) {
        CLIHandler cli = new CLIHandler();
        cli.displayMenu();
    }
}
