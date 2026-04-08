package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Match {
    @SerializedName("id")
    private String id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("matchType")
    private String matchType;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("statusText")
    private String statusText;
    
    @SerializedName("venue")
    private String venue;
    
    @SerializedName("date")
    private String date;
    
    @SerializedName("teams")
    private List<String> teams;
    
    @SerializedName("score")
    private List<Score> score;
    
    @SerializedName("matchStarted")
    private Boolean matchStarted;
    
    @SerializedName("matchEnded")
    private Boolean matchEnded;

    @SerializedName("playerOfMatch")
    private String playerOfMatch;

    // Getters with safety checks
    public String getId() { return id; }
    public String getName() { return name != null ? name : "Unknown Match"; }
    public String getMatchType() { return matchType; }
    public String getStatus() { return status != null ? status : "upcoming"; }
    public String getStatusText() { return statusText != null ? statusText : getStatus(); }
    public String getVenue() { return venue != null ? venue : "TBA"; }
    public String getDate() { return date; }
    public List<String> getTeams() { return teams; }
    public List<Score> getScore() { return score; }
    public String getPlayerOfMatch() { return playerOfMatch; }
    
    public boolean hasMatchStarted() { return matchStarted != null && matchStarted; }
    public boolean hasMatchEnded() { return matchEnded != null && matchEnded; }
    
    public boolean isLive() {
        return hasMatchStarted() && !hasMatchEnded();
    }
    
    public boolean isCompleted() {
        return hasMatchEnded();
    }
    
    public boolean isUpcoming() {
        return !hasMatchStarted();
    }
}
