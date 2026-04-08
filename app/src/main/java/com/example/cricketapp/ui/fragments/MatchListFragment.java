package com.example.cricketapp.ui.fragments;

import android.util.Log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cricketapp.adapters.MatchAdapter;
import com.example.cricketapp.databinding.FragmentMatchListBinding;
import com.example.cricketapp.models.Match;
import com.example.cricketapp.ui.MainActivity;
import java.util.ArrayList;
import java.util.List;

public class MatchListFragment extends Fragment {

    private FragmentMatchListBinding binding;
    private List<Match> allMatches = new ArrayList<>();
    private String type; // LIVE, RECENT, UPCOMING
    private MatchAdapter adapter;

    public static MatchListFragment newInstance(String type) {
        MatchListFragment fragment = new MatchListFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMatchListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new MatchAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);
        
        binding.swipeRefresh.setColorSchemeResources(android.R.color.white);
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.black);
        
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).fetchMatches(true);
            } else {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            com.example.cricketapp.models.MatchResponse response = ((MainActivity) getActivity()).getCachedResponse();
            if (response != null && response.getData() != null) {
                android.util.Log.d("API_DEBUG", "Fragment " + type + " pulling data from cache");
                updateData(response.getData());
            }
        }
    }

    public void setData(List<Match> matches) {
        this.allMatches = matches;
        if (binding != null && binding.swipeRefresh.isRefreshing()) {
            binding.swipeRefresh.setRefreshing(false);
        }
        filterAndDisplay();
    }
    
    public void updateData(List<Match> matches) {
        setData(matches);
    }

    private void filterAndDisplay() {
        if (binding == null) return;
        
        List<Match> filtered = new ArrayList<>();
        for (Match m : allMatches) {
            if ("LIVE".equals(type) && m.isLive()) filtered.add(m);
            else if ("RECENT".equals(type) && m.isCompleted()) filtered.add(m);
            else if ("UPCOMING".equals(type) && m.isUpcoming()) filtered.add(m);
        }

        binding.progressBar.setVisibility(View.GONE);
        if (filtered.isEmpty()) {
            binding.emptyStateText.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyStateText.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            adapter.setMatches(filtered);
        }
    }
}
