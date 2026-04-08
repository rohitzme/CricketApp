package com.example.cricketapp.logic;

import com.example.cricketapp.models.Score;
import java.util.List;

public class WinProbabilityCalculator {

    // Returns a string like "55% - 45%"
    public static int[] calculate(List<Score> scores) {
        if (scores == null || scores.isEmpty()) {
            return new int[]{50, 50}; // default
        }

        if (scores.size() == 1) {
            Score firstInning = scores.get(0);
            int runs = firstInning.getRuns();
            int wickets = firstInning.getWickets();
            double overs = firstInning.getOvers();
            
            // Simple logic for first inning
            int prob = 50 + (int)((runs / (overs > 0 ? overs : 1)) * 2) - (wickets * 3);
            prob = clamp(prob);
            return new int[]{prob, 100 - prob};
        } else if (scores.size() == 2) {
            Score team1 = scores.get(0);
            Score team2 = scores.get(1);
            
            int target = team1.getRuns() + 1;
            int currentRuns = team2.getRuns();
            int wicketsLeft = 10 - team2.getWickets();
            double oversBowled = team2.getOvers();
            double oversLeft = 20.0 - oversBowled; // assuming T20 as default
            
            int runsNeeded = target - currentRuns;
            
            if (runsNeeded <= 0) return new int[]{0, 100}; // Team 2 won
            if (wicketsLeft <= 0) return new int[]{100, 0}; // Team 1 won
            
            double crr = oversBowled > 0 ? currentRuns / oversBowled : 0;
            double rrr = oversLeft > 0 ? runsNeeded / oversLeft : 99;
            
            int winProbTeam2 = 50;
            if (rrr > 10) winProbTeam2 -= (int)((rrr - 10) * 5);
            else if (rrr < 8) winProbTeam2 += (int)((8 - rrr) * 5);
            
            winProbTeam2 += (wicketsLeft - 5) * 5;
            winProbTeam2 = clamp(winProbTeam2);
            
            return new int[]{100 - winProbTeam2, winProbTeam2};
        }

        return new int[]{50, 50};
    }

    private static int clamp(int val) {
        if (val < 1) return 1;
        if (val > 99) return 99;
        return val;
    }
}
