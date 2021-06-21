package com.chris.android_samples.color_palatte.delta_e

import kotlin.math.pow

/// A color represented in the CIELAB color space which expresses a color as three values:
/// its lightness (L\*) from black (0) to white (100), its chroma (a\*) from green (-180) to
/// red (+180), and its hue (b\*) from blue (-180) to yellow (+180).
///
/// Learn more: https://en.wikipedia.org/wiki/CIELAB_color_space


enum class RGBStructure {
    argb, // Alpha Red Green Blue
    rgb, // Red Green Blue
    rgba // Red Green Blue Alpha
}


/// Constructs a `LabColor`.
///
/// - `lightness` (L) must be between 0 (inclusive) and 100 (inclusive).
/// - `chroma` (a) must be between -128 (inclusive) and 128 (inclusive).
/// - `hue` (b) must be between -128 (inclusive) and 128 (inclusive).
class LabColor(private val lightness: Double, private val chroma: Double, private val hue: Double) {

    companion object {

        /// Constructs a `LabColor` from a RGB color value. The `structure` parameter is used to specify how `value` is structured, it's set to `RGBStructure.argb` by default (which is what Flutter uses for its color values).
        ///
        /// Reference: https://gist.github.com/manojpandey/f5ece715132c572c80421febebaf66ae
        fun fromRGBValue(value: Int, structure: RGBStructure = RGBStructure.argb): LabColor {
            when (structure) {
                RGBStructure.rgb -> {
                    return fromRGB(
                        (value and 0xFF0000) shr 16,
                        (value and 0x00FF00) shr 8,
                        value and 0x0000FF
                    );
                }

                RGBStructure.rgba -> {
                    //TODO(chris): need confirm
                    return fromRGB(
                        (value and 0xFF000000.toInt()) shr 24,
                        (value and 0x00FF0000) shr 16,
                        (value and 0x0000FF00) shr 8
                    )
                }
                else -> {
                    //RGBStructure.argb
                    return fromRGB(
                        (value and 0x00FF0000) shr 16,
                        (value and 0x0000FF00) shr 8,
                        value and 0x000000FF
                    )
                }
            }
        }

        // Constructs a `LabColor` from a RGB color.
        //
        // Reference: https://gist.github.com/manojpandey/f5ece715132c572c80421febebaf66ae
        fun fromRGB(red: Int, green: Int, blue: Int): LabColor {
            val rgb = arrayOf(red, green, blue).map {
                var value = it.toDouble() / 255
                if (value > 0.04045) {
                    value = ((value + 0.055) / 1.055).pow(2.4)
                } else {
                    value /= 12.92;
                }
                value * 100
            }.toList()

            val xyz = arrayOf(
                (rgb[0] * 0.4124 + rgb[1] * 0.3576 + rgb[2] * 0.1805) / 95.047,
                (rgb[0] * 0.2126 + rgb[1] * 0.7152 + rgb[2] * 0.0722) / 100,
                (rgb[0] * 0.0193 + rgb[1] * 0.1192 + rgb[2] * 0.9505) / 108.883,
            ).map {
                if (it > 0.008856) {
                    it.pow((1.0 / 3))
                } else {
                    (7.787 * it) + (16 / 116)
                }
            }.toList()

            return LabColor(
                (116 * xyz[1]) - 16,
                500 * (xyz[0] - xyz[1]),
                200 * (xyz[1] - xyz[2]),
            );
        }
    }


    val l: Double
        get() = this.lightness

    val a: Double
        get() = this.chroma

    val b: Double
        get() = this.hue

    override fun toString(): String = "LabColor(L: $l, a: $a, b: $b)"
}