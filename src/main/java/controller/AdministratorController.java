package controller;

import model.Candidate;
import model.Voter;
import utils.DataManager;
import utils.PasswordUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date; // Added for election date management
import java.util.List;

/**
 * Handles administrator operations for managing candidates.
 * All operations are synchronized with CSV files via DataManager.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class AdministratorController {
    
    /**
     * Adds a new candidate to the system.
     * 
     * @param candidateId The unique identifier for the candidate
     * @param name The name of the candidate
     * @param position The position the candidate is running for
     * @return true if candidate was successfully added, false otherwise
     */
    public boolean addCandidate(String candidateId, String name, String position) {
        if (candidateId == null || name == null || position == null) {
            return false;
        }
        
        // Load existing candidates
        List<Candidate> candidates = DataManager.loadCandidates();
        
        // Check if candidate ID already exists
        for (Candidate candidate : candidates) {
            if (candidate.getCandidateId().equals(candidateId)) {
                return false; // Candidate ID already exists
            }
        }
        
        // Create new candidate
        Candidate newCandidate = new Candidate(candidateId, name, position);
        candidates.add(newCandidate);
        
        // Save to CSV
        DataManager.saveCandidates(candidates);
        
        return true;
    }
    
    /**
     * Updates an existing candidate's information.
     * 
     * @param candidateId The unique identifier of the candidate to update
     * @param name The new name for the candidate
     * @param position The new position for the candidate
     * @return true if candidate was successfully updated, false otherwise
     */
    public boolean updateCandidate(String candidateId, String name, String position) {
        if (candidateId == null || name == null || position == null) {
            return false;
        }
        
        // Load existing candidates
        List<Candidate> candidates = DataManager.loadCandidates();
        
        // Find and update candidate
        for (int i = 0; i < candidates.size(); i++) {
            Candidate candidate = candidates.get(i);
            if (candidate.getCandidateId().equals(candidateId)) {
                // Update candidate information (preserve vote count)
                candidate.setName(name);
                candidate.setPosition(position);
                
                // Save to CSV
                DataManager.saveCandidates(candidates);
                return true;
            }
        }
        
        return false; // Candidate not found
    }
    
    /**
     * Deletes a candidate from the system.
     * 
     * @param candidateId The unique identifier of the candidate to delete
     * @return true if candidate was successfully deleted, false otherwise
     */
    public boolean deleteCandidate(String candidateId) {
        if (candidateId == null) {
            return false;
        }
        
        // Load existing candidates
        List<Candidate> candidates = DataManager.loadCandidates();
        
        // Find and remove candidate
        List<Candidate> updatedCandidates = new ArrayList<>();
        boolean found = false;
        
        for (Candidate candidate : candidates) {
            if (!candidate.getCandidateId().equals(candidateId)) {
                updatedCandidates.add(candidate);
            } else {
                found = true;
            }
        }
        
        if (found) {
            // Save updated list to CSV
            DataManager.saveCandidates(updatedCandidates);
            return true;
        }
        
        return false; // Candidate not found
    }
    
    /**
     * Adds a new voter to the system.
     * 
     * @param voterId The unique identifier for the voter
     * @param password The plain text password (will be hashed)
     * @return true if voter was successfully added, false if duplicate ID exists
     */
    public boolean addVoter(String voterId, String password) {
        if (voterId == null || password == null || voterId.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }
        
        // Load existing voters
        List<Voter> voters = DataManager.loadVoters();
        
        // Check if voter ID already exists
        for (Voter voter : voters) {
            if (voter.getId().equals(voterId.trim())) {
                return false; // Voter ID already exists
            }
        }
        
        // Hash the password
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        // Create new voter
        Voter newVoter = new Voter(voterId.trim(), hashedPassword);
        voters.add(newVoter);
        
        // Save to CSV
        DataManager.saveVoters(voters);
        
        return true;
    }
    
    /**
     * Deletes a voter from the system.
     * 
     * @param voterId The unique identifier of the voter to delete
     * @return true if voter was successfully deleted, false otherwise
     */
    public boolean deleteVoter(String voterId) {
        if (voterId == null || voterId.trim().isEmpty()) {
            return false;
        }
        
        // Load existing voters
        List<Voter> voters = DataManager.loadVoters();
        
        // Find and remove voter
        List<Voter> updatedVoters = new ArrayList<>();
        boolean found = false;
        
        for (Voter voter : voters) {
            if (!voter.getId().equals(voterId.trim())) {
                updatedVoters.add(voter);
            } else {
                found = true;
            }
        }
        
        if (found) {
            // Save updated list to CSV
            DataManager.saveVoters(updatedVoters);
            return true;
        }
        
        return false; // Voter not found
    }
    
    /**
     * Imports voters from a CSV file (FR-A07).
     * Expected format: Header row "voterId,password" followed by data rows.
     * Duplicate voterIds are skipped to preserve existing voting records.
     * 
     * @param file The CSV file to import from
     * @return A summary string indicating success and duplicate count
     * @throws IOException if file reading fails
     */
    public String importVotersFromCSV(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist or is null");
        }

        // Load existing voters
        List<Voter> existingVoters = DataManager.loadVoters();

        // Create a set of existing voter IDs for quick lookup
        java.util.Set<String> existingIds = new java.util.HashSet<>();
        for (Voter voter : existingVoters) {
            existingIds.add(voter.getId());
        }

        int importedCount = 0;
        int duplicateCount = 0;
        int errorCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Read header

            // Validate header
            if (line == null || !line.trim().toLowerCase().startsWith("voterid")) {
                throw new IOException("Invalid CSV format. Expected header: voterId,password");
            }

            // Read data rows
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 2) {
                        errorCount++;
                        continue;
                    }

                    String voterId = parts[0].trim();
                    String plainPassword = parts[1].trim();

                    // Skip if voterId is empty
                    if (voterId.isEmpty() || plainPassword.isEmpty()) {
                        errorCount++;
                        continue;
                    }

                    // Check for duplicates (preserve existing voter data)
                    if (existingIds.contains(voterId)) {
                        duplicateCount++;
                        continue; // Skip duplicate, preserve existing hasVoted status
                    }

                    // Hash the password
                    String hashedPassword = PasswordUtils.hashPassword(plainPassword);

                    // Create new voter
                    Voter newVoter = new Voter(voterId, hashedPassword);
                    existingVoters.add(newVoter);
                    existingIds.add(voterId); // Add to set to prevent duplicates in same import

                    importedCount++;
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Error processing voter CSV line: " + line);
                    e.printStackTrace(); // Print stack trace
                    // Continue processing other lines
                }
            }
        }

        // Save all voters (existing + new)
        DataManager.saveVoters(existingVoters);

        // Build summary message
        StringBuilder summary = new StringBuilder();
        summary.append(importedCount).append(" voter(s) imported successfully");

        if (duplicateCount > 0) {
            summary.append(", ").append(duplicateCount).append(" duplicate(s) skipped");
        }

        if (errorCount > 0) {
            summary.append(", ").append(errorCount).append(" error(s) encountered");
        }

        return summary.toString();
    }

    /**
     * Sets the start and end dates for the election.
     *
     * @param startDate The start date of the election.
     * @param endDate The end date of the election.
     * @return true if dates are set successfully, false otherwise.
     */
    public boolean setElectionDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return false;
        }
        Election election = Election.getInstance();
        election.setStartDate(startDate);
        election.setEndDate(endDate);
        // In a real application, you might want to save the election state to DataManager here.
        return true;
    }

    /**
     * Starts the election, setting its status to ACTIVE.
     * The election can only be started if it's currently CLOSED and dates are set.
     *
     * @return true if the election was successfully started, false otherwise.
     */
    public boolean startElection() {
        Election election = Election.getInstance();
        if (election.getStatus() == Election.ElectionStatus.CLOSED && election.getStartDate() != null && election.getEndDate() != null) {
            // Check if current date is within the election period (optional, based on detailed requirements)
            // For simplicity, we are allowing start if status is CLOSED and dates are set.
            election.startElection();
            // In a real application, you might want to save the election state to DataManager here.
            return true;
        }
        return false;
    }

    /**
     * Stops the election, setting its status to CLOSED.
     * The election can only be stopped if it's currently ACTIVE.
     *
     * @return true if the election was successfully stopped, false otherwise.
     */
    public boolean stopElection() {
        Election election = Election.getInstance();
        if (election.getStatus() == Election.ElectionStatus.ACTIVE) {
            election.stopElection();
            // In a real application, you might want to save the election state to DataManager here.
            return true;
        }
        return false;
    }

    /**
     * Retrieves the current status of the election.
     *
     * @return The current ElectionStatus.
     */
    public Election.ElectionStatus getElectionStatus() {
        return Election.getInstance().getStatus();
    }

    /**
     * Calculates and returns the current vote tally from the Election instance.
     * Results are sorted by vote count in descending order.
     *
     * @return List of candidates sorted by vote count (highest first)
     */
    public List<Candidate> calculateTally() {
        return Election.getInstance().calculateTally();
    }
}

