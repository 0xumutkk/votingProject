package utils;

import model.Administrator;
import model.Candidate;
import model.VoteRecord;
import model.Voter;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Manages CSV file I/O operations for the online voting system.
 * Handles loading and saving of voters, candidates, vote records, and administrators.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class DataManager {
    
    private static final String VOTERS_FILE = "voters.csv";
    private static final String CANDIDATES_FILE = "candidates.csv";
    private static final String VOTES_FILE = "votes.csv";
    private static final String ADMINISTRATORS_FILE = "administrators.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Loads all voters from the CSV file.
     * 
     * @return List of Voter objects
     */
    public static List<Voter> loadVoters() {
        List<Voter> voters = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(VOTERS_FILE))) {
            String line = reader.readLine(); // Skip header
            if (line == null) {
                return voters; // Empty file
            }
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Voter voter = new Voter();
                    voter.setId(parts[0].trim());
                    // hasVoted is managed internally by the application, not imported directly via CSV for initial setup
                    voter.setPassword(parts[1].trim());
                    voters.add(voter);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            return voters;
        } catch (IOException e) {
            System.err.println("Error loading voters: " + e.getMessage());
        }
        
        return voters;
    }
    
    /**
     * Saves all voters to the CSV file.
     * 
     * @param voters List of Voter objects to save
     */
    public static void saveVoters(List<Voter> voters) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VOTERS_FILE))) {
            // Write header
            writer.write("id,password");
            writer.newLine();
            
            // Write data
            for (Voter voter : voters) {
                writer.write(voter.getId() + "," + 
                           voter.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving voters: " + e.getMessage());
        }
    }
    
    /**
     * Loads all candidates from the CSV file.
     * 
     * @return List of Candidate objects
     */
    public static List<Candidate> loadCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CANDIDATES_FILE))) {
            String line = reader.readLine(); // Skip header
            if (line == null) {
                return candidates; // Empty file
            }
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Candidate candidate = new Candidate();
                    candidate.setCandidateId(parts[0].trim());
                    candidate.setName(parts[1].trim());
                    candidate.setPosition(parts[2].trim());
                    candidate.setVoteCount(Integer.parseInt(parts[3].trim()));
                    candidates.add(candidate);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            return candidates;
        } catch (IOException e) {
            System.err.println("Error loading candidates: " + e.getMessage());
        }
        
        return candidates;
    }
    
    /**
     * Saves all candidates to the CSV file.
     * 
     * @param candidates List of Candidate objects to save
     */
    public static void saveCandidates(List<Candidate> candidates) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CANDIDATES_FILE))) {
            // Write header
            writer.write("candidateId,name,position,voteCount");
            writer.newLine();
            
            // Write data
            for (Candidate candidate : candidates) {
                writer.write(candidate.getCandidateId() + "," + 
                           candidate.getName() + "," + 
                           candidate.getPosition() + "," + 
                           candidate.getVoteCount());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving candidates: " + e.getMessage());
        }
    }
    
    /**
     * Loads all vote records from the CSV file.
     * 
     * @return List of VoteRecord objects
     */
    public static List<VoteRecord> loadVoteRecords() {
        List<VoteRecord> voteRecords = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(VOTES_FILE))) {
            String line = reader.readLine(); // Skip header
            if (line == null) {
                return voteRecords; // Empty file
            }
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    VoteRecord record = new VoteRecord();
                    record.setVoterId(parts[0].trim());
                    record.setCandidateId(parts[1].trim());
                    
                    try {
                        Date timestamp = DATE_FORMAT.parse(parts[2].trim());
                        record.setTimestamp(timestamp);
                    } catch (ParseException e) {
                        // If parsing fails, use current date
                        record.setTimestamp(new Date());
                    }
                    
                    voteRecords.add(record);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            return voteRecords;
        } catch (IOException e) {
            System.err.println("Error loading vote records: " + e.getMessage());
        }
        
        return voteRecords;
    }
    
    /**
     * Saves all vote records to the CSV file.
     * 
     * @param voteRecords List of VoteRecord objects to save
     */
    public static void saveVoteRecords(List<VoteRecord> voteRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VOTES_FILE))) {
            // Write header
            writer.write("voterId,candidateId,timestamp");
            writer.newLine();
            
            // Write data
            for (VoteRecord record : voteRecords) {
                writer.write(record.getVoterId() + "," + 
                           record.getCandidateId() + "," + 
                           DATE_FORMAT.format(record.getTimestamp()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving vote records: " + e.getMessage());
        }
    }
    
    /**
     * Loads all administrators from the CSV file.
     * 
     * @return List of Administrator objects
     */
    public static List<Administrator> loadAdministrators() {
        List<Administrator> administrators = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINISTRATORS_FILE))) {
            String line = reader.readLine(); // Skip header
            if (line == null) {
                return administrators; // Empty file
            }
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Administrator admin = new Administrator();
                    admin.setUsername(parts[0].trim());
                    admin.setPassword(parts[1].trim());
                    administrators.add(admin);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            return administrators;
        } catch (IOException e) {
            System.err.println("Error loading administrators: " + e.getMessage());
        }
        
        return administrators;
    }
    
    /**
     * Saves all administrators to the CSV file.
     * 
     * @param administrators List of Administrator objects to save
     */
    public static void saveAdministrators(List<Administrator> administrators) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMINISTRATORS_FILE))) {
            // Write header
            writer.write("username,password");
            writer.newLine();
            
            // Write data
            for (Administrator admin : administrators) {
                writer.write(admin.getUsername() + "," + PasswordUtils.hashPassword(admin.getPassword()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving administrators: " + e.getMessage());
        }
    }
}

