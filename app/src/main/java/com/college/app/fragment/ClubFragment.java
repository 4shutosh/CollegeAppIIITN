package com.college.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.college.app.adapter.ClubAdapter;
import com.college.app.club.Club;
import com.college.app.databinding.ClubFragmentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class ClubFragment extends Fragment {

    private ClubFragmentBinding clubFragmentBinding;
    private List<Club> clubList;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        clubFragmentBinding = ClubFragmentBinding.inflate(inflater);
        ///
        HolderActivity a = (HolderActivity) getActivity();
        if (a != null) {
            a.setSupportActionBar(clubFragmentBinding.toolbar);
            a.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            a.getSupportActionBar().setTitle("Clubs");
        }
//        clubFragmentBinding.toolbar.setTitle("Clubs");
        clubFragmentBinding.shimmerClubs.startShimmerAnimation();
        getClubs();
        swipeLayout();
        return clubFragmentBinding.getRoot();
    }

    public void getClubs() {

        clubList = new ArrayList<>();
        // get Clubs from internet
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Clubs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Club club = data.getValue(Club.class);
                    if (club != null) {
                        club.setClubName(club.getClubName());
                        club.setHead(club.getHead());
                        clubList.add(club);
                    }
                }
                Log.d(TAG, "clubs found " + clubList.size());
                recyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recyclerView() {
        ClubAdapter clubAdapter = new ClubAdapter(clubList, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        clubFragmentBinding.clubsRecyclerView.setLayoutManager(gridLayoutManager);
        clubFragmentBinding.clubsRecyclerView.setAdapter(clubAdapter);

        clubFragmentBinding.shimmerClubs.stopShimmerAnimation();
        clubFragmentBinding.shimmerClubs.setVisibility(View.GONE);

        clubFragmentBinding.swipeLayoutClubs.setRefreshing(false);

        clubFragmentBinding.swipeLayoutClubs.setVisibility(View.VISIBLE);
        clubFragmentBinding.clubsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void swipeLayout() {
        clubFragmentBinding.swipeLayoutClubs.setOnRefreshListener(() -> {
            clubFragmentBinding.clubsRecyclerView.setVisibility(View.GONE);
            clubFragmentBinding.shimmerClubs.setVisibility(View.VISIBLE);
            clubFragmentBinding.shimmerClubs.startShimmerAnimation();
            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // you cannot touch the UI from another thread. This thread now calls a function on the main thread
                            getClubs();
                        }
                    }, 1000);
        });
    }
}
