package com.college.app.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.app.databinding.ServiceItemBinding

class ServiceAdapter(var serviceList: MutableList<Service>, var context: Context) :
    RecyclerView.Adapter<ServiceAdapter.Item>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val layoutInflater = LayoutInflater.from(parent.context)
        val serviceItemBinding = ServiceItemBinding.inflate(layoutInflater, parent, false)
        return Item(serviceItemBinding)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        holder.serviceItemBinding.serviceName.text = serviceList[position].serviceName
        holder.serviceItemBinding.serviceDescShort.text = serviceList[position].serviceShortDesc
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    inner class Item(var serviceItemBinding: ServiceItemBinding) :
        RecyclerView.ViewHolder(serviceItemBinding.root), View.OnClickListener {
        override fun onClick(v: View) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(serviceList[adapterPosition].link))
            context.startActivity(intent)
        }

        init {
            serviceItemBinding.cardViewServiceItem.setOnClickListener(this)
        }
    }
}