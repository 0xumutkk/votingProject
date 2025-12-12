package controller;

import model.Administrator;
import model.Voter;
import utils.DataManager;
import utils.PasswordUtils;

import java.util.List;

/**
 * Handles authentication operations for both administrators and voters.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class AuthenticationController {
    
    /**
     * Result object returned by the authenticate method.
     * Contains authentication status, user type, and user object.
     */
    public static class AuthenticationResult {
        private boolean success;
        private String userType;
        private Object user;
        
        /**
         * Constructor for AuthenticationResult.
         * 
         * @param success Whether authentication was successful
         * @param userType The type of user ("ADMIN" or "VOTER")
         * @param user The authenticated user object (Administrator or Voter)
         */
        public AuthenticationResult(boolean success, String userType, Object user) {
            this.success = success;
            this.userType = userType;
            this.user = user;
        }
        
        /**
         * Checks if authentication was successful.
         * 
         * @return true if authentication succeeded, false otherwise
         */
        public boolean isSuccess() {
            return success;
        }
        
        /**
         * Gets the user type.
         * 
         * @return "ADMIN" or "VOTER"
         */
        public String getUserType() {
            return userType;
        }
        
        /**
         * Gets the authenticated user object.
         * 
         * @return Administrator or Voter object, or null if authentication failed
         */
        public Object getUser() {
            return user;
        }
    }
    
    /**
     * Authenticates a user (administrator or voter) based on username and password.
     * Uses SHA-256 hashing for password comparison.
     * 
     * @param username The username or voter ID
     * @param password The plain text password
     * @return AuthenticationResult containing success status, user type, and user object
     */
    public AuthenticationResult authenticate(String username, String password) {
        if (username == null || password == null) {
            return new AuthenticationResult(false, null, null);
        }
        
        // Hash the provided password
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        // Try administrator authentication first
        List<Administrator> administrators = DataManager.loadAdministrators();
        for (Administrator admin : administrators) {
            if (admin.getUsername().equals(username) && 
                admin.getPassword().equals(hashedPassword)) {
                return new AuthenticationResult(true, "ADMIN", admin);
            }
        }
        
        // Try voter authentication
        List<Voter> voters = DataManager.loadVoters();
        for (Voter voter : voters) {
            if (voter.getId().equals(username) && 
                voter.getPassword().equals(hashedPassword)) {
                return new AuthenticationResult(true, "VOTER", voter);
            }
        }
        
        // Authentication failed
        return new AuthenticationResult(false, null, null);
    }
}

