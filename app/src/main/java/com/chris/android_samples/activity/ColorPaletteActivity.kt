package com.chris.android_samples.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chris.android_samples.databinding.ActivityColorPaletteBinding
import com.chris.android_samples.extension.getViewModel
import com.chris.android_samples.viewmodel.ColorPaletteViewModel

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

        binding.startButton.setOnClickListener {
            viewModel.generate()
        }
    }
}