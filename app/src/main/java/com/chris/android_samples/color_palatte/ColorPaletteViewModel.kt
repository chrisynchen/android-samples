package com.chris.android_samples.color_palatte

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.chris.android_samples.color_palatte.delta_e.LabColor
import com.chris.android_samples.color_palatte.delta_e.deltaE
import com.chris.android_samples.viewmodel.BaseViewModel
import io.reactivex.Single


class ColorPaletteViewModel : BaseViewModel() {

    companion object {
        const val defaultValue = 20
    }

    var bitmap: Bitmap? = null


    fun generate() {
        bitmap?.let { bitmap ->
            Single.just(bitmap)
                .map {
                    val map = mutableMapOf<Int, Int>()
//                    val data = Array(width) {Array(height) {0} }

                    Log.e("chris", "start")
                    for (x in 0 until bitmap.width) {
                        for (y in 0 until bitmap.height) {
                            // Get the color of a pixel within bitmap.
                            val key = bitmap.getPixel(x, y)
                            map[key] = (map[key] ?: 0) + 1
                        }
                    }
                    Log.e("chris", "end")
                    map
                }.map {
                    it.toList()
                        .sortedByDescending { pair ->
                            pair.second
                        }
                }.map {
                    val filterList = mutableListOf(Color.BLACK, Color.WHITE)
                    val list = mutableListOf<Pair<Int, Int>>()
                    for (e in it) {
                        val result = shouldRemove(e.first, filterList)
                        filterList.add(e.first)
                        if (!result) {
                            list.add(e)
                        }
                    }
                    list
                }.subscribe({
                    for (e in it) {
                        Log.e("Chris", Integer.toHexString(e.first) + ", total:${e.second}")
                    }
                }, {
                    Log.e("Chris", it.toString())
                })

        } ?: return
    }

    private fun shouldRemove(targetColor: Int, filterList: List<Int>): Boolean {
        val targetLabColor: LabColor = LabColor.fromRGB(
            Color.red(targetColor),
            Color.green(targetColor),
            Color.blue(targetColor)
        )

        filterList.forEach {
            if (it == targetColor) return true
            val comparedLabColor: LabColor = LabColor.fromRGB(
                Color.red(it),
                Color.green(it),
                Color.blue(it)
            )
            val deltaE = deltaE(targetLabColor, comparedLabColor)
            if (deltaE <= 20) return true
        }

        return false
    }
}