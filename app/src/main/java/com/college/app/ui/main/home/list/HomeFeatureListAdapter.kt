package com.college.app.ui.main.home.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ListItemHomeFeatureBinding
import com.college.app.utils.extensions.executeAfter

class HomeFeatureListAdapter(private val clickListener: HomeFeatureListClickListener) :
    ListAdapter<HomeFeatureListViewState, HomeFeatureListAdapter.HomeFeatureListViewHolder>(
        HomeFeatureListDiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeatureListViewHolder {
        return HomeFeatureListViewHolder(
            ListItemHomeFeatureBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: HomeFeatureListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemId(position: Int) = currentList[position].id.toLong()

    inner class HomeFeatureListViewHolder(
        private val binding: ListItemHomeFeatureBinding,
        val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeFeatureListViewState) {
            binding.executeAfter {

                actionHandler = clickListener
                viewState = item

                listItemHomeFeatureBg.backgroundTintList =
                    AppCompatResources.getColorStateList(context, item.itemBackgroundColorRes)

                listItemHomeFeatureIcon.setImageResource(item.iconRes)

                listItemHomeFeatureTitle.setText(item.titleRes)
                listItemHomeFeatureTitle.setTextColor(
                    ContextCompat.getColor(
                        context,
                        item.textColorRes
                    )
                )
            }
        }
    }

    interface HomeFeatureListClickListener {
        fun onHomeFeatureListItemClick(itemId: Int)
    }
}


object HomeFeatureListDiffCallback : DiffUtil.ItemCallback<HomeFeatureListViewState>() {
    override fun areItemsTheSame(
        oldItem: HomeFeatureListViewState,
        newItem: HomeFeatureListViewState,
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: HomeFeatureListViewState,
        newItem: HomeFeatureListViewState,
    ) = oldItem == newItem

}

data class HomeFeatureListViewState(
    val id: Int,
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @ColorRes val itemBackgroundColorRes: Int,
    @ColorRes val textColorRes: Int,
)