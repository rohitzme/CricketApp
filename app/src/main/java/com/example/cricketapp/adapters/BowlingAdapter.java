package com.example.cricketapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cricketapp.databinding.ItemBowlerBinding;
import com.example.cricketapp.models.Bowler;
import java.util.List;

public class BowlingAdapter extends RecyclerView.Adapter<BowlingAdapter.BowlingViewHolder> {

    private List<Bowler> bowlers;

    public BowlingAdapter(List<Bowler> bowlers) {
        this.bowlers = bowlers != null ? bowlers : new java.util.ArrayList<>();
    }

    public void setData(List<Bowler> newData) {
        this.bowlers = newData != null ? newData : new java.util.ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BowlingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBowlerBinding binding = ItemBowlerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BowlingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BowlingViewHolder holder, int position) {
        Bowler b = bowlers.get(position);
        holder.binding.bowlerName.setText(b.getName());
        holder.binding.bowlerOvers.setText(b.getOvers());
        holder.binding.bowlerRuns.setText(b.getRuns());
        holder.binding.bowlerWickets.setText(b.getWickets());
        holder.binding.bowlerEco.setText(b.getEconomy());

        // Subtle animation (Polish Phase)
        android.animation.ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f).setDuration(250).start();
    }

    @Override
    public int getItemCount() {
        return bowlers != null ? bowlers.size() : 0;
    }

    static class BowlingViewHolder extends RecyclerView.ViewHolder {
        ItemBowlerBinding binding;
        BowlingViewHolder(ItemBowlerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
