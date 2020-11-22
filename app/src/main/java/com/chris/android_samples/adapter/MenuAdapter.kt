package com.chris.android_samples.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chris.android_samples.data.Menu
import com.chris.android_samples.viewholder.MenuViewHolder
import com.chris.android_samples.R

class MenuAdapter(
    private val listener: MenuViewHolder.Listener,
    private val menus: List<Menu>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MenuViewHolder).bind((getItem(position)))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_menu, parent, false)
        return MenuViewHolder(
            itemView,
            listener
        )
    }

    private fun getItem(position: Int): Menu? {
        return if (position in 0 until itemCount) {
            menus[position]
        } else null
    }
}