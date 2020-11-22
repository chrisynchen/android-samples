package com.chris.android_samples.extension

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

fun <T : View> RecyclerView.ViewHolder.bindView(@IdRes resId: Int): Lazy<T> {
    return lazy {
        itemView.findViewById<T>(resId)
    }
}