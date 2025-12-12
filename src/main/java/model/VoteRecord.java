package model;

import java.util.Date;

/**
 * Represents a vote record in the online voting system.
 * This class stores information about a single vote cast by a voter for a candidate.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class VoteRecord {
    
    /**
     * The unique identifier of the voter who cast this vote.
     */
    private String voterId;
    
    /**
     * The unique identifier of the candidate who received this vote.
     */
    private String candidateId;
    
    /**
     * The timestamp when the vote was cast.
     */
    private Date timestamp;
    
    /**
     * Default constructor for VoteRecord.
     * Initializes timestamp to the current date and time.
     */
    public VoteRecord() {
        this.timestamp = new Date();
    }
    
    /**
     * Constructor with voter ID and candidate ID parameters.
     * Automatically sets timestamp to the current date and time.
     * 
     * @param voterId The ID of the voter who cast the vote
     * @param candidateId The ID of the candidate who received the vote
     */
    public VoteRecord(String voterId, String candidateId) {
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.timestamp = new Date();
    }
    
    /**
     * Constructor with all parameters.
     * 
     * @param voterId The ID of the voter who cast the vote
     * @param candidateId The ID of the candidate who received the vote
     * @param timestamp The timestamp when the vote was cast
     */
    public VoteRecord(String voterId, String candidateId, Date timestamp) {
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.timestamp = timestamp;
    }
    
    /**
     * Gets the voter ID associated with this vote record.
     * 
     * @return The voter's ID
     */
    public String getVoterId() {
        return voterId;
    }
    
    /**
     * Sets the voter ID for this vote record.
     * 
     * @param voterId The voter's ID to set
     */
    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }
    
    /**
     * Gets the candidate ID associated with this vote record.
     * 
     * @return The candidate's ID
     */
    public String getCandidateId() {
        return candidateId;
    }
    
    /**
     * Sets the candidate ID for this vote record.
     * 
     * @param candidateId The candidate's ID to set
     */
    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
    
    /**
     * Gets the timestamp when the vote was cast.
     * 
     * @return The vote timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }
    
    /**
     * Sets the timestamp for this vote record.
     * 
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Returns a string representation of the VoteRecord object.
     * 
     * @return A string containing the voter ID, candidate ID, and timestamp
     */
    @Override
    public String toString() {
        return "VoteRecord{" +
                "voterId='" + voterId + '\'' +
                ", candidateId='" + candidateId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

