package view;

import controller.AdministratorController;
import controller.Election; // Re-added for ElectionStatus type resolution
import model.Candidate;
import model.Voter;
import utils.DataManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Admin panel for managing candidates, controlling elections, and viewing results.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class AdminPanel extends JPanel {
    
    private MainFrame mainFrame;
    private JTabbedPane tabbedPane;
    
    // Candidate Management Components
    private JTextField candidateIdField;
    private JTextField candidateNameField;
    private JTextField candidatePositionField;
    private JTable candidatesTable;
    private DefaultTableModel candidatesTableModel;
    
    // Election Control Components
    private JLabel electionStatusLabel;
    private JTextField startDateField;
    private JTextField endDateField;
    private JButton setElectionDatesButton;
    private JButton startElectionButton;
    private JButton stopElectionButton;
    
    // Results Components
    private JTable resultsTable;
    private DefaultTableModel resultsTableModel;
    private JButton refreshResultsButton;
    
    // Voter Management Components
    private JTextField voterIdField;
    private JPasswordField voterPasswordField;
    private JTable votersTable;
    private DefaultTableModel votersTableModel;
    
    /**
     * Constructor for AdminPanel.
     * 
     * @param mainFrame Reference to the main frame for navigation
     */
    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Candidate Management Components
        candidateIdField = new JTextField(15);
        candidateNameField = new JTextField(15);
        candidatePositionField = new JTextField(15);
        
        String[] columnNames = {"Candidate ID", "Name", "Position", "Vote Count"};
        candidatesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        candidatesTable = new JTable(candidatesTableModel);
        
        // Election Control Components
        electionStatusLabel = new JLabel("Status: CLOSED");
        startDateField = new JTextField(16);
        endDateField = new JTextField(16);
        setElectionDatesButton = new JButton("Set Election Dates");
        startElectionButton = new JButton("Start Election");
        stopElectionButton = new JButton("Stop Election");
        
        // Results Components
        String[] resultsColumnNames = {"Name", "Position", "Vote Count"};
        resultsTableModel = new DefaultTableModel(resultsColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(resultsTableModel);
        refreshResultsButton = new JButton("Refresh Results");
        
        // Voter Management Components
        voterIdField = new JTextField(15);
        voterPasswordField = new JPasswordField(15);
        
        String[] voterColumnNames = {"Voter ID", "Has Voted"};
        votersTableModel = new DefaultTableModel(voterColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        votersTable = new JTable(votersTableModel);
    }
    
    /**
     * Sets up the layout of the panel.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create tabs
        tabbedPane.addTab("Manage Candidates", createCandidateManagementPanel());
        tabbedPane.addTab("Manage Voters", createVoterManagementPanel());
        tabbedPane.addTab("Election Control", createElectionControlPanel());
        tabbedPane.addTab("View Results", createResultsPanel());
        tabbedPane.addTab("Logout", createLogoutPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the candidate management panel.
     * 
     * @return JPanel containing candidate management components
     */
    private JPanel createCandidateManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Candidate ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Candidate ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(candidateIdField, gbc);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(candidateNameField, gbc);
        
        // Position
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        formPanel.add(candidatePositionField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Candidate");
        JButton updateButton = new JButton("Update Candidate");
        JButton deleteButton = new JButton("Delete Candidate");
        
        addButton.addActionListener(e -> handleAddCandidate());
        updateButton.addActionListener(e -> handleUpdateCandidate());
        deleteButton.addActionListener(e -> handleDeleteCandidate());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(candidatesTable);
        candidatesTable.setFillsViewportHeight(true);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the voter management panel.
     * 
     * @return JPanel containing voter management components
     */
    private JPanel createVoterManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Section A: Manual Registration
        JPanel manualPanel = new JPanel(new BorderLayout());
        manualPanel.setBorder(BorderFactory.createTitledBorder("Manual Registration"));
        JPanel manualFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Voter ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        manualFormPanel.add(new JLabel("Voter ID:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        manualFormPanel.add(voterIdField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        manualFormPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        manualFormPanel.add(voterPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registerButton = new JButton("Register Single Voter");
        JButton deleteButton = new JButton("Delete Voter");
        
        registerButton.addActionListener(e -> handleRegisterVoter());
        deleteButton.addActionListener(e -> handleDeleteVoter());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(deleteButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        manualFormPanel.add(buttonPanel, gbc);
        
        manualPanel.add(manualFormPanel, BorderLayout.NORTH);
        
        // Section B: Bulk Import
        JPanel bulkPanel = new JPanel(new BorderLayout());
        bulkPanel.setBorder(BorderFactory.createTitledBorder("Bulk Import (FR-A07)"));
        JPanel bulkFormPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton importButton = new JButton("Import Voters from CSV");
        importButton.addActionListener(e -> handleImportVoters());
        bulkFormPanel.add(importButton);
        
        bulkPanel.add(bulkFormPanel, BorderLayout.NORTH);
        
        // Create split pane for sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(manualPanel);
        splitPane.setBottomComponent(bulkPanel);
        splitPane.setDividerLocation(180);
        splitPane.setResizeWeight(0.4);
        
        // Voters table
        JScrollPane scrollPane = new JScrollPane(votersTable);
        votersTable.setFillsViewportHeight(true);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Voters"));
        
        // Combine sections
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setTopComponent(splitPane);
        mainSplitPane.setBottomComponent(scrollPane);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setResizeWeight(0.5);
        
        panel.add(mainSplitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the election control panel.
     * 
     * @return JPanel containing election control components
     */
    private JPanel createElectionControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Status label
        // Status label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4; // Span across 4 columns
        electionStatusLabel.setFont(new Font(electionStatusLabel.getFont().getName(), Font.BOLD, 16));
        panel.add(electionStatusLabel, gbc);

        // Start Date
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Start Date (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridx = 1;
        panel.add(startDateField, gbc);

        // End Date
        gbc.gridx = 2;
        panel.add(new JLabel("End Date (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridx = 3;
        panel.add(endDateField, gbc);

        // Set Dates Button
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 4; // Span across 4 columns
        panel.add(setElectionDatesButton, gbc);

        // Election Control Buttons
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span across 2 columns
        panel.add(startElectionButton, gbc);

        gbc.gridx = 2;
        panel.add(stopElectionButton, gbc);
        
        updateElectionStatus();
        
        return panel;
    }
    
    /**
     * Creates the results panel.
     * 
     * @return JPanel containing results components
     */
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshResultsButton);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        resultsTable.setFillsViewportHeight(true);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the logout panel.
     * 
     * @return JPanel containing logout button
     */
    private JPanel createLogoutPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.addActionListener(e -> mainFrame.showLogin());
        
        panel.add(logoutButton, gbc);
        
        return panel;
    }
    
    /**
     * Sets up event handlers.
     */
    private void setupEventHandlers() {
        startElectionButton.addActionListener(e -> handleStartElection());
        stopElectionButton.addActionListener(e -> handleStopElection());
        setElectionDatesButton.addActionListener(e -> handleSetElectionDates());
        refreshResultsButton.addActionListener(e -> refreshResults());
    }
    
    /**
     * Handles adding a new candidate.
     */
    private void handleAddCandidate() {
        String candidateId = candidateIdField.getText().trim();
        String name = candidateNameField.getText().trim();
        String position = candidatePositionField.getText().trim();
        
        if (candidateId.isEmpty() || name.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AdministratorController controller = new AdministratorController();
        if (controller.addCandidate(candidateId, name, position)) {
            JOptionPane.showMessageDialog(this,
                    "Candidate added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearCandidateFields();
            refreshCandidatesTable();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add candidate. Candidate ID may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handles updating a candidate.
     */
    private void handleUpdateCandidate() {
        String candidateId = candidateIdField.getText().trim();
        String name = candidateNameField.getText().trim();
        String position = candidatePositionField.getText().trim();
        
        if (candidateId.isEmpty() || name.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AdministratorController controller = new AdministratorController();
        if (controller.updateCandidate(candidateId, name, position)) {
            JOptionPane.showMessageDialog(this,
                    "Candidate updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearCandidateFields();
            refreshCandidatesTable();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update candidate. Candidate ID may not exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handles deleting a candidate.
     */
    private void handleDeleteCandidate() {
        String candidateId = candidateIdField.getText().trim();
        
        if (candidateId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Candidate ID.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete candidate: " + candidateId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            AdministratorController controller = new AdministratorController();
            if (controller.deleteCandidate(candidateId)) {
                JOptionPane.showMessageDialog(this,
                        "Candidate deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearCandidateFields();
                refreshCandidatesTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete candidate. Candidate ID may not exist.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Handles starting the election.
     */
    private void handleStartElection() {
        AdministratorController adminController = new AdministratorController();
        if (adminController.startElection()) {
            updateElectionStatus();
            JOptionPane.showMessageDialog(this,
                    "Election started successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to start election. It might already be active or dates are not set.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handles stopping the election.
     */
    private void handleStopElection() {
        AdministratorController adminController = new AdministratorController();
        if (adminController.stopElection()) {
            updateElectionStatus();
            JOptionPane.showMessageDialog(this,
                    "Election stopped successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to stop election. It might already be closed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates the election status display.
     */
    private void updateElectionStatus() {
        AdministratorController adminController = new AdministratorController();
        Election.ElectionStatus status = adminController.getElectionStatus();

        electionStatusLabel.setText("Status: " + status.toString());

        startElectionButton.setEnabled(status == Election.ElectionStatus.CLOSED);
        stopElectionButton.setEnabled(status == Election.ElectionStatus.ACTIVE);
    }
    
    /**
     * Refreshes the candidates table.
     */
    private void refreshCandidatesTable() {
        candidatesTableModel.setRowCount(0);
        List<Candidate> candidates = DataManager.loadCandidates();
        
        for (Candidate candidate : candidates) {
            candidatesTableModel.addRow(new Object[]{
                    candidate.getCandidateId(),
                    candidate.getName(),
                    candidate.getPosition(),
                    candidate.getVoteCount()
            });
        }
    }
    
    /**
     * Refreshes the results table.
     */
    private void refreshResults() {
        resultsTableModel.setRowCount(0);
        AdministratorController adminController = new AdministratorController();
        List<Candidate> results = adminController.calculateTally();

        for (Candidate candidate : results) {
            resultsTableModel.addRow(new Object[]{
                    candidate.getName(),
                    candidate.getPosition(),
                    candidate.getVoteCount()
            });
        }
    }
    
    /**
     * Clears candidate input fields.
     */
    private void clearCandidateFields() {
        candidateIdField.setText("");
        candidateNameField.setText("");
        candidatePositionField.setText("");
    }
    
    /**
     * Handles registering a single voter.
     */
    private void handleRegisterVoter() {
        String voterId = voterIdField.getText().trim();
        String password = new String(voterPasswordField.getPassword());
        
        if (voterId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AdministratorController controller = new AdministratorController();
        if (controller.addVoter(voterId, password)) {
            JOptionPane.showMessageDialog(this,
                    "Voter registered successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearVoterFields();
            refreshVotersTable();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register voter. Voter ID may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handles deleting a voter.
     */
    private void handleDeleteVoter() {
        String voterId = voterIdField.getText().trim();
        
        if (voterId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Voter ID.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete voter: " + voterId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            AdministratorController controller = new AdministratorController();
            if (controller.deleteVoter(voterId)) {
                JOptionPane.showMessageDialog(this,
                        "Voter deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                clearVoterFields();
                refreshVotersTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete voter. Voter ID may not exist.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Handles importing voters from CSV file (FR-A07).
     */
    private void handleImportVoters() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File to Import Voters");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            try {
                AdministratorController controller = new AdministratorController();
                String summary = controller.importVotersFromCSV(selectedFile);
                
                JOptionPane.showMessageDialog(this,
                        summary,
                        "Import Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                
                refreshVotersTable();
            } catch (Exception e) {
                // Log the exception details to the console for debugging
                System.err.println("Error during voter import in AdminPanel:");
                e.printStackTrace(); // Print the stack trace here

                JOptionPane.showMessageDialog(this,
                        "Error importing voters: " + e.getMessage(),
                        "Import Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Refreshes the voters table.
     */
    private void refreshVotersTable() {
        votersTableModel.setRowCount(0);
        List<Voter> voters = DataManager.loadVoters();
        
        for (Voter voter : voters) {
            votersTableModel.addRow(new Object[]{
                    voter.getId(),
                    voter.isHasVoted() ? "Yes" : "No"
            });
        }
    }
    
    /**
     * Clears voter input fields.
     */
    private void clearVoterFields() {
        voterIdField.setText("");
        voterPasswordField.setText("");
    }
    
    /**
     * Refreshes all data in the panel.
     */
    public void refreshData() {
        refreshCandidatesTable();
        refreshVotersTable();
        updateElectionStatus();
        refreshResults();
    }

    /**
     * Handles setting the election start and end dates.
     */
    private void handleSetElectionDates() {
        String startDateStr = startDateField.getText().trim();
        String endDateStr = endDateField.getText().trim();

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both start and end dates.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date startDate = dateFormat.parse(startDateStr);
            java.util.Date endDate = dateFormat.parse(endDateStr);

            AdministratorController adminController = new AdministratorController();
            if (adminController.setElectionDates(startDate, endDate)) {
                JOptionPane.showMessageDialog(this,
                        "Election dates set successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                updateElectionStatus();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to set election dates. Please ensure start date is before end date.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use yyyy-MM-dd HH:mm (e.g., 2025-12-31 23:59).",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

