package controller;

import model.Candidate;
import model.VoteRecord;
import model.Voter;
import utils.DataManager;

import java.util.List;

/**
 * Handles voting operations and vote casting logic.
 * Ensures election is active and voters can only vote once.
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class VotingController {
    
    /**
     * Casts a vote for a candidate by a voter.
     * Performs validation checks and updates all necessary data.
     * 
     * @param voter The voter casting the vote
     * @param candidate The candidate receiving the vote
     * @return true if vote was successfully cast, false otherwise
     */
    public boolean castVote(Voter voter, Candidate candidate) {
        // Check if election is active
        Election election = Election.getInstance();
        if (election.getStatus() != Election.ElectionStatus.ACTIVE) {
            return false;
        }
        
        // Check if voter has already voted
        if (voter.isHasVoted()) {
            return false;
        }
        
        // Validate voter and candidate are not null
        if (voter == null || candidate == null) {
            return false;
        }
        
        // Perform vote operations
        candidate.incrementVoteCount();
        voter.setHasVoted(true);
        
        // Create vote record
        VoteRecord voteRecord = new VoteRecord(voter.getId(), candidate.getCandidateId());
        
        // Load all data
        List<Voter> voters = DataManager.loadVoters();
        List<Candidate> candidates = DataManager.loadCandidates();
        List<VoteRecord> voteRecords = DataManager.loadVoteRecords();
        
        // Update voter in list
        for (int i = 0; i < voters.size(); i++) {
            if (voters.get(i).getId().equals(voter.getId())) {
                voters.set(i, voter);
                break;
            }
        }
        
        // Update candidate in list
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getCandidateId().equals(candidate.getCandidateId())) {
                candidates.set(i, candidate);
                break;
            }
        }
        
        // Add vote record
        voteRecords.add(voteRecord);
        
        // Save all updated data to CSV files
        DataManager.saveVoters(voters);
        DataManager.saveCandidates(candidates);
        DataManager.saveVoteRecords(voteRecords);
        
        return true;
    }
}

