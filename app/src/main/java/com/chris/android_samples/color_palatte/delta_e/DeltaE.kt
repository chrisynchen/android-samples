package com.chris.android_samples.color_palatte.delta_e

import kotlin.math.*

enum class DeltaEAlgorithm {
    /// The 1976 formula is the first formula that related a measured color difference to a known set of CIELAB
    /// coordinates. This formula has been succeeded by the 1994 and 2000 formulas because the CIELAB space turned out
    /// to be not as perceptually uniform as intended, especially in the saturated regions. This means that this formula
    /// rates these colors too highly as opposed to other colors.
    ///
    /// Source: http://en.wikipedia.org/wiki/Color_difference#CIE76
    cie76,  /// The 1976 definition was extended to address perceptual non-uniformities, while retaining the CIELAB color space,

    /// by the introduction of application-specific weights derived from an automotive paint test's tolerance data.
    ///
    /// Source: https://en.wikipedia.org/wiki/Color_difference#CIE94
    cie94,  /// Since the 1994 definition did not adequately resolve the perceptual uniformity issue, the CIE refined their

    /// definition, adding five corrections:
    /// - A hue rotation term, to deal with the problematic blue region (hue angles in the neighborhood of 275°)
    /// - Compensation for neutral colors (the primed values in the L*C*h differences)
    /// - Compensation for lightness
    /// - Compensation for chroma
    /// - Compensation for hue
    ///
    /// Source: https://en.wikipedia.org/wiki/Color_difference#CIEDE2000
    ciede2000
}

/// Calculates the color difference between `lab1` and `lab2`.
///
/// The `weights` parameter is only taken into account if `algorithm` isn't set to `DeltaEAlgorithm.cie76`.
fun deltaE(
    lab1: LabColor,
    lab2: LabColor,
    algorithm: DeltaEAlgorithm = DeltaEAlgorithm.ciede2000,
    weights: Weights = Weights()
): Double {
    return when (algorithm) {
        DeltaEAlgorithm.cie76 ->
            deltaE76(lab1, lab2)
        DeltaEAlgorithm.cie94 ->
            deltaE94(lab1, lab2, weights)
        else ->
            //DeltaEAlgorithm.ciede2000
            deltaE00(lab1, lab2, weights)
    }
}

/// The 1976 formula is the first formula that related a measured color difference to a known set of CIELAB
/// coordinates. This formula has been succeeded by the 1994 and 2000 formulas because the CIELAB space turned out
/// to be not as perceptually uniform as intended, especially in the saturated regions. This means that this formula
/// rates these colors too highly as opposed to other colors.
///
/// Source: http://en.wikipedia.org/wiki/Color_difference#CIE76
fun deltaE76(lab1: LabColor, lab2: LabColor): Double {
    return sqrt(
        (lab2.l - lab1.l).pow(2) +
                (lab2.a - lab1.a).pow(2) +
                (lab2.b - lab1.b).pow(2)
    )
}

/// The 1976 definition was extended to address perceptual non-uniformities, while retaining the CIELAB color space,
/// by the introduction of application-specific weights derived from an automotive paint test's tolerance data.
///
/// Source: https://en.wikipedia.org/wiki/Color_difference#CIE94
fun deltaE94(lab1: LabColor, lab2: LabColor, weights: Weights = Weights()): Double {

    val k1 = if (weights.l == 1.0) {
        0.045
    } else {
        0.048
    }

    val k2: Double = if (weights.l == 1.0) {
        0.015
    } else {
        0.014
    }

    // Cab
    val c1 = sqrt(lab1.a.pow(2) + lab1.b.pow(2))
    val c2 = sqrt(lab2.a.pow(2) + lab2.b.pow(2))
    val cab = c1 - c2
    // L
    val l = (lab1.l - lab2.l) / weights.lightness
    // a
    val sc = 1 + (k1 * c1)
    val a = cab / (weights.a * sc)
    // b - Top
    val hab =
        sqrt((lab1.a - lab2.a).pow(2) + (lab1.b - lab2.b).pow(2) - cab.pow(2))
    // b - Bottom
    val sh = 1 + (k2 * c1)
    val b = hab / sh

    return sqrt(l.pow(2) + a.pow(2) + b.pow(2))
}

