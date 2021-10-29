package com.nextpass.nextiati.nextpass.ui.access.menu.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nextpass.nextiati.nextpass.databinding.ItemMenuAccessBinding

class MenuAccessAdapter : RecyclerView.Adapter<MenuAccessAdapter.ViewHolder>() {
    var items: List<MenuItemQR> = ArrayList()
    lateinit var context: Context

    fun setData(list: List<MenuItemQR>) {
        items = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ItemMenuAccessBinding.inflate(inflater)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ItemMenuAccessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItemQR) {
            with(binding){
                title.setText(item.title)
                subtitle.setText(item.subtitle)
                icon.setCardBackgroundColor(Color.parseColor(item.color))
                imageIcon.setImageResource(item.icon)
                itemView.setOnClickListener {
                    listener.onClick(item.title)
                }
            }
        }
    }

    private lateinit var listener: Listener
    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onClick(title: String)
    }

    class MenuItemQR(var title: String, var subtitle: String, var color: String, var icon: Int)
}