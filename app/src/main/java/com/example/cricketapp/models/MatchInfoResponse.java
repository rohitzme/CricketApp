package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;

public class MatchInfoResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("data")
    private Match data;

    public String getStatus() { return status; }
    public Match getData() { return data; }
}
