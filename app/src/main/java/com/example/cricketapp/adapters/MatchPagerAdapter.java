package com.example.cricketapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.cricketapp.ui.fragments.MatchListFragment;
import java.util.ArrayList;
import java.util.List;

public class MatchPagerAdapter extends FragmentStateAdapter {

    private final List<MatchListFragment> fragments = new ArrayList<>();

    public MatchPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments.add(MatchListFragment.newInstance("LIVE"));
        fragments.add(MatchListFragment.newInstance("RECENT"));
        fragments.add(MatchListFragment.newInstance("UPCOMING"));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public MatchListFragment getFragment(int position) {
        return fragments.get(position);
    }
}
