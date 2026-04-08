package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScorecardResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("data")
    private ScorecardData data;

    public String getStatus() { return status; }
    public ScorecardData getData() { return data; }
}