/// Since the 1994 definition did not adequately resolve the perceptual uniformity issue, the CIE refined their
/// definition, adding five corrections:
/// - A hue rotation term, to deal with the problematic blue region (hue angles in the neighborhood of 275°)
/// - Compensation for neutral colors (the primed values in the L*C*h differences)
/// - Compensation for lightness
/// - Compensation for chroma
/// - Compensation for hue
///
/// Source: https://en.wikipedia.org/wiki/Color_difference#CIEDE2000
fun deltaE00(lab1: LabColor, lab2: LabColor, weights: Weights = Weights()): Double {
    // Delta L Prime
    val deltaLPrime = lab2.l - lab1.l
    // L Bar
    val lBar = (lab1.l + lab2.l) / 2
    // C1 & C2
    val c1 = sqrt(lab1.a.pow(2) + lab1.b.pow(2))
    val c2 = sqrt(lab2.a.pow(2) + lab2.b.pow(2))
    // C Bar
    val cBar = (c1 + c2) / 2
    // A Prime 1
    val aPrime1 = lab1.a +
            (lab1.a / 2) * (1 - sqrt(cBar.pow(7) / (cBar.pow(7) + 25.0.pow(7))))
    // A Prime 2
    val aPrime2 = lab2.a +
            (lab2.a / 2) * (1 - sqrt(cBar.pow(7) / (cBar.pow(7) + 25.0.pow(7))))
    // C Prime 1
    val cPrime1 = sqrt(aPrime1.pow(2) + lab1.b.pow(2))
    // C Prime 2
    val cPrime2 = sqrt(aPrime2.pow(2) + lab2.b.pow(2))
    // C Bar Prime
    val cBarPrime = (cPrime1 + cPrime2) / 2
    // Delta C Prime
    val deltaCPrime = cPrime2 - cPrime1
    // S sub L
    val sSubL =
        1 + ((0.015 * (lBar - 50).pow(2)) / sqrt(20 + (lBar - 50).pow(2)))
    // S sub C
    val sSubC = 1 + 0.045 * cBarPrime
    // h Primes
    val hPrime1 = getPrimeFn(lab1.b, aPrime1)
    val hPrime2 = getPrimeFn(lab2.b, aPrime2)
    // Delta h Prime
    // - When either C′1 or C′2 is zero, then Δh′ is irrelevant and may be set to zero.
    val deltahPrime: Double = if (c1 == 0.0 || c2 == 0.0) {
        0.0
    } else if (abs(hPrime1 - hPrime2) <= 180.0) {
        hPrime2 - hPrime1
    } else if (hPrime2 <= hPrime1) {
        hPrime2 - hPrime1 + 360.0
    } else {
        hPrime2 - hPrime1 - 360.0
    }
    // Delta H Prime
    val deltaHPrime =
        2 * sqrt(cPrime1 * cPrime2) * sin(degreesToRadians(deltahPrime) / 2)
    // H Bar Prime
    val hBarPrime: Double = if (abs(hPrime1 - hPrime2) > 180) {
        (hPrime1 + hPrime2 + 360) / 2
    } else {
        (hPrime1 + hPrime2) / 2
    }
    // T
    val t = 1 -
            0.17 * cos(degreesToRadians(hBarPrime - 30)) +
            0.24 * cos(degreesToRadians(2 * hBarPrime)) +
            0.32 * cos(degreesToRadians(3 * hBarPrime + 6)) -
            0.20 * cos(degreesToRadians(4 * hBarPrime - 63))
    // S sub H
    val sSubH = 1 + 0.015 * cBarPrime * t
    // R sub T
    val rSubT = -2 *
            sqrt(cBarPrime.pow(7) / (cBarPrime.pow(7) + 25.0.pow(7))) *
            sin(degreesToRadians(60 * exp(-((hBarPrime - 275) / 25).pow(2))))
    // Lab
    val lightness = deltaLPrime / (weights.l * sSubL)
    val chroma = deltaCPrime / (weights.a * sSubC)
    val hue = deltaHPrime / (weights.b * sSubH)

    return sqrt(
        lightness.pow(2) + chroma.pow(2) + hue.pow(2) + (rSubT * chroma * hue)
    )
}

/// A helper function to calculate the h Prime 1 and h Prime 2 values.
private fun getPrimeFn(x: Double, y: Double): Double {
    if (x == 0.0 && y == 0.0) {
        return 0.0
    }
    val hueAngle: Double = radiansToDegrees(atan2(x, y))
    return if (hueAngle >= 0) hueAngle else hueAngle + 360
}

private fun radiansToDegrees(radians: Double): Double = radians * (180 / Math.PI)

private fun degreesToRadians(degrees: Double): Double = degrees * (Math.PI / 180)