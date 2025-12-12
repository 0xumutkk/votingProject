package view;

import model.Voter;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window that manages panel switching using CardLayout.
 * This is the primary container for all GUI panels in the Online Voting System.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel cardContainer;
    private LoginPanel loginPanel;
    private AdminPanel adminPanel;
    private VotingPanel votingPanel;
    
    /**
     * Constructor for MainFrame.
     * Initializes the window and sets up the CardLayout with all panels.
     */
    public MainFrame() {
        initializeFrame();
        setupCardLayout();
    }
    
    /**
     * Initializes the main frame properties.
     */
    private void initializeFrame() {
        setTitle("Online Voting System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
    }
    
    /**
     * Sets up the CardLayout and initializes all panels.
     */
    private void setupCardLayout() {
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        
        // Initialize panels
        loginPanel = new LoginPanel(this);
        adminPanel = new AdminPanel(this);
        votingPanel = null; // Will be created when needed with voter context
        
        // Add panels to card layout
        cardContainer.add(loginPanel, "LOGIN");
        cardContainer.add(adminPanel, "ADMIN");
        
        // Add card container to frame
        add(cardContainer);
        
        // Show login panel initially
        showLogin();
    }
    
    /**
     * Switches to the login panel.
     */
    public void showLogin() {
        cardLayout.show(cardContainer, "LOGIN");
        loginPanel.clearFields();
    }
    
    /**
     * Switches to the admin panel.
     */
    public void showAdminPanel() {
        adminPanel.refreshData();
        cardLayout.show(cardContainer, "ADMIN");
    }
    
    /**
     * Switches to the voting panel for the specified voter.
     * Creates a new VotingPanel instance if needed or updates existing one.
     * 
     * @param currentVoter The voter who is logging in
     */
    public void showVotingPanel(Voter currentVoter) {
        if (votingPanel == null) {
            votingPanel = new VotingPanel(this, currentVoter);
            cardContainer.add(votingPanel, "VOTING");
        } else {
            votingPanel.setVoter(currentVoter);
            votingPanel.refreshCandidates();
        }
        cardLayout.show(cardContainer, "VOTING");
    }
}

