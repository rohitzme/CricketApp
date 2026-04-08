package com.example.cricketapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cricketapp.adapters.BattingAdapter;
import com.example.cricketapp.adapters.BowlingAdapter;
import com.example.cricketapp.api.RetrofitClient;
import com.example.cricketapp.databinding.ActivityMatchDetailBinding;
import com.example.cricketapp.logic.InsightsGenerator;
import com.example.cricketapp.logic.WinProbabilityCalculator;
import com.example.cricketapp.models.Inning;
import com.example.cricketapp.models.Match;
import com.example.cricketapp.models.MatchInfoResponse;
import com.example.cricketapp.models.Score;
import com.example.cricketapp.models.ScorecardResponse;
import com.example.cricketapp.utils.Constants;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailActivity extends AppCompatActivity {

    private ActivityMatchDetailBinding binding;
    private String matchId;
    private BattingAdapter battingAdapter;
    private BowlingAdapter bowlingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        matchId = getIntent().getStringExtra("match_id");
        if (matchId == null) {
            finish();
            return;
        }

        binding.btnRetry.setOnClickListener(v -> loadData());
        initAdapters();
        loadData();
    }

    private void initAdapters() {
        battingAdapter = new BattingAdapter(new java.util.ArrayList<>());
        bowlingAdapter = new BowlingAdapter(new java.util.ArrayList<>());
        
        binding.battingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.battingRecyclerView.setHasFixedSize(true);
        binding.battingRecyclerView.setAdapter(battingAdapter);

        binding.bowlingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.bowlingRecyclerView.setHasFixedSize(true);
        binding.bowlingRecyclerView.setAdapter(bowlingAdapter);
    }

    private void loadData() {
        if (binding.btnRetry != null) {
            binding.btnRetry.setEnabled(false);
            binding.btnRetry.setText("Retrying...");
        }
        showLoading();
        fetchMatchInfo();
    }

    private void fetchMatchInfo() {
        Log.d("API_DEBUG", "Fetching match info for ID: " + matchId);
        RetrofitClient.getClient().getMatchInfo(Constants.API_KEY, matchId).enqueue(new Callback<MatchInfoResponse>() {
            @Override
            public void onResponse(Call<MatchInfoResponse> call, Response<MatchInfoResponse> response) {
                Log.d("API_DEBUG", "MatchInfo Response: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    bindMatchInfo(response.body().getData());
                    fetchScorecard();
                } else {
                    Log.d("API_DEBUG", "MatchInfo Body Null or Error");
                    showError();
                    resetRetryButton();
                }
            }

            @Override
            public void onFailure(Call<MatchInfoResponse> call, Throwable t) {
                Log.e("API_DEBUG", "MatchInfo Failure: " + t.getMessage());
                showError();
                resetRetryButton();
            }
        });
    }

    private void resetRetryButton() {
        if (binding.btnRetry != null) {
            binding.btnRetry.setEnabled(true);
            binding.btnRetry.setText("Retry");
        }
    }

    private void fetchScorecard() {
        Log.d("API_DEBUG", "Fetching scorecard for ID: " + matchId);
        RetrofitClient.getClient().getMatchScorecard(Constants.API_KEY, matchId).enqueue(new Callback<ScorecardResponse>() {
            @Override
            public void onResponse(Call<ScorecardResponse> call, Response<ScorecardResponse> response) {
                Log.d("API_DEBUG", "Scorecard Response: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_DEBUG", "RAW SCORECARD: " + new com.google.gson.Gson().toJson(response.body()));
                    
                    String status = response.body().getStatus();
                    if (!"success".equals(status)) {
                        Log.e("API_DEBUG", "Scorecard API failed: " + status);
                        showScorecardUnavailable();
                        binding.statusTag.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (response.body().getData() == null) {
                        showScorecardUnavailable();
                        return;
                    }

                    bindScorecard(response.body().getData());
                    binding.statusTag.setVisibility(View.GONE);
                } else {
                    showScorecardUnavailable();
                }
                showContent();
            }

            @Override
            public void onFailure(Call<ScorecardResponse> call, Throwable t) {
                Log.e("API_DEBUG", "Scorecard Failure: " + t.getMessage());
                showScorecardUnavailable();
                showContent();
            }
        });
    }

    private void bindMatchInfo(Match match) {
        binding.detailMatchName.setText(match.getName());
        
        List<Score> scores = match.getScore();
        if (scores != null && !scores.isEmpty()) {
            Score latest = scores.get(scores.size() - 1);
            binding.detailScore.setText(latest.getRuns() + "/" + latest.getWickets());
            
            String statusLine = latest.getOvers() + " ov ● ";
            if (match.isLive()) {
                statusLine += "LIVE";
                binding.detailStatus.setTextColor(getResources().getColor(com.example.cricketapp.R.color.live_red));
            } else if (match.isCompleted()) {
                statusLine = "RESULT: " + (match.getStatus() != null ? match.getStatus() : "Finished");
                binding.detailStatus.setTextColor(getResources().getColor(com.example.cricketapp.R.color.status_result));
            } else {
                statusLine = match.getStatus();
                binding.detailStatus.setTextColor(getResources().getColor(com.example.cricketapp.R.color.grey_light));
            }
            binding.detailStatus.setText(statusLine);
            
            // Logic Layer usage
            int[] probs = WinProbabilityCalculator.calculate(scores);
            binding.winProbabilityBar.setProgress(probs[0]);
            
            List<String> teams = match.getTeams();
            if (teams != null && teams.size() >= 2) {
                binding.team1ProbText.setText(teams.get(0) + " " + probs[0] + "%");
                binding.team2ProbText.setText(teams.get(1) + " " + probs[1] + "%");
            }

            binding.crrText.setText(String.format("%.2f", (latest.getOvers() > 0 ? latest.getRuns() / latest.getOvers() : 0.0)));
            binding.rrrText.setText(InsightsGenerator.getPressureInsight(scores));
            
            // Show POTM and Summary
            StringBuilder insight = new StringBuilder();
            if (match.getPlayerOfMatch() != null && !match.getPlayerOfMatch().isEmpty()) {
                insight.append("Player of the Match: ").append(match.getPlayerOfMatch()).append("\n\n");
            }
            insight.append("Status: ").append(match.getStatus());
            binding.insightText.setText(insight.toString());
        } else {
            binding.detailScore.setText("- / -");
            binding.detailStatus.setText(match.getStatus() != null ? match.getStatus() : "Upcoming");
            binding.insightText.setText("Match Summary: " + (match.getStatus() != null ? match.getStatus() : "Upcoming"));
        }

        // Match Summary
        binding.summaryVenue.setText(match.getVenue());
        if (match.getDate() != null) {
            binding.summaryDate.setText(match.getDate());
        } else {
            binding.summaryDate.setVisibility(View.GONE);
        }
    }

    private void bindScorecard(com.example.cricketapp.models.ScorecardData data) {
        if (data == null || data.getInnings() == null || data.getInnings().isEmpty()) {
            showScorecardUnavailable();
            return;
        }

        try {
            // Get the latest inning (or first as fallback)
            List<Inning> inningsList = data.getInnings();
            Inning current = inningsList.get(inningsList.size() - 1);
            
            boolean hasData = false;

            // Batting
            if (current != null && current.getBatting() != null && !current.getBatting().isEmpty()) {
                battingAdapter.setData(current.getBatting());
                binding.labelBatting.setVisibility(View.VISIBLE);
                binding.battingRecyclerView.setVisibility(View.VISIBLE);
                hasData = true;
            } else {
                binding.labelBatting.setVisibility(View.GONE);
                binding.battingRecyclerView.setVisibility(View.GONE);
            }

            // Bowling
            if (current != null && current.getBowling() != null && !current.getBowling().isEmpty()) {
                bowlingAdapter.setData(current.getBowling());
                binding.labelBowling.setVisibility(View.VISIBLE);
                binding.bowlingRecyclerView.setVisibility(View.VISIBLE);
                hasData = true;
            } else {
                binding.labelBowling.setVisibility(View.GONE);
                binding.bowlingRecyclerView.setVisibility(View.GONE);
            }

            if (hasData) {
                binding.scorecardContainer.setVisibility(View.VISIBLE);
                binding.scorecardErrorText.setVisibility(View.GONE);
            } else {
                showScorecardUnavailable();
            }

        } catch (Exception e) {
            Log.e("API_DEBUG", "Scorecard parse error: " + e.getMessage());
            showScorecardUnavailable();
        }
    }

    private void showScorecardUnavailable() {
        if (binding.labelBatting != null) binding.labelBatting.setVisibility(View.GONE);
        if (binding.battingRecyclerView != null) binding.battingRecyclerView.setVisibility(View.GONE);
        if (binding.labelBowling != null) binding.labelBowling.setVisibility(View.GONE);
        if (binding.bowlingRecyclerView != null) binding.bowlingRecyclerView.setVisibility(View.GONE);
        if (binding.scorecardContainer != null) binding.scorecardContainer.setVisibility(View.GONE);
        
        if (binding.scorecardErrorText != null) {
            binding.scorecardErrorText.setVisibility(View.VISIBLE);
            binding.scorecardErrorText.setText("Detailed scorecard not available yet");
        }
    }

    private void showLoading() {
        if (binding.loadingLayout != null) binding.loadingLayout.setVisibility(View.VISIBLE);
        if (binding.contentLayout != null) binding.contentLayout.setVisibility(View.GONE);
        if (binding.errorLayout != null) binding.errorLayout.setVisibility(View.GONE);
    }

    private void showContent() {
        if (binding.contentLayout != null) {
            binding.contentLayout.setVisibility(View.VISIBLE);
            binding.contentLayout.post(() -> {
                binding.contentLayout.requestLayout();
                binding.contentLayout.invalidate();
            });
        }
        if (binding.loadingLayout != null) binding.loadingLayout.setVisibility(View.GONE);
        if (binding.errorLayout != null) binding.errorLayout.setVisibility(View.GONE);
    }

    private void showError() {
        if (binding.errorLayout != null) binding.errorLayout.setVisibility(View.VISIBLE);
        if (binding.loadingLayout != null) binding.loadingLayout.setVisibility(View.GONE);
        if (binding.contentLayout != null) binding.contentLayout.setVisibility(View.GONE);
    }
}
