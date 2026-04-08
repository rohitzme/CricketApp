package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Score {
    @SerializedName("r")
    private Integer runs;
    
    @SerializedName("w")
    private Integer wickets;
    
    @SerializedName("o")
    private Double overs;
    
    @SerializedName("inning")
    private String inning;

    // Getters
    public Integer getRuns() { return runs != null ? runs : 0; }
    public Integer getWickets() { return wickets != null ? wickets : 0; }
    public Double getOvers() { return overs != null ? overs : 0.0; }
    public String getInning() { return inning; }
}
