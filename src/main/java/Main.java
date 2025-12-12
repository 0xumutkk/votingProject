import model.Administrator;
import utils.DataManager;
import utils.PasswordUtils;
import view.MainFrame;

import javax.swing.*;
import java.util.List;

/**
 * Main entry point for the Online Voting System application.
 * Initializes data, ensures default administrator exists, and launches the GUI.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method to launch the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize data and ensure default admin exists
        initializeApplication();
        
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * Initializes the application data.
     * Loads all data and ensures default administrator exists.
     */
    private static void initializeApplication() {
        // Load all data to validate CSV files exist
        DataManager.loadVoters();
        DataManager.loadCandidates();
        DataManager.loadVoteRecords();
        DataManager.loadAdministrators();
        
        // Ensure default admin exists
        ensureDefaultAdmin();
    }
    
    /**
     * Ensures that a default administrator account exists.
     * Creates one if it doesn't exist.
     */
    private static void ensureDefaultAdmin() {
        List<Administrator> administrators = DataManager.loadAdministrators();
        
        // Check if admin "admin" exists
        boolean adminExists = false;
        for (Administrator admin : administrators) {
            if ("admin".equals(admin.getUsername())) {
                adminExists = true;
                break;
            }
        }
        
        // Create default admin if it doesn't exist
        if (!adminExists) {
            String hashedPassword = PasswordUtils.hashPassword("admin");
            Administrator defaultAdmin = new Administrator("admin", hashedPassword);
            administrators.add(defaultAdmin);
            DataManager.saveAdministrators(administrators);
            System.out.println("Default administrator created: username='admin', password='admin'");
        }
    }
    
    /**
     * Creates and displays the main GUI frame.
     */
    private static void createAndShowGUI() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system L&F fails
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Create and show main frame
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
