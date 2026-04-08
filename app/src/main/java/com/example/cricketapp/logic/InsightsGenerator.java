package com.example.cricketapp.logic;

import com.example.cricketapp.models.Score;
import java.util.List;

public class InsightsGenerator {

    public static String getMatchPhase(List<Score> scores) {
        if (scores == null || scores.isEmpty()) return "Pre-match";
        Score latest = scores.get(scores.size() - 1);
        double overs = latest.getOvers();
        if (overs < 6.0) return "Powerplay";
        if (overs < 15.0) return "Middle Overs";
        return "Death Overs";
    }

    public static String getPressureInsight(List<Score> scores) {
        if (scores == null || scores.size() < 2) return "Building Innings";
        
        Score team1 = scores.get(0);
        Score team2 = scores.get(1);
        
        int target = team1.getRuns() + 1;
        int currentRuns = team2.getRuns();
        double oversBowled = team2.getOvers();
        double oversLeft = 20.0 - oversBowled;
        
        double rrr = oversLeft > 0 ? (target - currentRuns) / oversLeft : 0;
        double crr = oversBowled > 0 ? currentRuns / oversBowled : 0;
        
        if (rrr > 12) return "High Pressure";
        if (crr > rrr) return "Strong Momentum";
        if (Math.abs(rrr - crr) < 1.5) return "Balanced Match";
        
        return "Chasing";
    }
}
