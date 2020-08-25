package com.college.app.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.college.app.R;
import com.college.app.club.Club;
import com.college.app.databinding.ClubFragmentDetailsBinding;
import com.college.app.utils.AppUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class ClubFragmentDetails extends Fragment {
    public static String FRAGMENT_DATA = "transaction_data";
    ClubFragmentDetailsBinding clubFragmentDetailsBinding;
    Club fetchedClub;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        clubFragmentDetailsBinding = ClubFragmentDetailsBinding.inflate(inflater);
        fetchClubDetails();
        return clubFragmentDetailsBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void fetchClubDetails() {
        String[] clubName = getArguments().getStringArray(FRAGMENT_DATA);
        if (clubName != null) {
            String name = clubName[0];
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Clubs");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Club club = data.getValue(Club.class);
                        if (club != null) {
                            if (club.getClubName().equals(name)) {
                                club.setClubName(club.getClubName());
                                club.setClubField(club.getClubField());
                                club.setHead(club.getHead());
                                club.setPhotoUrlLandscape(club.getPhotoUrlLandscape());
                                club.setPhotoUrl(club.getPhotoUrl());
                                club.setAbout(club.getAbout());
                                club.setUpComingEvents(club.getUpComingEvents());
                                fetchedClub = club;
                                break;
                            }

                        }
                    }
                    setViews();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void setViews() {
        clubFragmentDetailsBinding.collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        clubFragmentDetailsBinding.collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Log.d(TAG, "photoLandscape" + fetchedClub.getPhotoUrlLandscape());
        Picasso.get()
                .load(fetchedClub.getPhotoUrlLandscape())
                .fit()

                .into(clubFragmentDetailsBinding.imageScrollingTop);

        if (AppUtils.getScreenWidthDp(getContext()) >= 600) {
            CollapsingToolbarLayout collapsing_toolbar_layout = clubFragmentDetailsBinding.collapsingToolbarLayout;
            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));
        }

        HolderActivity a = (HolderActivity) getActivity();
        a.setSupportActionBar(clubFragmentDetailsBinding.toolbar);
        a.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clubFragmentDetailsBinding.toolbar.setTitle(fetchedClub.getClubName());
        clubFragmentDetailsBinding.scrollLayout.clubName.setText(fetchedClub.getClubName());
        clubFragmentDetailsBinding.scrollLayout.clubHeads.setText(fetchedClub.getHead());
//        clubFragmentDetailsBinding.scrollLayout.clubAbout.setText(fetchedClub.getAbout());
        clubFragmentDetailsBinding.scrollLayout.applicationStatus.setText(fetchedClub.getApplicationStatus());
        String applicationLink = fetchedClub.getApplicationLink();
        clubFragmentDetailsBinding.scrollLayout.clubApplyButton.setOnClickListener(v -> {
            if (applicationLink != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(applicationLink));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Sorry, Unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        clubFragmentDetailsBinding.scrollLayout.clubField.setText(fetchedClub.getClubField());
        clubFragmentDetailsBinding.scrollLayout.clubAbout.setText(fetchedClub.getAbout());
        clubFragmentDetailsBinding.scrollLayout.upComingEvents.setText(fetchedClub.getUpComingEvents());
        clubFragmentDetailsBinding.scrollLayout.clubPerks.setText(fetchedClub.getPerks());
    }
}
