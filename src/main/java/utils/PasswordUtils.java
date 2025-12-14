package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing operations.
 * Implements SHA-256 hashing algorithm for secure password storage.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class PasswordUtils {
    
    /**
     * Hashes a plain text password using SHA-256 algorithm.
     * This method is used for secure password storage (NFR-S01).
     * 
     * @param plainText The plain text password to hash
     * @return A hexadecimal string representation of the SHA-256 hash
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
    public static String hashPassword(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return "";
        }
        
        try {
            // Get instance of SHA-256 MessageDigest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Compute the hash
            byte[] hashBytes = digest.digest(plainText.getBytes());
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Log the error or handle it more gracefully without crashing
            System.err.println("SHA-256 algorithm not available: " + e.getMessage());
            return ""; // Return an empty string or null to indicate failure
        }
    }
}
