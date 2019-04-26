package br.com.enjoei.app.presentation.feature.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import br.com.enjoei.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.mainFragment)).build()

        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainFragment -> {
                    navController.navigate(R.id.action_mainFragment_to_home)
                }
                R.id.search -> {
                    navController.navigate(R.id.action_mainFragment_to_search)
                }
                R.id.sell -> {
                    navController.navigate(R.id.action_mainFragment_to_sell)
                }
                R.id.perfilFragment -> {
                    navController.navigate(R.id.action_mainFragment_to_messageFragment)
                }
                else -> {
                    navController.navigate(R.id.action_mainFragment_to_perfilFragment)
                }
            }
            true
        }
        bottomNavigation.menu.getItem(0).isChecked = true
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
}
