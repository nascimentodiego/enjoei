package br.com.enjoei.app.presentation.feature.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.enjoei.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_home)
                }
                R.id.navigation_search -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_search)
                }
                R.id.navigation_sell -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_sell)
                }
                R.id.navigation_message -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_messageFragment)
                }
                else -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_perfilFragment)
                }
            }

            true
        }
        bottomNavigation.menu.getItem(0).isChecked = true
    }
}
