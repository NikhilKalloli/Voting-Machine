package com.voting.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vote")
public class VoteController {

    private int numberOfVoters;
    private Map<String, Integer> voteCounts = new HashMap<>(); // Define the voteCounts map

    @GetMapping("/start")
    public String showVotersForm() {
        return "voters-form";
    }

    @PostMapping("/start")
    public String startVoting(@RequestParam int numberOfVoters, Model model) {
        this.numberOfVoters = numberOfVoters;
        model.addAttribute("numberOfVoters", numberOfVoters);
        // model.addAttribute("candidates", new String[]{"Candidate A", "Candidate B"});
        return "redirect:/vote/castVote/1"; 
    }

    @GetMapping("/castVote/{voterNumber}")
    public String showVoteForm(@PathVariable int voterNumber, Model model) {
        model.addAttribute("voterNumber", voterNumber);
        voterNumber++;
        // model.addAttribute("candidates", new String[]{"Candidate A", "Candidate B"});
        return "vote";
    }

    @PostMapping("/submitVote")
public String processVote(@RequestParam int voterNumber, @RequestParam String selectedCandidate, Model model) {
    System.out.println("Voter " + voterNumber + " voted for: " + selectedCandidate);

    // Count the vote for the selected candidate
    countVote(selectedCandidate);

    // Continue to the next voter or finish if all voters have voted
    if (voterNumber < numberOfVoters) {
        return "redirect:/vote/castVote/" + (voterNumber + 1);
    } else {
        // Determine the winner based on the vote counts
        String winner = determineWinner();
        model.addAttribute("winner", winner);
        return "voting-completed";
    }
}


    private void countVote(String selectedCandidate) {
        // Increment the vote count for the selected candidate
        voteCounts.put(selectedCandidate, voteCounts.getOrDefault(selectedCandidate, 0) + 1);
    }

    private String determineWinner() {
        // Find the candidate with the maximum votes
        int maxVotes = 0;
        String winner = "Tie";

        for (Map.Entry<String, Integer> entry : voteCounts.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                winner = entry.getKey();
            } else if (entry.getValue() == maxVotes) {
                winner = "Tie"; 
            }
        }

        return winner;
    }
}

