package com.chris.android_samples

import com.chris.android_samples.color_palatte.delta_e.LabColor
import com.chris.android_samples.color_palatte.delta_e.deltaE
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.round


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val a = LabColor.fromRGB(136, 163, 187)
        val b = LabColor.fromRGB(255, 255, 255)
        val c = LabColor.fromRGB(42, 44, 51)
        val deltaOne = deltaE(a, b)
        val deltaTwo = deltaE(a, c)
        assertTrue(round(deltaOne) == round(25.942241866327098))
        assertTrue(round(deltaTwo) == round(44.11770735695545))
    }

    @Test
    fun test() {
    }
}