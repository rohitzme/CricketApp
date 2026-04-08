package com.example.cricketapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cricketapp.adapters.MatchPagerAdapter;
import com.example.cricketapp.api.RetrofitClient;
import com.example.cricketapp.databinding.ActivityMainBinding;
import com.example.cricketapp.models.MatchResponse;
import com.example.cricketapp.utils.Constants;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.example.cricketapp.ui.fragments.MatchListFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MatchPagerAdapter pagerAdapter;
    private MatchResponse cachedResponse;

    public MatchResponse getCachedResponse() {
        return cachedResponse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("API_DEBUG", "onCreate triggered");
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupTabs();
        setupRetryButton();
        
        // Ensure API call is the last thing in onCreate
        fetchMatches(false);
    }

    private void setupRetryButton() {
        if (binding.btnRetry != null) {
            binding.btnRetry.setOnClickListener(v -> fetchMatches(false));
        }
    }

    private void showLoading() {
        Log.d("API_DEBUG", "showLoading called");
        if (binding.loadingLayout != null) binding.loadingLayout.setVisibility(View.VISIBLE);
        if (binding.contentLayout != null) binding.contentLayout.setVisibility(View.GONE);
        if (binding.errorLayout != null) binding.errorLayout.setVisibility(View.GONE);
    }

    private void showError() {
        showErrorWithMessage("Unable to load data", "Check your connection");
    }

    private void showErrorWithMessage(String title, String subtitle) {
        if (binding.errorLayout != null) {
            binding.errorLayout.setVisibility(View.VISIBLE);
            binding.errorTitle.setText(title);
            binding.errorSubtitle.setText(subtitle);
        }
        if (binding.loadingLayout != null) binding.loadingLayout.setVisibility(View.GONE);
        if (binding.contentLayout != null) binding.contentLayout.setVisibility(View.GONE);
    }

    private void showEmptyState(String message) {
        showErrorWithMessage("No Matches", message);
    }

    private void showSubtleBanner(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    private void showContent() {
        Log.d("API_DEBUG", "showContent called");
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

    private void setupTabs() {
        pagerAdapter = new MatchPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("LIVE"); break;
                case 1: tab.setText("RECENT"); break;
                case 2: tab.setText("UPCOMING"); break;
            }
        }).attach();
        
        binding.viewPager.setOffscreenPageLimit(2);
    }

    public void fetchMatches(boolean isRefresh) {
        Log.d("API_DEBUG", "fetchMatches called. isRefresh: " + isRefresh);
        
        if (!isRefresh && cachedResponse != null) {
            Log.d("API_DEBUG", "Using cached response");
            updateFragments(cachedResponse);
            showContent();
            return;
        }

        // 3. Ensure showLoading() is called before API request
        if (!isRefresh) {
            showLoading();
        }

        if (binding.btnRetry != null) {
            binding.btnRetry.setEnabled(false);
            binding.btnRetry.setText("Retrying...");
        }

        // 2. Ensure logging is present: Before API call
        Log.d("API_DEBUG", "Fetching matches...");
        
        RetrofitClient.getClient().getMatches(Constants.API_KEY, 0).enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {
                Log.d("API_DEBUG", "URL: " + call.request().url().toString());
                Log.e("API_DEBUG", "FULL RESPONSE: " + new Gson().toJson(response.body()));
                
                if (response.body() != null && "success".equals(response.body().getStatus())) {
                    if (response.body().getData() != null && !response.body().getData().isEmpty()) {
                        cachedResponse = response.body();
                        updateFragments(cachedResponse);
                        showContent();
                    } else {
                        showEmptyState("No real matches found in API");
                    }
                } else {
                    String errorMsg = (response.body() != null) ? response.body().getStatus() : "Unknown Error";
                    showErrorWithMessage("API Error", "Service reported: " + errorMsg);
                }
                resetRetryButton();
            }

            @Override
            public void onFailure(Call<MatchResponse> call, Throwable t) {
                Log.e("API_DEBUG", "Network Error: " + t.getMessage());
                showErrorWithMessage("Connection Error", "Please check your network");
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

    private void updateFragments(MatchResponse response) {
        if (pagerAdapter == null || response == null || response.getData() == null) return;
        for (int i = 0; i < 3; i++) {
            MatchListFragment fragment = pagerAdapter.getFragment(i);
            if (fragment != null) {
                fragment.updateData(response.getData());
            }
        }
    }

    private void handleError(String message) {
        if (cachedResponse != null) {
            updateFragments(cachedResponse);
            showContent();
            showSubtleBanner("Offline mode: " + message);
        } else {
            showErrorWithMessage("Connection Error", message);
        }
    }
}
