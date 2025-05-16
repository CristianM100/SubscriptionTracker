package com.example.subscriptiontracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.subscriptiontracker.R
import com.example.subscriptiontracker.data.model.SubscriptionItem
import java.text.SimpleDateFormat
import java.util.Locale


class SubscriptionCategoryAdapter(private var items: List<SubscriptionItem>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<SubscriptionCategoryAdapter.SubscriptionViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return SubscriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener.onItemClick(position) }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<SubscriptionItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.itemName)
        private val itemDesc: TextView = itemView.findViewById(R.id.itemDesc)
        private val itemPayDate: TextView = itemView.findViewById(R.id.itemPayDate)
        private val itemAmount: TextView = itemView.findViewById(R.id.itemAmount)
        private val itemCycle: TextView = itemView.findViewById(R.id.itemCycle)
        private val itemPayMet: TextView = itemView.findViewById(R.id.itemPayMet)

        fun bind(item: SubscriptionItem) {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            itemName.text = item.name
            itemDesc.text = item.desc
            itemPayDate.text = formatter.format(item.pay)
            itemAmount.text = String.format(Locale.US, "%.2f", item.amount)
            itemCycle.text = item.cycle
            itemPayMet.text = item.payMet
        }
    }
}
