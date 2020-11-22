package com.chris.android_samples.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chris.android_samples.data.Menu
import com.chris.android_samples.R
import com.chris.android_samples.extension.bindView

class MenuViewHolder(itemView: View, listener: Listener) :
    RecyclerView.ViewHolder(itemView) {

    private var menu: Menu? = null

    init {
        itemView.setOnClickListener {
            listener.onItemClick(menu)
        }
    }

    private val titleTextView: TextView by bindView(R.id.titleTextView)
    private val subTitleTextView: TextView by bindView(R.id.subTitleTextView)

    fun bind(menu: Menu?) {
        this.menu = menu
        titleTextView.text = menu?.title
        subTitleTextView.text = menu?.subtitle
    }

    interface Listener {
        fun onItemClick(menu: Menu?)
    }
}