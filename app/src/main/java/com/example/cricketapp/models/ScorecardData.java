package com.example.cricketapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScorecardData {
    @SerializedName("innings")
    private List<Inning> innings;

    public List<Inning> getInnings() {
        return innings;
    }

    public void setInnings(List<Inning> innings) {
        this.innings = innings;
    }
}
