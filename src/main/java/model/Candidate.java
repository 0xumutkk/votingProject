package model;

/**
 * Represents a candidate in the online voting system.
 * This class stores candidate information including name, position, and vote count.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class Candidate {
    
    /**
     * Unique identifier for the candidate.
     */
    private String candidateId;
    
    /**
     * The name of the candidate.
     */
    private String name;
    
    /**
     * The position or office the candidate is running for.
     */
    private String position;
    
    /**
     * The total number of votes received by this candidate.
     */
    private int voteCount;
    
    /**
     * Default constructor for Candidate.
     * Initializes voteCount to 0.
     */
    public Candidate() {
        this.voteCount = 0;
    }
    
    /**
     * Constructor with candidate ID, name and position parameters.
     * Initializes voteCount to 0.
     * 
     * @param candidateId The unique identifier for the candidate
     * @param name The name of the candidate
     * @param position The position the candidate is running for
     */
    public Candidate(String candidateId, String name, String position) {
        this.candidateId = candidateId;
        this.name = name;
        this.position = position;
        this.voteCount = 0;
    }
    
    /**
     * Constructor with all parameters.
     * 
     * @param candidateId The unique identifier for the candidate
     * @param name The name of the candidate
     * @param position The position the candidate is running for
     * @param voteCount The initial vote count
     */
    public Candidate(String candidateId, String name, String position, int voteCount) {
        this.candidateId = candidateId;
        this.name = name;
        this.position = position;
        this.voteCount = voteCount;
    }
    
    /**
     * Gets the candidate's unique identifier.
     * 
     * @return The candidate's ID
     */
    public String getCandidateId() {
        return candidateId;
    }
    
    /**
     * Sets the candidate's unique identifier.
     * 
     * @param candidateId The candidate's ID to set
     */
    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
    
    /**
     * Gets the candidate's name.
     * 
     * @return The candidate's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the candidate's name.
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the position the candidate is running for.
     * 
     * @return The candidate's position
     */
    public String getPosition() {
        return position;
    }
    
    /**
     * Sets the position the candidate is running for.
     * 
     * @param position The position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * Gets the current vote count for this candidate.
     * 
     * @return The number of votes received
     */
    public int getVoteCount() {
        return voteCount;
    }
    
    /**
     * Sets the vote count for this candidate.
     * 
     * @param voteCount The vote count to set
     */
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
    
    /**
     * Increments the vote count by 1.
     * This method is typically called when a vote is cast for this candidate.
     */
    public void incrementVoteCount() {
        this.voteCount++;
    }
    
    /**
     * Returns a string representation of the Candidate object.
     * 
     * @return A string containing the candidate's name, position, and vote count
     */
    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId='" + candidateId + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
}

