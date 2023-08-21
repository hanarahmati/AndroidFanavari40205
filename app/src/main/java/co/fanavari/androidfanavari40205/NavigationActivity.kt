package co.fanavari.androidfanavari40205

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.fanavari.androidfanavari40205.databinding.ActivityNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfig : AppBarConfiguration

    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navHostFragment = supportFragmentManager.
        findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        drawerLayout = binding.drawerLayout

        intent.extras?.let {
            if(it.getBoolean("PAGE_DB")){
                val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
                navGraph.setStartDestination(R.id.tasksFragment)
                navController.graph = navGraph
            }
        }

        binding.navigationView.setupWithNavController(navController)
        binding.bottomNavView.setupWithNavController(navController)

        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)

        /*listener = NavController.OnDestinationChangedListener { controller, destination,
                                                                arguments ->
            if(destination.id == R.id.firstFragment){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.green_500)))
                }

            }
            else if (destination.id == R.id.secondFragment){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.purple_200)))
                }

            }
        }*/
    }

    override fun onPause() {
        super.onPause()
       // navController.addOnDestinationChangedListener(listener)
    }

    override fun onResume() {
        super.onResume()
        //navController.addOnDestinationChangedListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()

    }

}
const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1