package com.college.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.app.R;
import com.college.app.club.Club;
import com.college.app.databinding.ClubItemBinding;
import com.college.app.fragment.ClubFragmentDetails;
import com.college.app.fragment.HolderActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.Item> {

    List<Club> clubList;
    private Context context;

    public ClubAdapter(List<Club> clubList, Context context) {
        this.clubList = clubList;
        this.context = context;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ClubItemBinding itemBinding = ClubItemBinding.inflate(layoutInflater, parent, false);
        return new ClubAdapter.Item(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.clubItemBinding.clubName.setText(clubList.get(position).getClubName());
        if (clubList.get(position).getPhotoUrl() != null) {
            String url = clubList.get(position).getPhotoUrl();
            Picasso.get()
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.material_design_3)
                    .into(holder.clubItemBinding.imageView1);
        }
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public class Item extends RecyclerView.ViewHolder implements View.OnClickListener {
        ClubItemBinding clubItemBinding;

        public Item(ClubItemBinding clubItemBinding) {
            super(clubItemBinding.getRoot());
            this.clubItemBinding = clubItemBinding;
            clubItemBinding.cardViewClubs.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String[] club = new String[]{clubList.get(getAdapterPosition()).getClubName().toString()};
            HolderActivity.startActivity(context, ClubFragmentDetails.class, null, club);
        }
    }
}
