package com.example.cricketapp.api;

import com.example.cricketapp.models.MatchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CricketApiService {
    
    @GET("v1/currentMatches")
    Call<MatchResponse> getMatches(
        @Query("apikey") String apiKey,
        @Query("offset") int offset
    );

    @GET("v1/cricScore")
    Call<MatchResponse> getCricScore(
        @Query("apikey") String apiKey
    );

    @GET("v1/match_info")
    Call<com.example.cricketapp.models.MatchInfoResponse> getMatchInfo(
        @Query("apikey") String apiKey,
        @Query("id") String matchId
    );

    @GET("v1/match_scorecard")
    Call<com.example.cricketapp.models.ScorecardResponse> getMatchScorecard(
        @Query("apikey") String apiKey,
        @Query("id") String matchId
    );
}
