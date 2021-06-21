package com.chris.android_samples.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chris.android_samples.R
import com.chris.android_samples.adapter.MenuAdapter
import com.chris.android_samples.color_palatte.ColorPaletteActivity
import com.chris.android_samples.data.Menu
import com.chris.android_samples.databinding.ActivityMenuBinding
import com.chris.android_samples.viewholder.MenuViewHolder

class MenuActivity : AppCompatActivity(),
    MenuViewHolder.Listener {

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuAdapter =
            MenuAdapter(this, buildMenus())

        binding.recyclerView.apply {
            context?.let {
                val linearLayoutManager = LinearLayoutManager(this@MenuActivity)
                val drawable = ContextCompat.getDrawable(this@MenuActivity, R.drawable.divider)
                val dividerItemDecoration =
                    DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
                drawable?.let { d ->
                    dividerItemDecoration.setDrawable(d)
                }
                addItemDecoration(dividerItemDecoration)
                layoutManager = linearLayoutManager
                adapter = menuAdapter
            }
        }
    }

    private fun buildMenus(): List<Menu> {
        return mutableListOf<Menu>().apply {
            add(
                Menu(
                    Menu.START_SERVICE,
                    applicationContext.getString(R.string.start_service),
                    applicationContext.getString(R.string.start_service_subtitle)
                )
            )
            add(
                Menu(
                    Menu.BIND_SERVICE,
                    applicationContext.getString(R.string.bind_service),
                    applicationContext.getString(R.string.bind_service)
                )
            )
            add(
                Menu(
                    Menu.MEMORY_LEAK,
                    applicationContext.getString(R.string.memory_leak),
                    applicationContext.getString(R.string.memory_leak)
                )
            )
            add(
                Menu(
                    Menu.COLOR_PALETTE,
                    applicationContext.getString(R.string.color_palette),
                    applicationContext.getString(R.string.color_palette)
                )
            )
        }
    }

    override fun onItemClick(menu: Menu?) {

        when (menu?.type) {
            Menu.START_SERVICE -> startActivity(
                StartServiceExperimentActivity.createIntent(
                    this
                )
            )
            Menu.BIND_SERVICE -> startActivity(
                BindServiceExperimentActivity.createIntent(
                    this
                )
            )
            Menu.MEMORY_LEAK -> startActivity(
                MemoryLeakActivity.createIntent(
                    this
                )
            )
            Menu.COLOR_PALETTE -> startActivity(
                ColorPaletteActivity.createIntent(
                    this
                )
            )
        }
    }
}