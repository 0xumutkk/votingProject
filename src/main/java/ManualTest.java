import controller.Election;
import controller.VotingController;
import model.Candidate;
import model.Voter;

/**
 * Manual test class for validating duplicate vote prevention (FR-C01).
 * This class programmatically tests that a voter cannot cast a vote more than once.
 * 
 * Test Case: TC-01
 * Requirement: FR-C01 (Single Vote Rule)
 * 
 * @author Online Voting System
 * @version 1.0
 */
public class ManualTest {
    
    /**
     * Main method to execute the duplicate vote prevention test.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("TEST CASE TC-01: Duplicate Vote Prevention");
        System.out.println("Requirement: FR-C01 (Single Vote Rule)");
        System.out.println("==========================================");
        System.out.println();
        
        // Step 1: Create test data
        System.out.println("STEP 1: Creating test data...");
        Voter testVoter = new Voter("TEST001", "testpassword");
        Candidate testCandidate = new Candidate("CAND001", "Test Candidate", "Test Position");
        
        System.out.println("  - Voter created: ID=" + testVoter.getId() + ", hasVoted=" + testVoter.isHasVoted());
        System.out.println("  - Candidate created: ID=" + testCandidate.getCandidateId() + 
                          ", Name=" + testCandidate.getName());
        System.out.println();
        
        // Step 2: Start the election (required for voting)
        System.out.println("STEP 2: Starting election...");
        Election election = Election.getInstance();
        election.startElection();
        System.out.println("  - Election status: " + election.getStatus());
        System.out.println();
        
        // Step 3: Create VotingController
        System.out.println("STEP 3: Initializing VotingController...");
        VotingController votingController = new VotingController();
        System.out.println("  - VotingController ready");
        System.out.println();
        
        // Step 4: First vote attempt (should succeed)
        System.out.println("STEP 4: Attempting first vote...");
        boolean firstVoteResult = votingController.castVote(testVoter, testCandidate);
        System.out.println("  - First vote result: " + firstVoteResult);
        System.out.println("  - Voter hasVoted status: " + testVoter.isHasVoted());
        System.out.println("  - Candidate vote count: " + testCandidate.getVoteCount());
        System.out.println();
        
        // Step 5: Second vote attempt (should fail)
        System.out.println("STEP 5: Attempting second vote (duplicate)...");
        boolean secondVoteResult = votingController.castVote(testVoter, testCandidate);
        System.out.println("  - Second vote result: " + secondVoteResult);
        System.out.println("  - Voter hasVoted status: " + testVoter.isHasVoted());
        System.out.println("  - Candidate vote count: " + testCandidate.getVoteCount());
        System.out.println();
        
        // Step 6: Validate test results
        System.out.println("STEP 6: Validating test results...");
        boolean testPassed = false;
        
        if (firstVoteResult == true && secondVoteResult == false) {
            if (testVoter.isHasVoted() == true) {
                if (testCandidate.getVoteCount() == 1) {
                    testPassed = true;
                    System.out.println("  ✓ First vote succeeded (expected)");
                    System.out.println("  ✓ Second vote rejected (expected)");
                    System.out.println("  ✓ Voter hasVoted flag is true (expected)");
                    System.out.println("  ✓ Candidate vote count is 1 (expected - not incremented twice)");
                } else {
                    System.out.println("  ✗ Candidate vote count is incorrect: " + testCandidate.getVoteCount() + " (expected: 1)");
                }
            } else {
                System.out.println("  ✗ Voter hasVoted flag is false (expected: true)");
            }
        } else {
            if (firstVoteResult != true) {
                System.out.println("  ✗ First vote failed (expected: success)");
            }
            if (secondVoteResult != false) {
                System.out.println("  ✗ Second vote succeeded (expected: failure)");
            }
        }
        
        System.out.println();
        System.out.println("==========================================");
        
        // Final result
        if (testPassed) {
            System.out.println("**TEST PASSED: Duplicate vote prevented**");
            System.out.println();
            System.out.println("Conclusion: The system correctly enforces the single vote rule (FR-C01).");
            System.out.println("  - First vote: ACCEPTED ✓");
            System.out.println("  - Second vote: REJECTED ✓");
            System.out.println("  - hasVoted flag: CORRECTLY SET ✓");
            System.out.println("  - Vote count: NOT INCREMENTED TWICE ✓");
        } else {
            System.out.println("**TEST FAILED: Duplicate vote prevention not working correctly**");
            System.out.println();
            System.out.println("Conclusion: The system failed to prevent duplicate voting.");
        }
        
        System.out.println("==========================================");
        
        // Cleanup: Stop election
        election.stopElection();
        System.out.println("Election stopped. Test completed.");
    }
}

