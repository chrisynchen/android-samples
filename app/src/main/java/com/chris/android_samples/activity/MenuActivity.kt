package com.chris.android_samples.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chris.android_samples.R
import com.chris.android_samples.adapter.MenuAdapter
import com.chris.android_samples.data.Menu
import com.chris.android_samples.viewholder.MenuViewHolder
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(),
    MenuViewHolder.Listener {

    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        menuAdapter =
            MenuAdapter(this, buildMenus())

        recyclerView.apply {
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
        }
    }
}