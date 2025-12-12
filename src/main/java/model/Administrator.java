package model;

/**
 * Represents an administrator in the online voting system.
 * This class stores administrator credentials for system management.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class Administrator {
    
    /**
     * The username for administrator authentication.
     */
    private String username;
    
    /**
     * The password for administrator authentication.
     */
    private String password;
    
    /**
     * Default constructor for Administrator.
     */
    public Administrator() {
    }
    
    /**
     * Constructor with username and password parameters.
     * 
     * @param username The administrator's username
     * @param password The administrator's password
     */
    public Administrator(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Gets the administrator's username.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the administrator's username.
     * 
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the administrator's password.
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the administrator's password.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Returns a string representation of the Administrator object.
     * Note: Password is not included in the string representation for security reasons.
     * 
     * @return A string containing the administrator's username
     */
    @Override
    public String toString() {
        return "Administrator{" +
                "username='" + username + '\'' +
                '}';
    }
}

