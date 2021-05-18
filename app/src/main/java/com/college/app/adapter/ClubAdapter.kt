package com.college.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.app.R
import com.college.app.club.Club
import com.college.app.databinding.ClubItemBinding
import com.college.app.fragment.ClubFragmentDetails
import com.college.app.fragment.HolderActivity
import com.squareup.picasso.Picasso

class ClubAdapter(var clubList: List<Club>, private val context: Context) : RecyclerView.Adapter<ClubAdapter.Item>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ClubItemBinding.inflate(layoutInflater, parent, false)
        return Item(itemBinding)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.clubItemBinding.clubName.text = clubList[position].clubName
        if (clubList[position].photoUrl != null) {
            val url = clubList[position].photoUrl
            Picasso.get()
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.material_design_3)
                    .into(holder.clubItemBinding.imageView1)
        }
    }

    override fun getItemCount(): Int {
        return clubList.size
    }

    inner class Item(var clubItemBinding: ClubItemBinding) : RecyclerView.ViewHolder(clubItemBinding.root), View.OnClickListener {
        override fun onClick(v: View) {
            val club = arrayOf(clubList[adapterPosition].clubName.toString())
            HolderActivity.startActivity(context, ClubFragmentDetails::class.java, null, club)
        }

        init {
            clubItemBinding.cardViewClubs.setOnClickListener(this)
        }
    }
}