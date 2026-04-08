package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;

public class Batsman {
    @SerializedName("batsman")
    private Player batsman;
    
    @SerializedName("r")
    private String runs;
    
    @SerializedName("b")
    private String balls;
    
    @SerializedName("4s")
    private String fours;
    
    @SerializedName("6s")
    private String sixes;
    
    @SerializedName("sr")
    private String strikeRate;
    
    public String getName() { return batsman != null && batsman.name != null ? batsman.name : "Unknown"; }
    public String getRuns() { return runs != null ? runs : "0"; }
    public String getBalls() { return balls != null ? balls : "0"; }
    public String getFours() { return fours != null ? fours : "0"; }
    public String getSixes() { return sixes != null ? sixes : "0"; }
    public String getStrikeRate() { return strikeRate != null ? strikeRate : "0.0"; }
    
    public static class Player {
        @SerializedName("name")
        public String name;
    }
}
