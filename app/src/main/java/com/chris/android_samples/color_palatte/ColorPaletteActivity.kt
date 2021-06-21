package com.chris.android_samples.color_palatte

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chris.android_samples.databinding.ActivityColorPaletteBinding
import com.chris.android_samples.extension.getViewModel


class ColorPaletteActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ColorPaletteActivity::class.java)
        }
    }

    private lateinit var binding: ActivityColorPaletteBinding

    private val viewModel by lazy {
        getViewModel {
            ColorPaletteViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColorPaletteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(applicationContext)
            .asBitmap()
            .load("https://d1sag4ddilekf6.cloudfront.net/Merchants/4-CZB3UBDFSEXHRT/pictures/0ff46a211ec3488daa1be5c65f9ab5d5_1584524848506026585.jpeg")
            .into(object : CustomTarget<Bitmap>(SIZE_ORIGINAL, SIZE_ORIGINAL) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.imageView.setImageBitmap(resource)
                    viewModel.bitmap = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

        binding.startButton.setOnClickListener {
            viewModel.generate()
        }
    }
}