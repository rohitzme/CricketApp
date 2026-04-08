package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Inning {
    @SerializedName("inning")
    private String inning;

    @SerializedName("name")
    private String name;
    
    @SerializedName("batting")
    private List<Batsman> batting;
    
    @SerializedName("bowling")
    private List<Bowler> bowling;
    
    public String getName() { 
        if (name != null) return name;
        return inning != null ? inning : "Unknown Inning"; 
    }
    public List<Batsman> getBatting() { return batting; }
    public List<Bowler> getBowling() { return bowling; }
}
