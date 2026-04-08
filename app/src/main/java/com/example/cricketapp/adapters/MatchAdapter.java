package com.example.cricketapp.adapters;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cricketapp.databinding.ItemMatchCardBinding;
import com.example.cricketapp.models.Match;
import com.example.cricketapp.models.Score;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<Match> matches;

    public MatchAdapter(List<Match> matches) {
        this.matches = matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMatchCardBinding binding = ItemMatchCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MatchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.binding.matchName.setText(match.getName());
        holder.binding.seriesName.setText(match.getMatchType() != null ? match.getMatchType() : "Series");

        // Format Score (Cricbuzz Style: Primary score and overs)
        if (match.getScore() != null && !match.getScore().isEmpty()) {
            Score s = match.getScore().get(match.getScore().size() - 1);
            String scoreStr = s.getRuns() + "/" + s.getWickets() + " (" + s.getOvers() + ")";
            holder.binding.scoreText.setText(scoreStr);
        } else {
            holder.binding.scoreText.setText("Match not started");
        }

        // Format Status and Result
        if (match.isLive()) {
            holder.binding.matchStatus.setText("LIVE ●");
            holder.binding.matchStatus.setTextColor(holder.itemView.getContext().getResources().getColor(com.example.cricketapp.R.color.live_red));
            if (holder.binding.liveTag != null) holder.binding.liveTag.setVisibility(android.view.View.VISIBLE);
        } else if (match.isCompleted()) {
            holder.binding.matchStatus.setText("RESULT: " + (match.getStatus() != null ? match.getStatus() : "Finished"));
            holder.binding.matchStatus.setTextColor(holder.itemView.getContext().getResources().getColor(com.example.cricketapp.R.color.status_result));
            if (holder.binding.liveTag != null) holder.binding.liveTag.setVisibility(android.view.View.GONE);
        } else {
            holder.binding.matchStatus.setText(match.getStatus() != null ? match.getStatus() : "Upcoming");
            holder.binding.matchStatus.setTextColor(holder.itemView.getContext().getResources().getColor(com.example.cricketapp.R.color.grey_light));
            if (holder.binding.liveTag != null) holder.binding.liveTag.setVisibility(android.view.View.GONE);
        }

        // Interaction (Ready to open MatchDetailActivity)
        holder.binding.getRoot().setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(v.getContext(), com.example.cricketapp.ui.MatchDetailActivity.class);
            intent.putExtra("match_id", match.getId());
            v.getContext().startActivity(intent);
        });

        // Subtle animation (Polish Phase)
        android.animation.ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f).setDuration(250).start();
    }

    @Override
    public int getItemCount() {
        return matches != null ? matches.size() : 0;
    }

    static class MatchViewHolder extends RecyclerView.ViewHolder {
        ItemMatchCardBinding binding;
        MatchViewHolder(ItemMatchCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
