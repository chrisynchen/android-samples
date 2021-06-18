package com.chris.android_samples.data

import androidx.annotation.IntDef

data class Menu(@Type val type: Int, val title: String, val subtitle: String) {
    companion object {
        const val START_SERVICE = 0
        const val BIND_SERVICE = 1
        const val MEMORY_LEAK = 2
        const val COLOR_PALETTE = 3

        @IntDef(
            START_SERVICE,
            BIND_SERVICE,
            MEMORY_LEAK,
            COLOR_PALETTE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Type
    }
}