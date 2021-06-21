package com.chris.android_samples.color_palatte.delta_e

/// Used to configure weight factors.
data class Weights(val lightness: Double = 1.0, val chroma: Double = 1.0, val hue: Double = 1.0) {
    val l: Double
        get() = this.lightness

    val a: Double
        get() = this.chroma

    val b: Double
        get() = this.hue
}