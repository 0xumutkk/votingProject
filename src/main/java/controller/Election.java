package controller;

import model.Candidate;
import utils.DataManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Manages the election state and operations.
 * Implements Singleton pattern to ensure only one election instance exists.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class Election {
    
    /**
     * Enumeration representing the status of an election.
     */
    public enum ElectionStatus {
        /**
         * Election is currently active and accepting votes.
         */
        ACTIVE,
        
        /**
         * Election is closed and no longer accepting votes.
         */
        CLOSED
    }
    
    private static Election instance;
    private ElectionStatus status;
    private Date startDate;
    private Date endDate;
    
    /**
     * Private constructor to enforce Singleton pattern.
     */
    private Election() {
        this.status = ElectionStatus.CLOSED;
    }
    
    /**
     * Gets the singleton instance of Election.
     * 
     * @return The single Election instance
     */
    public static Election getInstance() {
        if (instance == null) {
            instance = new Election();
        }
        return instance;
    }
    
    /**
     * Gets the current election status.
     * 
     * @return The election status (ACTIVE or CLOSED)
     */
    public ElectionStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the election status.
     * 
     * @param status The status to set
     */
    public void setStatus(ElectionStatus status) {
        this.status = status;
    }
    
    /**
     * Gets the election start date.
     * 
     * @return The start date
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * Sets the election start date.
     * 
     * @param startDate The start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Gets the election end date.
     * 
     * @return The end date
     */
    public Date getEndDate() {
        return endDate;
    }
    
    /**
     * Sets the election end date.
     * 
     * @param endDate The end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Starts the election.
     * Sets status to ACTIVE and records the start date.
     */
    public void startElection() {
        this.status = ElectionStatus.ACTIVE;
        this.startDate = new Date();
    }
    
    /**
     * Stops the election.
     * Sets status to CLOSED and records the end date.
     */
    public void stopElection() {
        this.status = ElectionStatus.CLOSED;
        this.endDate = new Date();
    }
    
    /**
     * Calculates and returns the current vote tally.
     * Results are sorted by vote count in descending order.
     * 
     * @return List of candidates sorted by vote count (highest first)
     */
    public List<Candidate> calculateTally() {
        List<Candidate> candidates = DataManager.loadCandidates();
        
        // Sort by vote count in descending order
        candidates.sort(new Comparator<Candidate>() {
            @Override
            public int compare(Candidate c1, Candidate c2) {
                return Integer.compare(c2.getVoteCount(), c1.getVoteCount());
            }
        });
        
        return new ArrayList<>(candidates);
    }
}

