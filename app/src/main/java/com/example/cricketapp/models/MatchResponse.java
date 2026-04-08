package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MatchResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("data")
    private List<Match> data;

    public String getStatus() { return status; }
    public List<Match> getData() { return data; }
}
