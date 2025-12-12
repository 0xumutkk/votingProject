package model;

/**
 * Represents a voter in the online voting system.
 * This class stores voter identification, voting status, and authentication credentials.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class Voter {
    
    /**
     * Unique identifier for the voter.
     */
    private String id;
    
    /**
     * Indicates whether this voter has already cast a vote.
     * Set to true after a vote is successfully cast.
     */
    private boolean hasVoted;
    
    /**
     * Password for voter authentication.
     */
    private String password;
    
    /**
     * Default constructor for Voter.
     * Initializes hasVoted to false.
     */
    public Voter() {
        this.hasVoted = false;
    }
    
    /**
     * Constructor with all parameters.
     * 
     * @param id The unique identifier for the voter
     * @param password The password for authentication
     */
    public Voter(String id, String password) {
        this.id = id;
        this.password = password;
        this.hasVoted = false;
    }
    
    /**
     * Gets the voter's unique identifier.
     * 
     * @return The voter's ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Sets the voter's unique identifier.
     * 
     * @param id The voter's ID to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Checks if the voter has already cast a vote.
     * 
     * @return true if the voter has voted, false otherwise
     */
    public boolean isHasVoted() {
        return hasVoted;
    }
    
    /**
     * Sets the voting status of the voter.
     * 
     * @param hasVoted true if the voter has voted, false otherwise
     */
    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
    
    /**
     * Gets the voter's password.
     * 
     * @return The voter's password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the voter's password.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Returns a string representation of the Voter object.
     * 
     * @return A string containing the voter's ID and voting status
     */
    @Override
    public String toString() {
        return "Voter{" +
                "id='" + id + '\'' +
                ", hasVoted=" + hasVoted +
                '}';
    }
}

