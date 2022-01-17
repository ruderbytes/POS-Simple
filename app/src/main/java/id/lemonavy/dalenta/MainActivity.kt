package id.lemonavy.dalenta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationUI()
    }

    private fun setupNavigationUI() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.productFragment,
                R.id.cartFragment,
                R.id.checkoutFragment,
            )
        )
        mainToolbar.setupWithNavController(navController, appBarConfiguration)

    }

}