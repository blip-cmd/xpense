/**
 * Main.java
 * 
 * Entry point for the Nkwa Real Estate Expenditure Management System (Xpense).
 * This application provides a comprehensive offline, locally-stored, command-line
 * expenditure tracking solution for real estate companies.
 * 
 * The system manages expenditures, bank accounts, categories, receipts, and provides
 * analytics and alerting capabilities using custom-built data structures only.
 * 
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app;

import app.modules.*;

/**
 * Main class that serves as the entry point for the Xpense application.
 * 
 * This class initializes the core XpenseSystem with default alert thresholds
 * and launches the command-line interface for user interaction.
 * 
 * The application starts with:
 * - Low balance alert threshold: $100.0
 * - Spending limit alert threshold: $5000.0
 */
public class Main {
    
    /**
     * Main method that starts the Xpense application.
     * 
     * Initializes the XpenseSystem with predefined thresholds for alerts:
     * - Low balance threshold of $100.0 triggers alerts when account balance drops below this amount
     * - Spending limit threshold of $5000.0 triggers alerts when expenditures exceed this amount
     * 
     * After initialization, the CLI interface is launched to provide interactive
     * menu-driven access to all system features.
     * 
     * @param args Command line arguments (currently unused)
     */
    public static void main(String[] args) {
        // Initialize the core expense management system with alert thresholds
        XpenseSystem system = new XpenseSystem(100.0, 5000.0);
        
        // Create and launch the command-line interface
        CLIHandler cli = new CLIHandler(system);
        cli.displayMenu();
    }
}