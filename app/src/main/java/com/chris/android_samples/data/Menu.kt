package com.chris.android_samples.data

import androidx.annotation.IntDef

data class Menu(@Type val type: Int, val title: String, val subtitle: String) {
    companion object {
        const val START_SERVICE = 0
        const val BIND_SERVICE = 1
        const val MEMORY_LEAK = 2

        @IntDef(
            START_SERVICE,
            BIND_SERVICE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Type
    }
}