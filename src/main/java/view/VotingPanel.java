package view;

import controller.VotingController;
import model.Candidate;
import model.Voter;
import utils.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Voting panel for voters to cast their votes.
 * Displays candidates and allows selection and voting.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class VotingPanel extends JPanel {
    
    private MainFrame mainFrame;
    private Voter currentVoter;
    private ButtonGroup candidateButtonGroup;
    private JPanel candidatesPanel;
    private JButton voteButton;
    private JButton logoutButton;
    private JLabel statusLabel;
    private List<Candidate> candidates;
    
    /**
     * Constructor for VotingPanel.
     * 
     * @param mainFrame Reference to the main frame for navigation
     * @param currentVoter The voter who is using this panel
     */
    public VotingPanel(MainFrame mainFrame, Voter currentVoter) {
        this.mainFrame = mainFrame;
        this.currentVoter = currentVoter;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshCandidates();
    }
    
    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        candidateButtonGroup = new ButtonGroup();
        candidatesPanel = new JPanel();
        candidatesPanel.setLayout(new BoxLayout(candidatesPanel, BoxLayout.Y_AXIS));
        
        voteButton = new JButton("Vote");
        logoutButton = new JButton("Logout");
        statusLabel = new JLabel();
    }
    
    /**
     * Sets up the layout of the panel.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Cast Your Vote");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        // Candidates panel with scroll
        JScrollPane scrollPane = new JScrollPane(candidatesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        // Status label
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font(statusLabel.getFont().getName(), Font.ITALIC, 14));
        add(statusLabel, BorderLayout.SOUTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(voteButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        updateVotingStatus();
    }
    
    /**
     * Sets up event handlers.
     */
    private void setupEventHandlers() {
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleVote();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLogin();
            }
        });
    }
    
    /**
     * Refreshes the candidates list and updates the display.
     */
    public void refreshCandidates() {
        candidates = DataManager.loadCandidates();
        candidatesPanel.removeAll();
        candidateButtonGroup = new ButtonGroup(); // Create new ButtonGroup
        
        if (candidates.isEmpty()) {
            JLabel noCandidatesLabel = new JLabel("No candidates available.");
            noCandidatesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            candidatesPanel.add(noCandidatesLabel);
        } else {
            for (Candidate candidate : candidates) {
                JRadioButton radioButton = new JRadioButton(
                        candidate.getName() + " - " + candidate.getPosition()
                );
                radioButton.setActionCommand(candidate.getCandidateId());
                candidateButtonGroup.add(radioButton);
                candidatesPanel.add(radioButton);
            }
        }
        
        candidatesPanel.revalidate();
        candidatesPanel.repaint();
        updateVotingStatus();
    }
    
    /**
     * Handles the vote action.
     */
    private void handleVote() {
        // Check if a candidate is selected
        ButtonModel selectedButton = candidateButtonGroup.getSelection();
        if (selectedButton == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a candidate before voting.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedCandidateId = selectedButton.getActionCommand();
        
        // Find the candidate object
        Candidate selectedCandidate = null;
        for (Candidate candidate : candidates) {
            if (candidate.getCandidateId().equals(selectedCandidateId)) {
                selectedCandidate = candidate;
                break;
            }
        }
        
        if (selectedCandidate == null) {
            JOptionPane.showMessageDialog(this,
                    "Selected candidate not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cast the vote
        VotingController votingController = new VotingController();
        boolean success = votingController.castVote(currentVoter, selectedCandidate);
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Vote cast successfully! Thank you for voting.",
                    "Vote Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Reload voter data to get updated hasVoted status
            List<Voter> voters = DataManager.loadVoters();
            for (Voter voter : voters) {
                if (voter.getId().equals(currentVoter.getId())) {
                    currentVoter = voter;
                    break;
                }
            }
            
            updateVotingStatus();
        } else {
            // Determine failure reason
            String errorMessage = "Vote failed. ";
            if (currentVoter.isHasVoted()) {
                errorMessage += "You have already voted.";
            } else {
                errorMessage += "Election may be closed or an error occurred.";
            }
            
            JOptionPane.showMessageDialog(this,
                    errorMessage,
                    "Vote Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates the voting status and enables/disables controls accordingly.
     */
    private void updateVotingStatus() {
        if (currentVoter != null && currentVoter.isHasVoted()) {
            statusLabel.setText("You have already voted. Thank you!");
            statusLabel.setForeground(Color.RED);
            voteButton.setEnabled(false);
            
            // Disable all radio buttons
            for (Component comp : candidatesPanel.getComponents()) {
                if (comp instanceof JRadioButton) {
                    ((JRadioButton) comp).setEnabled(false);
                }
            }
        } else {
            statusLabel.setText("Please select a candidate and click Vote.");
            statusLabel.setForeground(Color.BLACK);
            voteButton.setEnabled(true);
        }
    }
    
    /**
     * Sets the current voter and refreshes the panel.
     * 
     * @param voter The voter to set
     */
    public void setVoter(Voter voter) {
        this.currentVoter = voter;
        refreshCandidates();
    }
}

