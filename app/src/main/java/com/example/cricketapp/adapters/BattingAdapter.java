package com.example.cricketapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cricketapp.databinding.ItemBatsmanBinding;
import com.example.cricketapp.models.Batsman;
import java.util.List;

public class BattingAdapter extends RecyclerView.Adapter<BattingAdapter.BattingViewHolder> {

    private List<Batsman> batsmen;

    public BattingAdapter(List<Batsman> batsmen) {
        this.batsmen = batsmen != null ? batsmen : new java.util.ArrayList<>();
    }

    public void setData(List<Batsman> newData) {
        this.batsmen = newData != null ? newData : new java.util.ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBatsmanBinding binding = ItemBatsmanBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BattingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BattingViewHolder holder, int position) {
        Batsman b = batsmen.get(position);
        holder.binding.batsmanName.setText(b.getName());
        holder.binding.batsmanRuns.setText(b.getRuns());
        holder.binding.batsmanBalls.setText(b.getBalls());
        holder.binding.batsmanSR.setText(b.getStrikeRate());

        // Subtle animation (Polish Phase)
        android.animation.ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f).setDuration(250).start();
    }

    @Override
    public int getItemCount() {
        return batsmen != null ? batsmen.size() : 0;
    }

    static class BattingViewHolder extends RecyclerView.ViewHolder {
        ItemBatsmanBinding binding;
        BattingViewHolder(ItemBatsmanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
