package com.chris.android_samples.color_palatte

import android.graphics.Bitmap
import android.util.Log
import com.chris.android_samples.viewmodel.BaseViewModel


class ColorPaletteViewModel : BaseViewModel() {

    companion object {
        const val defaultValue = 20
    }

    var bitmap: Bitmap? = null


    fun generate() {
        bitmap?.run {
            val map = mutableMapOf<Int, Int>()
//            val data = Array(width) {Array(height) {0} }

            for (x in 0 until width) {
                for (y in 0 until height) {
                    // Get the color of a pixel within myBitmap.
                    val key = getPixel(x, y)
                    map[key] = (map[key] ?: 0) + 1
//                    val r: Int = Color.red(pixelColor)
//                    val g: Int = Color.green(pixelColor)
//                    val b: Int = Color.blue(pixelColor)
//                    val t: Int = Color.alpha(pixelColor)
                }
            }

            val allList = map.toList().sortedByDescending {
                it.second
            }

            val list = allList.take(
                if (allList.size > defaultValue) {
                    defaultValue
                } else {
                    allList.size
                }
            ).toList()

            print(list.size)
            for (e in list) {
                Log.e("Chris", "#%06X".format(0xFFFFFF and e.first) + ", total:${e.second}")
            }

        } ?: return
    }
}