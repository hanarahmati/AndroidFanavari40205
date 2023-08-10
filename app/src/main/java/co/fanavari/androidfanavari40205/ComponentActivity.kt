package co.fanavari.androidfanavari40205

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.fanavari.androidfanavari40205.databinding.ActivityComponentBinding
import com.google.android.material.bottomappbar.BottomAppBar

class ComponentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComponentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component)

        binding = ActivityComponentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle edit text press
                    true
                }
                R.id.favorite -> {
                    showToast("favorite clicked")
                    // Handle favorite icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.favorite -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        binding.floatingActionButton.setOnClickListener {
            if(binding.bottomAppBar.menuAlignmentMode == BottomAppBar.MENU_ALIGNMENT_MODE_START)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }
    }

}