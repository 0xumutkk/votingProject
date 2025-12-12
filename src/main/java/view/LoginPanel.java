package view;

import controller.AuthenticationController;
import model.Voter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login panel for user authentication.
 * Handles both administrator and voter login.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class LoginPanel extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    /**
     * Constructor for LoginPanel.
     * 
     * @param mainFrame Reference to the main frame for navigation
     */
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
    }
    
    /**
     * Sets up the layout of the panel.
     */
    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Online Voting System - Login");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
        add(titleLabel, gbc);
        
        // Username label
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Username/ID:"), gbc);
        
        // Username field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);
        
        // Password label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);
        
        // Password field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);
    }
    
    /**
     * Sets up event handlers for buttons.
     */
    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Allow Enter key to trigger login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }
    
    /**
     * Handles the login action.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authenticate user
        AuthenticationController authController = new AuthenticationController();
        AuthenticationController.AuthenticationResult result = authController.authenticate(username, password);
        
        if (result.isSuccess()) {
            String userType = result.getUserType();
            
            if ("ADMIN".equals(userType)) {
                // Navigate to admin panel
                mainFrame.showAdminPanel();
                clearFields();
            } else if ("VOTER".equals(userType)) {
                // Navigate to voting panel
                Voter voter = (Voter) result.getUser();
                mainFrame.showVotingPanel(voter);
                clearFields();
            }
        } else {
            // Show error message
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    /**
     * Clears all input fields.
     */
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}

