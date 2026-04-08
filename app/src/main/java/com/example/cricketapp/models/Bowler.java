package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;

public class Bowler {
    @SerializedName("bowler")
    private Player bowler;
    
    @SerializedName("o")
    private String overs;
    
    @SerializedName("r")
    private String runs;
    
    @SerializedName("w")
    private String wickets;
    
    @SerializedName("eco")
    private String economy;
    
    public String getName() { return bowler != null && bowler.name != null ? bowler.name : "Unknown"; }
    public String getOvers() { return overs != null ? overs : "0"; }
    public String getRuns() { return runs != null ? runs : "0"; }
    public String getWickets() { return wickets != null ? wickets : "0"; }
    public String getEconomy() { return economy != null ? economy : "0.0"; }
    
    public static class Player {
        @SerializedName("name")
        public String name;
    }
}
